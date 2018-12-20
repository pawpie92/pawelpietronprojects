import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/*
 * Behavioral Test cases for AI players
 * Idea of these tests is to check if AI strategy algorithms are working correctly.
 * For example, if AI player already has pair, two pairs or three of a kind, player should discard the other cards
 * Or if card is out of sequence or is player's hand has only one card of different color it discards corresponding single card
 * Author Pawel Pietron
 */

public class ComputerPlayerTest implements CardConstants {

    private ComputerPlayer testPlayer = new ComputerPlayer();
    private ComputerPlayer testPlayer2 = new ComputerPlayer();
    private ArrayList<Card> initialHand = new ArrayList<>();
    private CardDeck deckPile = new CardDeck();
    private CardDeck discardPile = new CardDeck();

    //Setup a testing deck
    @Before
    public void setup()
    {
        deckPile.initializeDeck();
        deckPile.shuffleCards();
    }

    @Test //AI should discard King
    public void makeMove() {
        System.out.println("Test 1");
        testPlayer.takeCard(new Card(KING,HEARTS));
        testPlayer.takeCard(new Card(TEN,DIAMONDS));
        testPlayer.takeCard(new Card(TEN,CLUBS));
        testPlayer.takeCard(new Card(FOUR,SPADES));
        testPlayer.takeCard(new Card(FOUR,DIAMONDS));
        testPlayer.sortHand();
        System.out.print("Initial hand: ");
        testPlayer.printHand();
        for(int i = 0; i < testPlayer.hand.size(); i++)
            initialHand.add(testPlayer.getHand().get(i));
        testPlayer.makeMove(discardPile, deckPile);
        assertNotSame(initialHand.get(0), testPlayer.getHand().get(0));
    }
    @Test //AI should discard 4
    public void makeMove2() {

        System.out.println("Test 2");
        testPlayer.takeCard(new Card(KING,HEARTS));
        testPlayer.takeCard(new Card(KING,DIAMONDS));
        testPlayer.takeCard(new Card(TEN,CLUBS));
        testPlayer.takeCard(new Card(TEN,SPADES));
        testPlayer.takeCard(new Card(FOUR,DIAMONDS));
        testPlayer.sortHand();
        System.out.print("Initial hand: ");
        testPlayer.printHand();
        for(int i = 0; i < testPlayer.hand.size(); i++)
            initialHand.add(testPlayer.getHand().get(i));
        testPlayer.makeMove(discardPile, deckPile);
        testPlayer.sortHand();
        assertNotSame(initialHand.get(4), testPlayer.getHand().get(4));
    }
    @Test //AI should discard Ten
    public void makeMove3() {

        System.out.println("Test 3");
        testPlayer.takeCard(new Card(KING,HEARTS));
        testPlayer.takeCard(new Card(KING,DIAMONDS));
        testPlayer.takeCard(new Card(TEN,CLUBS));
        testPlayer.takeCard(new Card(FOUR,SPADES));
        testPlayer.takeCard(new Card(FOUR,DIAMONDS));
        testPlayer.sortHand();
        System.out.print("Initial hand: ");
        testPlayer.printHand();
        for(int i = 0; i < testPlayer.hand.size(); i++)
            initialHand.add(testPlayer.getHand().get(i));
        testPlayer.makeMove(discardPile, deckPile);
        assertNotSame(initialHand.get(2), testPlayer.getHand().get(2));
    }

