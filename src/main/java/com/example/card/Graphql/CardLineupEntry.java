package com.example.card.Graphql;

import com.example.card.Model.Card;
import java.util.List;

public class CardLineupEntry {

  private final Card card;
  private final List<CategoryAssignment> assignedCategories;
  private final double annualCashback;
  private final int annualFee;
  private final double netAnnualValue;

  public CardLineupEntry(
      Card card,
      List<CategoryAssignment> assignedCategories,
      double annualCashback,
      int annualFee,
      double netAnnualValue) {
    this.card = card;
    this.assignedCategories = assignedCategories;
    this.annualCashback = annualCashback;
    this.annualFee = annualFee;
    this.netAnnualValue = netAnnualValue;
  }

  public Card getCard() {
    return card;
  }

  public List<CategoryAssignment> getAssignedCategories() {
    return assignedCategories;
  }

  public double getAnnualCashback() {
    return annualCashback;
  }

  public int getAnnualFee() {
    return annualFee;
  }

  public double getNetAnnualValue() {
    return netAnnualValue;
  }
}
