import csv
import json
from pathlib import Path
from urllib import request

GRAPHQL_URL = "http://localhost:8080/graphql"
PAGE_SIZE = 200

FIELDS = [
    "id",
    "cardName",
    "cardType",
    "cardBank",
    "hasAnnualFee",
    "annualFee",
    "rating",
    "bonus",
    "bonusSpend",
    "cashbackFlat",
    "cashbackTravel",
    "cashbackDining",
    "cashbackGrocery",
    "cashbackGas",
    "cashbackPharmacy",
    "cashbackLyft",
    "cashbackOfficeSupply",
    "cashbackServices",
    "cashbackBrand",
    "cashbackOther",
    "cashbackChoiceHotels",
    "cashbackHyattHotels",
    "cashbackHiltonHotels",
    "cashbackMarriottHotels",
    "creditScore",
    "imageS3Key",
    "imageSourceUrl",
]

FETCH_CARDS_QUERY = """
query FetchCards($page: Int!, $size: Int!) {
  cards(page: $page, size: $size) {
    content {
      id
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
    if body.get("errors"):
        raise RuntimeError(body["errors"][0].get("message", "GraphQL error"))
    return body.get("data", {})


def fetch_all_cards():
    cards = []
    page = 0
    while True:
        data = post_graphql(FETCH_CARDS_QUERY, {"page": page, "size": PAGE_SIZE})
        cards_page = data["cards"]
        cards.extend(cards_page["content"])
        if not cards_page.get("hasNext"):
            break
        page += 1
    return cards


def main():
    cards = fetch_all_cards()
    output_path = Path("exports") / "cards_export_all_entries.csv"
    output_path.parent.mkdir(parents=True, exist_ok=True)

    with output_path.open("w", newline="", encoding="utf-8") as file:
        writer = csv.DictWriter(file, fieldnames=FIELDS)
        writer.writeheader()
        for card in cards:
            writer.writerow({field: card.get(field) for field in FIELDS})

    print(f"Exported {len(cards)} cards")
    print(f"CSV path: {output_path.resolve()}")


if __name__ == "__main__":
    main()
