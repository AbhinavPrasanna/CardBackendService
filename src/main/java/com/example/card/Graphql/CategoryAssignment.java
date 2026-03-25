package com.example.card.Graphql;

public class CategoryAssignment {

  private final String category;
  private final double monthlySpend;
  private final double effectiveRate;
  private final double annualCashback;

  public CategoryAssignment(
      String category, double monthlySpend, double effectiveRate, double annualCashback) {
    this.category = category;
    this.monthlySpend = monthlySpend;
    this.effectiveRate = effectiveRate;
    this.annualCashback = annualCashback;
  }

  public String getCategory() {
    return category;
  }

  public double getMonthlySpend() {
    return monthlySpend;
  }

  public double getEffectiveRate() {
    return effectiveRate;
  }

  public double getAnnualCashback() {
    return annualCashback;
  }
}
