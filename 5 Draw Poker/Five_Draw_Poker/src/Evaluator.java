/*
 * This class purpose is to check the cards of player and give him corresponding score
 * Author: Pawel Pietron
 *
 */


import java.util.ArrayList;

public class Evaluator {

    private int evaluateValue = 1;
    private String combinations = "High Card";
    private ArrayList<Integer> rankScores = new ArrayList<Integer>();


    public void welcomeMessage()
    {
        System.out.println("Welcome to 5 Draw Poker Game. Today you are going to taste an gambling adventure.");
        System.out.println("Card labeling goes as follows:");
        System.out.print("Rank Characters\n" +
                "\uF0B7 2 - Two\n" +
                "\uF0B7 3 - Three\n" +
                "\uF0B7 4 - Four\n" +
                "\uF0B7 5 - Five\n" +
                "\uF0B7 6 - Six\n" +
                "\uF0B7 7 - Seven\n" +
                "\uF0B7 8 - Eight\n" +
                "\uF0B7 9 - Nine\n" +
                "\uF0B7 T - Ten\n" +
                "\uF0B7 J - Jack\n" +
                "\uF0B7 Q - Queen\n" +
                "\uF0B7 K - King\n" +
                "\uF0B7 A - Ace\n" +
                "Suit Characters\n" +
                "\uF0B7 C - Clubs (Black)\n" +
                "\uF0B7 D - Diamonds (Red)\n" +
                "\uF0B7 H - Hearts (Red)\n" +
                "\uF0B7 S - Spades (Black)");
    }

    public String getCombinations() {
        return combinations;
    }
    public int getEvaluateValue() { return evaluateValue; }

    //Check for combinations with the same ranks(One pair, Two pair etc.)
    public void sameRanks(ArrayList<Card> hand)
    {
        int index = 0;
        //Used only two counters since we evaluating only 5 cards
        int numOfMatches = 0;
        int numOfMatches2 = 0;
        boolean rankSwitch = false;
        boolean skipCard = false;

        for(Card card : hand)
        {

            if(index > 0){
                    //If previous card matched skip it
                    if(card.getRank() == hand.get(index - 1).getRank())
                    {
                        index++;
                        continue;
                    }
                    //otherwise switch counter for the next rank
                    else
                        //if a first card did not have any matches
                        //It won't change the counter
                        if(numOfMatches != 0 && index != 1)
                            rankSwitch = true;
            }
                //Selectively checking for matching cards
                for(int i = index + 1; i < hand.size(); i++)
                {
                    if(card.getRank() == hand.get(i).getRank()){
                        rankScores.add(card.getValue());
                        if(!rankSwitch)
                            numOfMatches++;
                        else
                            numOfMatches2++;
                    }

                }
            index++;
        }
        //check for full house
        if((numOfMatches == 2 && numOfMatches2 == 1) || (numOfMatches == 1 && numOfMatches2 == 2))
        {
            this.evaluateValue = 7;
            this.combinations = "Full House" ;
        }
        else
            {
                //check for Two pair
                if(numOfMatches == 1 && numOfMatches2 == 1)
                {
                    this.evaluateValue = 3;
                    this.combinations = "Two Pair" ;
                }
                else
                    {
                        //check for One pair
                        if(numOfMatches == 1 || numOfMatches2 == 1)
                        {
                            this.evaluateValue = 2;
                            this.combinations = "One Pair" ;
                        }
                        //check for Three of a kind
                        else if(numOfMatches == 2 || numOfMatches2 == 2)
                        {
                            this.evaluateValue = 4;
                            this.combinations = "Three of a kind" ;
                        }
                        //check for Four of a kind
                        else if(numOfMatches == 3 || numOfMatches2 == 3)
                        {
                            this.evaluateValue = 8;
                            this.combinations = "Four of a Kind" ;
                        }
                        //If all checks will not mark any combination mark it as high card
                        else
                        {
                            this.evaluateValue = 1;
                            this.combinations = "High Card" ;
                        }
                    }
        }

    }

    //check if all cards have the same color
    public boolean isFlush(ArrayList<Card> hand)
    {
        int index = 0;
        //boundary check
        if(index == hand.size() - 1)
            return true;
        for(Card card : hand)
        {
            if(card.getColor() != hand.get(index + 1).getColor())
                return false;
            index++;
        }
        return true;
    }
    //Check if cards follow the sequence
    public boolean isStaright(ArrayList<Card> hand)
    {
        int index = 0;
        for(Card card : hand)
        {
            //boundary check
            if(index == hand.size() - 1)
                break;
            //Exception check for 5 4 3 2 A sequence and skip the card due to sort by value
            if(card.getRank() == 'A' && hand.get(hand.size() - 1).getRank() == '2'){
                index++;
                continue;
            }
            //check cards by sequence
            if(card.getValue() != hand.get(index + 1).getValue() + 1)
                return false;
            index++;
        }
        return true;
    }

    //Main method for the evaluator
    public void evaluate(ArrayList<Card> hand)
    {
        if(isStaright(hand) && isFlush(hand))
        {
            this.evaluateValue = 9;
            this.combinations = "Straight Flush";
        }
        else if(isStaright(hand))
        {
            this.evaluateValue = 5;
            this.combinations = "Straight";
        }
        else if(isFlush(hand))
        {
            this.evaluateValue = 6;
            this.combinations = "Flush";
        }
        else
            sameRanks(hand);
    }
    public int giveScore(ArrayList<Card> hand)
    {
        int sum = 0;
        if(rankScores.size() > 0)
            for(Integer s : rankScores)
                sum += s;
        else
            sum = hand.get(0).getValue();

        rankScores.removeAll(rankScores);
        return (sum+evaluateValue)*evaluateValue;
    }

}

