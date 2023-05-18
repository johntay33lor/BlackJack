
public class Card {

    //Variables
    private Suit suit;
    private Rank rank;

    //create a card given a suit and a value.
    public Card(Suit suit, Rank rank) {
        this.suit = suit;
        this.rank = rank;
    }

    //Generate getters
    public int getValue() {
        return rank.rankValue ;
    }
    public Suit getSuit() {
        return suit;
    }

    public Rank getRank() {
        return rank;
    }
    public String toString() {
        return ("[" + rank +" of " + suit + "] (" +this.getValue()+")" );
    }

    public Card(Card card){
        this.suit = card.getSuit();
        this.rank = card.getRank();
    }
}