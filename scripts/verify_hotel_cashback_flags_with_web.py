import json
import re
import time
from html import unescape
from urllib import parse, request

GRAPHQL_URL = "http://localhost:8080/graphql"
PAGE_SIZE = 200
SEARCH_DELAY_SECONDS = 0.35
SEARCH_TIMEOUT_SECONDS = 10


def post_graphql(query, variables=None):
    payload = json.dumps({"query": query, "variables": variables or {}}).encode("utf-8")
    req = request.Request(
        GRAPHQL_URL,
        data=payload,
        headers={"Content-Type": "application/json"},
        method="POST",
    )
    with request.urlopen(req, timeout=30) as response:
        body = json.loads(response.read().decode("utf-8"))
    if "errors" in body and body["errors"]:
        raise RuntimeError(body["errors"][0].get("message", "GraphQL error"))
    return body.get("data", {})


FETCH_CARDS_QUERY = """
query FetchCards($page: Int!, $size: Int!) {
  cards(page: $page, size: $size) {
    content {
      cardName
      cardType
      cardBank
      hasAnnualFee
      annualFee
      rating
      bonus
      bonusSpend
      cashbackFlat
      cashbackTravel
      cashbackDining
      cashbackGrocery
      cashbackGas
      cashbackPharmacy
      cashbackLyft
      cashbackOfficeSupply
      cashbackServices
      cashbackBrand
      cashbackOther
      cashbackChoiceHotels
      cashbackHyattHotels
      cashbackHiltonHotels
      cashbackMarriottHotels
      creditScore
      imageS3Key
      imageSourceUrl
    }
    hasNext
  }
}
"""

UPDATE_CARD_MUTATION = """
mutation UpdateCard($name: String!, $input: CardInput!) {
  updateCardByName(name: $name, input: $input) {
    cardName
  }
}
"""


def fetch_all_cards():
    cards = []
    page = 0
    while True:
        data = post_graphql(FETCH_CARDS_QUERY, {"page": page, "size": PAGE_SIZE})
        page_data = data["cards"]
        cards.extend(page_data["content"])
        if not page_data.get("hasNext"):
            break
        page += 1
    return cards


def strip_html(text):
    plain = re.sub(r"<script.*?>.*?</script>", " ", text, flags=re.IGNORECASE | re.DOTALL)
    plain = re.sub(r"<style.*?>.*?</style>", " ", plain, flags=re.IGNORECASE | re.DOTALL)
    plain = re.sub(r"<[^>]+>", " ", plain)
    plain = unescape(plain)
    plain = re.sub(r"\s+", " ", plain)
    return plain.lower()


def search_card_text(card_name):
    query = parse.quote_plus(f"{card_name} credit card")
    url = f"https://duckduckgo.com/html/?q={query}"
    req = request.Request(
        url,
        headers={
            "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 "
            "(KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36"
        },
    )
    try:
        with request.urlopen(req, timeout=SEARCH_TIMEOUT_SECONDS) as response:
            html = response.read().decode("utf-8", errors="ignore")
        return strip_html(html)
    except Exception:
        return ""


def compute_flags(card, search_text):
    name = (card.get("cardName") or "").lower()
    combined = f"{name} {search_text}"

    # Brand-specific signals can come from card name or web result text.
    choice = ("choice" in combined and "hotel" in combined) or "choice privileges" in combined
    hyatt = "hyatt" in combined
    hilton = "hilton" in combined
    marriott = "marriott" in combined or "bonvoy" in combined

    return {
        "cashbackChoiceHotels": choice,
        "cashbackHyattHotels": hyatt,
        "cashbackHiltonHotels": hilton,
        "cashbackMarriottHotels": marriott,
    }


def build_input(card, flags):
    return {
        "cardName": card.get("cardName"),
        "cardType": card.get("cardType"),
        "cardBank": card.get("cardBank"),
        "hasAnnualFee": card.get("hasAnnualFee"),
        "annualFee": card.get("annualFee"),
        "rating": float(card.get("rating") or 0),
        "bonus": float(card.get("bonus") or 0),
        "bonusSpend": float(card.get("bonusSpend") or 0),
        "cashbackFlat": float(card.get("cashbackFlat") or 0),
        "cashbackTravel": float(card.get("cashbackTravel") or 0),
        "cashbackDining": float(card.get("cashbackDining") or 0),
        "cashbackGrocery": float(card.get("cashbackGrocery") or 0),
        "cashbackGas": float(card.get("cashbackGas") or 0),
        "cashbackPharmacy": float(card.get("cashbackPharmacy") or 0),
        "cashbackLyft": float(card.get("cashbackLyft") or 0),
        "cashbackOfficeSupply": float(card.get("cashbackOfficeSupply") or 0),
        "cashbackServices": float(card.get("cashbackServices") or 0),
        "cashbackBrand": float(card.get("cashbackBrand") or 0),
        "cashbackOther": float(card.get("cashbackOther") or 0),
        "cashbackChoiceHotels": flags["cashbackChoiceHotels"],
        "cashbackHyattHotels": flags["cashbackHyattHotels"],
        "cashbackHiltonHotels": flags["cashbackHiltonHotels"],
        "cashbackMarriottHotels": flags["cashbackMarriottHotels"],
        "creditScore": card.get("creditScore"),
        "imageS3Key": card.get("imageS3Key"),
        "imageSourceUrl": card.get("imageSourceUrl"),
    }


def main():
    cards = fetch_all_cards()
    updated = 0
    unchanged = 0
    web_checked = 0

    for index, card in enumerate(cards, start=1):
        search_text = search_card_text(card["cardName"])
        web_checked += 1
        flags = compute_flags(card, search_text)

        current = {
            "cashbackChoiceHotels": bool(card.get("cashbackChoiceHotels")),
            "cashbackHyattHotels": bool(card.get("cashbackHyattHotels")),
            "cashbackHiltonHotels": bool(card.get("cashbackHiltonHotels")),
            "cashbackMarriottHotels": bool(card.get("cashbackMarriottHotels")),
        }

        if current == flags:
            unchanged += 1
        else:
            post_graphql(
                UPDATE_CARD_MUTATION,
                {
                    "name": card["cardName"],
                    "input": build_input(card, flags),
                },
            )
            updated += 1

        if index % 20 == 0:
            print(f"Checked {index}/{len(cards)} cards...")

        time.sleep(SEARCH_DELAY_SECONDS)

    print(f"Total cards: {len(cards)}")
    print(f"Web-checked cards: {web_checked}")
    print(f"Updated cards: {updated}")
    print(f"Unchanged cards: {unchanged}")


if __name__ == "__main__":
    main()
