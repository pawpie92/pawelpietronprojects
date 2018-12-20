/*
 * This class defines a deck of cards and is supplied with methods to operate with the stack of cards.
 * Author: Pawel Pietron
 *
 */


import java.util.Collections;
import java.util.Stack;

public class CardDeck implements CardConstants {
    private Stack<Card> Deck = new Stack();
    private Collections dealer ;


    public void initializeDeck()
    {
        this.initializeColorCards(CLUBS);
        this.initializeColorCards(DIAMONDS);
        this.initializeColorCards(HEARTS);
        this.initializeColorCards(SPADES);
    }

    //Helper method for initializing the deck
    private void initializeColorCards(char c)
    {
        this.Deck.push(new Card(TWO , c));
        this.Deck.push(new Card(THREE , c));
        this.Deck.push(new Card(FOUR , c));
        this.Deck.push(new Card(FIVE , c));
        this.Deck.push(new Card(SIX , c));
        this.Deck.push(new Card(SEVEN , c));
        this.Deck.push(new Card(EIGHT , c));
        this.Deck.push(new Card(NINE , c));
        this.Deck.push(new Card(TEN , c));
        this.Deck.push(new Card(JACK , c));
        this.Deck.push(new Card(QUEEN , c));
        this.Deck.push(new Card(KING , c));
        this.Deck.push(new Card(ACE , c));
    }


    public void addCard(Card card){ this.Deck.push(card); }

    public void shuffleCards(){
        this.dealer.shuffle(Deck);
    }

    public Card dealCard(){
       return this.Deck.pop();
    }

}
