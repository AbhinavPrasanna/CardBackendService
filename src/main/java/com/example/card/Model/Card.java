package com.example.card.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "card")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "card_name")
    @NotNull
    private String cardName;

    @Column(name = "card_type")
    @NotNull
    private String cardType;

    @Column(name = "card_bank")
    @NotNull
    private String cardBank;

    @Column(name = "has_annual_fee")
    @NotNull
    private boolean hasAnnualFee;

    @Column(name = "annual_fee")
    private int annualFee;

    @Column(name = "rating")
    @NotNull
    private double rating;

    @Column(name = "bonus")
    private long bonus;

    @Column(name = "bonus_spend")
    private long bonusSpend;

    @Column(name = "cashback_flat")
    private double cashbackFlat;

    @Column(name = "cashback_travel")
    private double cashbackTravel;

    @Column(name = "cashback_dining")
    private double cashbackDining;

    @Column(name = "cashback_grocery")
    private double cashbackGrocery;

    @Column(name = "cashback_gas")
    private double cashbackGas;

    @Column(name = "cashback_pharmacy")
    private double cashbackPharmacy;

    @Column(name = "cashback_lyft")
    private double cashbackLyft;

    @Column(name = "cashback_officesupply")
    private double cashbackOfficeSupply;

    @Column(name = "cashback_services")
    private double cashbackServices;

    @Column(name = "cashback_brand")
    private double cashbackBrand;

    @Column(name = "cashback_other")
    private double cashbackOther;

    @Column(name = "credit_score")
    private int creditScore;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCardBank() {
        return cardBank;
    }

    public void setCardBank(String cardBank) {
        this.cardBank = cardBank;
    }

    public boolean isHasAnnualFee() {
        return hasAnnualFee;
    }

    public void setHasAnnualFee(boolean hasAnnualFee) {
        this.hasAnnualFee = hasAnnualFee;
    }

    public int getAnnualFee() {
        return annualFee;
    }

    public void setAnnualFee(int annualFee) {
        this.annualFee = annualFee;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public long getBonus() {
        return bonus;
    }

    public void setBonus(long bonus) {
        this.bonus = bonus;
    }

    public long getBonusSpend() {
        return bonusSpend;
    }

    public void setBonusSpend(long bonusSpend) {
        this.bonusSpend = bonusSpend;
    }

    public double getCashbackFlat() {
        return cashbackFlat;
    }

    public void setCashbackFlat(double cashbackFlat) {
        this.cashbackFlat = cashbackFlat;
    }

    public double getCashbackTravel() {
        return cashbackTravel;
    }

    public void setCashbackTravel(double cashbackTravel) {
        this.cashbackTravel = cashbackTravel;
    }

    public double getCashbackDining() {
        return cashbackDining;
    }

    public void setCashbackDining(double cashbackDining) {
        this.cashbackDining = cashbackDining;
    }

    public double getCashbackGrocery() {
        return cashbackGrocery;
    }

    public void setCashbackGrocery(double cashbackGrocery) {
        this.cashbackGrocery = cashbackGrocery;
    }

    public double getCashbackGas() {
        return cashbackGas;
    }

    public void setCashbackGas(double cashbackGas) {
        this.cashbackGas = cashbackGas;
    }

    public double getCashbackPharmacy() {
        return cashbackPharmacy;
    }

    public void setCashbackPharmacy(double cashbackPharmacy) {
        this.cashbackPharmacy = cashbackPharmacy;
    }

    public double getCashbackLyft() {
        return cashbackLyft;
    }

    public void setCashbackLyft(double cashbackLyft) {
        this.cashbackLyft = cashbackLyft;
    }

    public double getCashbackOfficeSupply() {
        return cashbackOfficeSupply;
    }

    public void setCashbackOfficeSupply(double cashbackOfficeSupply) {
        this.cashbackOfficeSupply = cashbackOfficeSupply;
    }

    public double getCashbackServices() {
        return cashbackServices;
    }

    public void setCashbackServices(double cashbackServices) {
        this.cashbackServices = cashbackServices;
    }

    public double getCashbackBrand() {
        return cashbackBrand;
    }

    public void setCashbackBrand(double cashbackBrand) {
        this.cashbackBrand = cashbackBrand;
    }

    public double getCashbackOther() {
        return cashbackOther;
    }

    public void setCashbackOther(double cashbackOther) {
        this.cashbackOther = cashbackOther;
    }

    public int getCreditScore() {
        return creditScore;
    }

    public void setCreditScore(int creditScore) {
        this.creditScore = creditScore;
    }
}