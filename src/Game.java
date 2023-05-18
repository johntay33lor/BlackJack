import java.util.Scanner;
import java.util.InputMismatchException;
//this class contains functionality of the game.
public class Game {
    private Deck deck;
    private Deck discarded;
    private Dealer dealer;
    private Player player;
    private int wins;
    private int losses;
    private int pushes;
    private int bet;

    //constructor
    public Game() {

        //Create a new deck with 52 cards
        deck = new Deck(true);
        //Create a new empty deck
        discarded = new Deck();

        //Create the People
        dealer = new Dealer();
        player = new Player();


        //Shuffle the deck and start the first round
        deck.shuffle();
        startRound();
    }

    //This method allows to make a bet
    private void makeBet(Player player) {
//        int bet = 0;
        Scanner scanner = new Scanner(System.in);
        // Use a loop to ensure the bet is at least $5
        while (bet < 5) {
            while (bet < 5) {
                try {
                    System.out.print("Enter your bet amount (minimum $5): ");
                    bet = scanner.nextInt();
                    if (bet < 5) {
                        System.out.println("Invalid bet amount. Minimum bet is $5.");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a valid bet amount.");
                    scanner.nextLine(); // Clear the input buffer
                }
            }
            // Deduct the bet amount from the player's balance
            player.setBalance(player.getBalance() - bet);
            System.out.println("Your balance is: $" + player.getBalance());
        }

    }
    //This  method will handle the logic for each round
    private void startRound() {
        bet =0;

        if(wins>0 || losses>0 || pushes > 0){
            System.out.println();
            System.out.println("Starting Next Round... Wins: " + wins + " Losses: "+ losses+ " Pushes: "+pushes);
            dealer.getHand().discardHandToDeck(discarded);
            player.getHand().discardHandToDeck(discarded);
        }

        //Check to make sure the deck has at least 4 cards left
        if(deck.cardsLeft() < 4){
            deck.reloadDeckFromDiscard(discarded);
        }
        makeBet(player);
        //Give the dealer two cards
        dealer.getHand().takeCardFromDeck(deck);
        dealer.getHand().takeCardFromDeck(deck);

        //Give the player two cards
        player.getHand().takeCardFromDeck(deck);
        player.getHand().takeCardFromDeck(deck);

        //Print their hands
        dealer.printFirstHand();
        player.printHand();

        //Check if dealer has BlackJack to start
        if(dealer.hasBlackjack()){
            //Show the dealer has BlackJack
            dealer.printHand();

            //Check if the player also has BlackJack
            if(player.hasBlackjack()){
                //End the round with a push
                System.out.println("You both have 21 - Push.");
                pushes++;
            }
            else{
                System.out.println("Dealer has BlackJack. You lose.");
                dealer.printHand();
                losses++;
            }
            startRound();
        }

        //Check if player has blackjack to start
        //If we got to this point, we already know the dealer didn't have blackjack
        if(player.hasBlackjack()){
            System.out.println("You have Blackjack! You win!");
            wins++;
            startRound();
        }

        //Let the player decide what to do next
        player.makeDecision(deck, discarded);

        //Check if they busted
        if(player.getHand().calculatedValue() > 21){
            System.out.println("You have gone over 21.");
            //count the losses
            losses ++;
            //start the round over
            startRound();
        }

        //Now it's the dealer's turn
        dealer.printHand();
        while(dealer.getHand().calculatedValue()<17){
            dealer.hit(deck, discarded);
        }

        //Check who wins
        if(dealer.getHand().calculatedValue()>21){
            System.out.println("Dealer busts");
            wins++;
            player.setBalance(player.getBalance() + (2 * this.bet)); // Add the winnings to the player's balance
        }
        else if(dealer.getHand().calculatedValue() > player.getHand().calculatedValue()){
            System.out.println("You lose.");
            losses++;
        }
        else if(player.getHand().calculatedValue() > dealer.getHand().calculatedValue()){
            System.out.println("You win.");
            wins++;
            player.setBalance(player.getBalance() + (2 * bet)); // Add the winnings to the player's balance
        }
        else{
            System.out.println("Push.");
            pushes++;
            player.setBalance(player.getBalance() + bet); // Add the bet back to the player's balance
        }

        //display the winning player
        if (wins > losses) {
            System.out.println("You are currently winning!");
        } else if (losses > wins) {
            System.out.println("The dealer is currently winning!");
        } else {
            System.out.println("It is currently tied!");
        }

        //Start a new round
        startRound();

    }

}