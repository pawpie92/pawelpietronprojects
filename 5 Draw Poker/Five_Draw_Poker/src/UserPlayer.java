/*
 * User interaction class that contains cards in the players hand, as well as methods to operate on cards
 * Author: Pawel Pietron
 *
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class UserPlayer implements CardConstants{

    protected ArrayList<Card> hand = new ArrayList<Card>();
    protected int handEvaluateValue;
    protected int handScoreValue;

    public void setEvaluateHandValue(int handValue) { this.handEvaluateValue = handValue; }
    public int getHandEvaluateValue() { return handEvaluateValue; }
    public void setHandScoreValue(int handValue) { this.handScoreValue = handValue; }
    public int getHandScoreValue() { return handScoreValue; }

    protected Collections sorter;

    //Methods that operates on player's hand
    public void takeCard(Card card) { this.hand.add(card); }
    public ArrayList<Card> getHand() { return hand; }
    public Card getCardFromHand(int index)
    {
        return this.hand.get(index);
    }
    public void discardCard(int index){
        this.hand.remove(index);
    }
    public void replaceCard(int index, Card card) { this.hand.add(index,card);}
    public void giveCardsBack() {this.hand.removeAll(this.hand);}

    //Check if player has an ace
    public boolean aceExist()
    {
            if (hand.get(0).getRank() == ACE)
            {
                return true;
            }
            return false;
    }

    //Sort player's cards by value
    public void sortHand()
    {
        this.sorter.sort(hand, new Comparator<Card>() {
            @Override
            public int compare(Card o1, Card o2) {
                return o1.getValue() > o2.getValue() ? -1 : (o1.getValue() < o2.getValue() ? 1 : 0);
            }
        });
    }

    //Print player's hand
    public void printHand()
    {
        for(Card card: hand)
        {
            String cardInfo = String.format(" %d) %c%c" , hand.indexOf(card) + 1, card.getRank(), card.getColor());
            System.out.print(cardInfo);
        }
        System.out.println();
    }

}