    @Test //AI should discard King and 4
    public void makeMove4() {

        System.out.println("Test 4");
        testPlayer.takeCard(new Card(KING,HEARTS));
        testPlayer.takeCard(new Card(TEN,DIAMONDS));
        testPlayer.takeCard(new Card(TEN,CLUBS));
        testPlayer.takeCard(new Card(TEN,SPADES));
        testPlayer.takeCard(new Card(TEN,DIAMONDS));
        testPlayer.sortHand();
        System.out.print("Initial hand: ");
        testPlayer.printHand();
        for(int i = 0; i < testPlayer.hand.size(); i++)
            initialHand.add(testPlayer.getHand().get(i));
        testPlayer.makeMove(discardPile, deckPile);
        assertNotSame(initialHand.get(0), testPlayer.getHand().get(0));
        assertNotSame(initialHand.get(4), testPlayer.getHand().get(4));
    }
    @Test //AI should discard King and Ten
    public void makeMove5() {

        System.out.println("Test 5");
        testPlayer.takeCard(new Card(KING,HEARTS));
        testPlayer.takeCard(new Card(TEN,HEARTS));
        testPlayer.takeCard(new Card(FOUR,HEARTS));
        testPlayer.takeCard(new Card(FOUR,SPADES));
        testPlayer.takeCard(new Card(FOUR,DIAMONDS));
        testPlayer.sortHand();
        testPlayer2.sortHand();
        System.out.print("Initial hand: ");
        testPlayer.printHand();
        for(int i = 0; i < testPlayer.hand.size(); i++)
            initialHand.add(testPlayer.getHand().get(i));
        testPlayer.makeMove(discardPile, deckPile);
        assertNotSame(initialHand.get(0), testPlayer.getHand().get(0));
        assertNotSame(initialHand.get(1), testPlayer.getHand().get(1));
    }
    @Test //AI should discard 4 Clubs
    public void makeMove6() {

        System.out.println("Test 6");
        testPlayer.takeCard(new Card(KING,DIAMONDS));
        testPlayer.takeCard(new Card(TEN,DIAMONDS));
        testPlayer.takeCard(new Card(FOUR,CLUBS));
        testPlayer.takeCard(new Card(THREE,DIAMONDS));
        testPlayer.takeCard(new Card(TWO,DIAMONDS));
        testPlayer.sortHand();
        System.out.print("Initial hand: ");
        testPlayer.printHand();
        initialHand.addAll(testPlayer.getHand());
        testPlayer.makeMove(discardPile, deckPile);
        assertNotSame(initialHand.get(2), testPlayer.getHand().get(2));

    }
    @Test //AI should discard 4 - out of sequence
    public void makeMove7() {

        System.out.println("Test 7");
        testPlayer.takeCard(new Card(KING,DIAMONDS));
        testPlayer.takeCard(new Card(QUEEN,SPADES));
        testPlayer.takeCard(new Card(JACK,CLUBS));
        testPlayer.takeCard(new Card(TEN,HEARTS));
        testPlayer.takeCard(new Card(FOUR,DIAMONDS));
        testPlayer.sortHand();
        System.out.print("Initial hand: ");
        testPlayer.printHand();
        initialHand.addAll(testPlayer.getHand());
        testPlayer.makeMove(discardPile, deckPile);
        assertSame(initialHand.get(2),testPlayer.getHand().get(2));
        assertSame(initialHand.get(3),testPlayer.getHand().get(3));
        assertNotSame(initialHand.get(4), testPlayer.getHand().get(4));

    }
    @Test //AI should discard King - out of sequence
    public void makeMove8() {
        System.out.println("Test 8");
        testPlayer.takeCard(new Card(KING, DIAMONDS));
        testPlayer.takeCard(new Card(JACK, SPADES));
        testPlayer.takeCard(new Card(TEN, CLUBS));
        testPlayer.takeCard(new Card(NINE, HEARTS));
        testPlayer.takeCard(new Card(EIGHT, DIAMONDS));
        testPlayer.sortHand();
        System.out.print("Initial hand: ");
        testPlayer.printHand();
        initialHand.addAll(testPlayer.getHand());
        testPlayer.makeMove(discardPile, deckPile);
        assertNotSame(initialHand.get(0), testPlayer.getHand().get(0));
        assertSame(initialHand.get(1), testPlayer.getHand().get(1));
        assertSame(initialHand.get(2), testPlayer.getHand().get(2));
        assertSame(initialHand.get(3), testPlayer.getHand().get(3));
        assertSame(initialHand.get(4), testPlayer.getHand().get(4));
    }

    // Cards are sorted again after assertion and displayed to user.
    @After
    public void printHand()
    {
        testPlayer.sortHand();
        System.out.print("After making move hand: ");
        testPlayer.printHand();
    }

}