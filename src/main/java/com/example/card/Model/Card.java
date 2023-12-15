@Entity
@Table(name = "card")
public class Card{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "card_name") @NotNull
    private String cardName;

    @Column(name = "card_type") @NotNull
    private String cardType;

    @Column(name = "card_bank") @NotNull
    private String cardBank;

    @Column(name = "has_annual_fee") @NotNull
    private boolean hasAnnualFee;

    @Column(name = "annual_fee")
    private int annualFee;

    @Column(name = "rating") @NotNull
    private double rating;

    @Column(name = "bonus")
    private long bonus;

    @Column(name = "bonus_spend")
    private long bonusSpend;

    @Column(name ="cashback_flat")
    private double cashbackFlat;

    @Column(name="cashback_travel")
    private double cashbackTravel;

    @Column(name="cashback_dining")
    private double cashbackDining;

    @Column(name="cashback_grocery")
    private double cashbackGrocery;

    @Column(name="cashback_gas")
    private double cashbackGas;

    @Column(name="cashback_pharmacy")
    private double cashbackPharmacy;

    @Column(name="cashback_lyft")
    private double cashbackLyft;
    
    @Column(name="cashback_officesupply")
    private double cashbackOfficeSupply;

    @Column(name="cashback_services")
    private double cashbackServices;

    @Column(name="cashback_brand")
    private double cashbackBrand;

    @Column(name="cashback_other")
    private double cashbackOther;

    @Column(name="credit_score")
    private int creditScore;

}