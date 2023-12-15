package 
@Repository
public interface CardRepository extends CrudRespository<Card,Long>{
    Optional<Card> findByCardName(String cardName);
    List<Card> findByCardType(String cardType);
    List<Card> findByCardBank(String cardBank);
    List<Card> findTop4ByOrderByRatingDesc();
}