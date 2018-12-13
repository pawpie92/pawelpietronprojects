/*
 * 5 Draw Poker game app main loop class main
 * Author: Pawel Pietron
 *
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;


public class Main {

    public static void main(String[] args) {
    	//Create game objects
    	Evaluator dealer = new Evaluator();
		UserPlayer player = new UserPlayer();
		ArrayList<ComputerPlayer> cpuPlayers = new ArrayList<ComputerPlayer>();
		CardDeck deckPile = new CardDeck();
		CardDeck discardPile = new CardDeck();
		Scanner scan = new Scanner(System.in);
		Collections refree = null;

		dealer.welcomeMessage();
		//Starting the game event loop
		while(true)
		{
			deckPile.initializeDeck();
			System.out.println("Cards are being shuffled");
			System.out.println();
			deckPile.shuffleCards();
			System.out.println("How many players do want to play with?");
			int numberOfPlayers = scan.nextInt();

			//Adding Computer players
			for(int i = 0; i < numberOfPlayers; i++)
				cpuPlayers.add(new ComputerPlayer());

			//Dealing cards to the players
			for(int i = 0; i < 5; i++)
				player.takeCard(deckPile.dealCard());
			player.sortHand();

			//Each player take 5 cards
			for(ComputerPlayer cPlayer: cpuPlayers)
			{
				for(int i = 0; i < 5; i++)
					cPlayer.takeCard(deckPile.dealCard());
				cPlayer.sortHand();
			}

			//Printing user's cards
			System.out.print("The cards in your hands are:");
			player.printHand();

			//Check if player has an ace
			int inputs = 3;
			if(player.aceExist()){
				System.out.println("Since you got an ace in your hand you can discard 4 cards.");
				inputs = 4;
			}

			//Discarding cards part of the game
			System.out.println("List the cards you wish to discard(enter -1 to stop discarding): ");
			for (int i = 0; i < inputs; i++)
			{
				int discard = scan.nextInt();
				if(discard == -1)
					break;
				discardPile.addCard(player.getCardFromHand(discard - 1));
				player.discardCard(discard - 1);
				player.replaceCard(discard - 1, deckPile.dealCard());
			}

			player.sortHand();
			for(ComputerPlayer cPlayer: cpuPlayers){
				cPlayer.makeMove(discardPile,deckPile);
				cPlayer.sortHand();
				String discardNumber = String.format("Player %d discarded %d cards", cpuPlayers.indexOf(cPlayer) + 1, cPlayer.getCardsDiscarded());
				System.out.print(discardNumber);
				System.out.println();
			}

			//After replacing cards evaluate and print all player's cards
			System.out.print("The cards in your hands are:");

			player.printHand();
			dealer.evaluate(player.getHand());
			player.setEvaluateHandValue(dealer.getEvaluateValue());
			player.setHandScoreValue(dealer.giveScore(player.getHand()));

			System.out.println();
			System.out.print("You have got: ");
			System.out.print(dealer.getCombinations());
			System.out.println();

			//Cheking computer players card's and evaluating combinations
			for(ComputerPlayer cPlayer : cpuPlayers)
			{
				dealer.evaluate(cPlayer.getHand());
				cPlayer.setEvaluateHandValue(dealer.getEvaluateValue());
				cPlayer.setHandScoreValue(dealer.giveScore(cPlayer.getHand()));
				String playNum = String.format("Player %d has: ", cpuPlayers.indexOf(cPlayer) + 1);
				System.out.print(playNum);
				cPlayer.printHand();
				System.out.println();
			}

			ComputerPlayer cWinner = refree.max(cpuPlayers, new Comparator<ComputerPlayer>() {
				@Override
				public int compare(ComputerPlayer o1, ComputerPlayer o2) {
					//If evaluate scores will be the same find a hand that has bigger score
					if(o1.getHandEvaluateValue() == o2.getHandEvaluateValue())
						if(o1.getHandScoreValue() == o2.getHandScoreValue())
							return o1.getHand().get(1).getValue() < o2.getHand().get(1).getValue() ? -1 :
									(o1.getHand().get(1).getValue() > o2.getHand().get(1).getValue() ? 1 : 0);
						else
							return o1.getHandScoreValue() < o2.getHandScoreValue() ? -1 :
								(o1.getHandScoreValue() > o2.getHandScoreValue() ? 1 : 0);
					else
						return o1.getHandEvaluateValue() < o2.getHandEvaluateValue() ? -1 :
							(o1.getHandEvaluateValue() > o2.getHandEvaluateValue() ? 1 : 0);
				}
			});

			if(player.getHandEvaluateValue() > cWinner.getHandEvaluateValue() ||
					(player.getHandEvaluateValue() == cWinner.getHandEvaluateValue() && player.getHandScoreValue() > cWinner.getHandScoreValue()))
				System.out.println("***You won!!!***");
			else
			{
				String winner = String.format("***Computer Player %d has won!***", cpuPlayers.indexOf(cWinner) + 1);
				System.out.print(winner);
				System.out.println();
			}

			cpuPlayers.removeAll(cpuPlayers);
			player.giveCardsBack();
			System.out.println("Do you wish to continue?(Y/N)");
			System.out.println();
			char continueGame = scan.next().charAt(0);
			if(continueGame == 'n' || continueGame == 'N')
				break;

		}

		System.out.println("***Thank you for playing!!!***");
    }
}
