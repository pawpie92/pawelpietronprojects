/*
 * AI player class supplied with its own evaluator and behavioral methods
 * Author: Pawel Pietron
 *
 */



import java.util.Stack;

public class ComputerPlayer extends UserPlayer implements CardConstants{
    private Evaluator evaluator = new Evaluator();
    private int cardsDiscarded = 0;


    private void discard(CardDeck discardDeck, CardDeck deckPile, int index)
    {
        discardDeck.addCard(this.getCardFromHand(index));
        this.discardCard(index);
        this.replaceCard(index, deckPile.dealCard());
        this.cardsDiscarded++;
    }

    private void discardAtWill(CardDeck discardDeck, CardDeck deckPile)
    {
        if(aceExist())
        {
            discard(discardDeck, deckPile, 1);
            discard(discardDeck, deckPile, 2);
            discard(discardDeck, deckPile, 3);
            discard(discardDeck, deckPile, 4);
        }
        else {
            discard(discardDeck, deckPile, 2);
            discard(discardDeck, deckPile, 3);
            discard(discardDeck, deckPile, 4);
        }
    }

    private void discardNonMatching(CardDeck discardDeck, CardDeck deckPile)
    {
        Stack<Integer> notMatched = new Stack<Integer>();
        Integer index = new Integer(0);
        boolean skipCard = false;

        for(Card card : hand)
        {
            if(skipCard)
                continue;
            if(index == hand.size() - 1)
            {
                if(card.getRank() != hand.get(index - 1).getRank())
                    notMatched.push(index);
                break;
            }
            if(card.getRank() != hand.get(index + 1).getRank())
            {
                if(index == 0 ){
                    notMatched.push(index);
                    index++;
                    continue;
                }
                if(index == 1)
                    if(card.getRank() != hand.get(0).getRank())
                    {
                    notMatched.push(index);
                    index++;
                    continue;
                    }
                    else
                    {
                        notMatched.push(index + 1);
                        index++;
                        continue;
                    }
                if((index > 1 &&  index < hand.size() - 2 && hand.get(index + 1).getRank() != hand.get(index + 2).getRank())
                        || (index >= hand.size() - 2 && index < hand.size()))
                {
                    if(card.getRank() == hand.get(index - 1).getRank())
                    {

                        notMatched.push(index + 1);
                        skipCard = true;
                    }
                    else {
                        notMatched.push(index);
                        skipCard = true;
                    }
                }

            }


            index++;
        }

        if(notMatched.size() != 0)
        {
            //since after popping element from the stack size will change
            //therefore it needs to be store before index values are popped
            int size = notMatched.size();
            for(int i = 0; i < size; i++)
            {
                discard(discardDeck,deckPile, notMatched.pop());
            }
        }
    }

    private boolean possibleFlush(CardDeck discardDeck, CardDeck deckPile)
    {
        int diffSuiteIndex = -1;
        boolean skipCard = false;
        int index = 0;
        for(Card card : hand)
        {
            //boundary check
            if(index == hand.size() - 1)
                break;
            if(skipCard)
            {
                skipCard = false;
                index++;
                continue;
            }
            if(card.getColor() != hand.get(index + 1).getColor())
            {
                if(diffSuiteIndex != -1)
                    return false;
                else
                {
                    if(index < 3)
                    {
                        if(card.getColor() == hand.get(index + 2).getColor() )
                            diffSuiteIndex = index + 1;
                        else
                            diffSuiteIndex = index;
                        skipCard = true;
                    }
                    else
                        diffSuiteIndex = index + 1;
                }
            }
            index++;
        }

        discard(discardDeck,deckPile,diffSuiteIndex);
        return true;
    }

    private boolean possibleStraight (CardDeck discardDeck, CardDeck deckPile)
    {
        int outOfSequenceIndex = -1;
        boolean skipCard = false;
        int index = 0;
        for(Card card : hand)
        {
            //boundary check
            if(index == hand.size() - 1)
                break;
            if(skipCard)
                continue;
            //Exception check for 5 4 3 2 A sequence and skip the card due to sort by value
            if(card.getRank() == ACE && hand.get(hand.size() - 1).getRank() == '2'){
                index++;
                continue;
            }
            //check cards by sequence
            if(card.getValue() - 1 != hand.get(index + 1).getValue())
            {
                if(outOfSequenceIndex == -1)
                {
                    if(index == hand.size() - 2)
                        outOfSequenceIndex = index + 1;
                    else if(card.getValue() - 2 != hand.get(index + 2).getValue())
                        outOfSequenceIndex = index;

                    else
                        outOfSequenceIndex = index + 1;
                }
                else
                    return false;
            }

            index++;
        }
        if(outOfSequenceIndex != -1)
        {
            discard(discardDeck,deckPile,outOfSequenceIndex);
            return true;
        }
        return false;
    }

    public int getCardsDiscarded() { return cardsDiscarded; }

    public void makeMove(CardDeck discardDeck, CardDeck deckPile)
    {
        evaluator.evaluate(this.hand);
        int value = evaluator.getEvaluateValue();
        //if player has combinations already it won't make any move
        //7 - Full house, 6 Flush, 5 Straight, 9 Straight Flush
        switch(value)
        {
            case 9: return;
            case 8: discardNonMatching(discardDeck,deckPile); return; //Four of the mind
            case 7:
            case 6:
            case 5: return;
            case 4: discardNonMatching(discardDeck,deckPile); return; //Three of the mind
            case 3: discardNonMatching(discardDeck,deckPile); return; //Two pair
            case 2: discardNonMatching(discardDeck,deckPile); return; //One pair
            default: break;

        }

        //If hand is a high card check other possibilities
        if(possibleFlush(discardDeck,deckPile)) {return;}
        else if(possibleStraight(discardDeck,deckPile)){return;}
        else discardAtWill(discardDeck,deckPile);
    }

}
