package com.example.card.Graphql;

import java.util.List;

public class CardLineupResult {

  private final List<CardLineupEntry> entries;
  private final double totalAnnualCashback;
  private final int totalAnnualFees;
  private final double totalNetAnnualValue;
  private final double totalMonthlySpend;

  public CardLineupResult(
      List<CardLineupEntry> entries,
      double totalAnnualCashback,
      int totalAnnualFees,
      double totalNetAnnualValue,
      double totalMonthlySpend) {
    this.entries = entries;
    this.totalAnnualCashback = totalAnnualCashback;
    this.totalAnnualFees = totalAnnualFees;
    this.totalNetAnnualValue = totalNetAnnualValue;
    this.totalMonthlySpend = totalMonthlySpend;
  }

  public List<CardLineupEntry> getEntries() {
    return entries;
  }

  public double getTotalAnnualCashback() {
    return totalAnnualCashback;
  }

  public int getTotalAnnualFees() {
    return totalAnnualFees;
  }

  public double getTotalNetAnnualValue() {
    return totalNetAnnualValue;
  }

  public double getTotalMonthlySpend() {
    return totalMonthlySpend;
  }
}
