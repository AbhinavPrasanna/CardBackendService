import json
from urllib import request

GRAPHQL_URL = "http://localhost:8080/graphql"
PAGE_SIZE = 200


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


def classify_flags(card):
    name = (card.get("cardName") or "").lower()
    return {
        "cashbackChoiceHotels": "choice" in name,
        "cashbackHyattHotels": "hyatt" in name,
        "cashbackHiltonHotels": "hilton" in name,
        "cashbackMarriottHotels": "marriott" in name or "bonvoy" in name,
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


def main():
    cards = fetch_all_cards()
    updated = 0
    unchanged = 0
    for card in cards:
        computed = classify_flags(card)
        current = {
            "cashbackChoiceHotels": bool(card.get("cashbackChoiceHotels")),
            "cashbackHyattHotels": bool(card.get("cashbackHyattHotels")),
            "cashbackHiltonHotels": bool(card.get("cashbackHiltonHotels")),
            "cashbackMarriottHotels": bool(card.get("cashbackMarriottHotels")),
        }
        if current == computed:
            unchanged += 1
            continue

        post_graphql(
            UPDATE_CARD_MUTATION,
            {
                "name": card["cardName"],
                "input": build_input(card, computed),
            },
        )
        updated += 1

    print(f"Total cards: {len(cards)}")
    print(f"Updated cards: {updated}")
    print(f"Unchanged cards: {unchanged}")


if __name__ == "__main__":
    main()
