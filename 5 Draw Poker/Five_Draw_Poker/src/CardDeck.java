/*
 * This class defines a deck of cards and is supplied with methods to operate with the stack of cards.
 * Author: Pawel Pietron
 *
 */


import java.util.Collections;
import java.util.Stack;

public class CardDeck {
    private Stack<Card> Deck = new Stack();
    private Collections dealer ;


    public void initializeDeck()
    {
        this.initializeColorCards('C');
        this.initializeColorCards('D');
        this.initializeColorCards('H');
        this.initializeColorCards('S');
    }

    //Helper method for initializing the deck
    private void initializeColorCards(char c)
    {
        this.Deck.push(new Card('2' , c));
        this.Deck.push(new Card('3' , c));
        this.Deck.push(new Card('4' , c));
        this.Deck.push(new Card('5' , c));
        this.Deck.push(new Card('6' , c));
        this.Deck.push(new Card('7' , c));
        this.Deck.push(new Card('8' , c));
        this.Deck.push(new Card('9' , c));
        this.Deck.push(new Card('T' , c));
        this.Deck.push(new Card('J' , c));
        this.Deck.push(new Card('Q' , c));
        this.Deck.push(new Card('K' , c));
        this.Deck.push(new Card('A' , c));
    }


    public void addCard(Card card){ this.Deck.push(card); }

    public void shuffleCards(){
        this.dealer.shuffle(Deck);
    }

    public Card dealCard(){
       return this.Deck.pop();
    }

}
