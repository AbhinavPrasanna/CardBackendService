package com.example.card.Graphql;

public class SpendingProfileInput {

  private int creditScore;
  private double monthlyTravel;
  private double monthlyDining;
  private double monthlyGrocery;
  private double monthlyGas;
  private double monthlyPharmacy;
  private double monthlyLyft;
  private double monthlyOfficeSupply;
  private double monthlyServices;
  private double monthlyBrand;
  private double monthlyOther;

  public int getCreditScore() {
    return creditScore;
  }

  public void setCreditScore(int creditScore) {
    this.creditScore = creditScore;
  }

  public double getMonthlyTravel() {
    return monthlyTravel;
  }

  public void setMonthlyTravel(double monthlyTravel) {
    this.monthlyTravel = monthlyTravel;
  }

  public double getMonthlyDining() {
    return monthlyDining;
  }

  public void setMonthlyDining(double monthlyDining) {
    this.monthlyDining = monthlyDining;
  }

  public double getMonthlyGrocery() {
    return monthlyGrocery;
  }

  public void setMonthlyGrocery(double monthlyGrocery) {
    this.monthlyGrocery = monthlyGrocery;
  }

  public double getMonthlyGas() {
    return monthlyGas;
  }

  public void setMonthlyGas(double monthlyGas) {
    this.monthlyGas = monthlyGas;
  }

  public double getMonthlyPharmacy() {
    return monthlyPharmacy;
  }

  public void setMonthlyPharmacy(double monthlyPharmacy) {
    this.monthlyPharmacy = monthlyPharmacy;
  }

  public double getMonthlyLyft() {
    return monthlyLyft;
  }

  public void setMonthlyLyft(double monthlyLyft) {
    this.monthlyLyft = monthlyLyft;
  }

  public double getMonthlyOfficeSupply() {
    return monthlyOfficeSupply;
  }

  public void setMonthlyOfficeSupply(double monthlyOfficeSupply) {
    this.monthlyOfficeSupply = monthlyOfficeSupply;
  }

  public double getMonthlyServices() {
    return monthlyServices;
  }

  public void setMonthlyServices(double monthlyServices) {
    this.monthlyServices = monthlyServices;
  }

  public double getMonthlyBrand() {
    return monthlyBrand;
  }

  public void setMonthlyBrand(double monthlyBrand) {
    this.monthlyBrand = monthlyBrand;
  }

  public double getMonthlyOther() {
    return monthlyOther;
  }

  public void setMonthlyOther(double monthlyOther) {
    this.monthlyOther = monthlyOther;
  }

  public double getTotalMonthlySpend() {
    return monthlyTravel
        + monthlyDining
        + monthlyGrocery
        + monthlyGas
        + monthlyPharmacy
        + monthlyLyft
        + monthlyOfficeSupply
        + monthlyServices
        + monthlyBrand
        + monthlyOther;
  }
}
