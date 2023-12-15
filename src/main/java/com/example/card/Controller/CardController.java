@RestController
@RequestMapping("/card")
public class CardController{

    @Autowired
    private CardRepository cardRepository;

    @GetMapping("/all")
    public ResponseEntity<List<Card>> getAllCards(){
        return ResponseEntity.ok(cardRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Card> getCardById(@PathVariable(value = "id") Long id){
        Optional<Card> card = cardRepository.findById(id);
        if(card.isPresent()){
            return ResponseEntity.ok(card.get());
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Card> getCardByName(@PathVariable(value = "name") String name){
        Optional<Card> card = cardRepository.findByCardName(name);
        if(card.isPresent()){
            return ResponseEntity.ok(card.get());
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/Top4RatingCards")
    public ResponseEntity<List<Card>> getTop4RatingCards(){
        List<Card> card = cardRepository.findTop4ByOrderByRatingDesc();
        if(card.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        else{
            return ResponseEntity.ok(card);
        }

    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<Card>> getCardByType(@PathVariable(value = "type") String type){
        List<Card> card = cardRepository.findByCardType(type);
        if(card.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        else{
            return ResponseEntity.ok(card);
        }
    }

    @GetMapping("/bank/{bank}")
    public ResponseEntity<List<Card>> getCardByBank(@PathVariable(value = "bank") String bank){
        List<Card> card = cardRepository.findByCardBank(bank);
        if(card.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        else{
            return ResponseEntity.ok(card);
        }
    }

    @GetMapping("/hasAnnualFee/{hasAnnualFee}")
    public ResponseEntity<List<Card>> getCardByHasAnnualFee(@PathVariable(value = "hasAnnualFee") boolean hasAnnualFee){
        List<Card> card = cardRepository.findByHasAnnualFee(hasAnnualFee);
        if(card.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        else{
            return ResponseEntity.ok(card);
        }
    }

    @PostMapping("/add"){
        cardRepository.save(card);
        Optional<Card> card = cardRepository.findByCardName(card.getCardName());
        if(card.isPresent()){
            return ResponseEntity.ok(card.get());
        }
        else{
            return ResponseEntity.notFound().build();
        } 
    }
    @PutMapping("/update/{name}"){
        Optional<Card> card = cardRepository.findByCardName(name);
        if(card.isPresent()){
            cardRepository.save(card);
            return ResponseEntity.ok(card.get());
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }




}