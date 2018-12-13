/*
 * This class defines the card info based on: Rank, Color and Value
 * Author: Pawel Pietron
 *
 */


public class Card {
    private int value;
    private char rank;
    private char color;

    public Card(char rank, char color) {
        this.rank = rank;
        this.color = color;
        this.setValue(rank);
    }

    //Set card's value by it's rank
    public void setValue(char rank) {
        switch(rank){
            case '2':
                this.value = 2;
                break;
            case '3':
                this.value = 3;
                break;
            case '4':
                this.value = 4;
                break;
            case '5':
                this.value = 5;
                break;
            case '6':
                this.value = 6;
                break;
            case '7':
                this.value = 7;
                break;
            case '8':
                this.value = 8;
                break;
            case '9':
                this.value = 9;
                break;
            case 'T':
                this.value = 10;
                break;
            case 'J':
                this.value = 11;
                break;
            case 'Q':
                this.value = 12;
                break;
            case 'K':
                this.value = 13;
                break;
            case 'A':
                this.value = 14;
                break;
            default:
                System.err.println("Wrong rank has been passed");

        }
    }

    public void setRank(char rank) {
        this.rank = rank;
    }

    public void setColor(char color) {
        this.color = color;
    }

    public char getRank() {
        return rank;
    }

    public char getColor() {
        return color;
    }

    public int getValue() {
        return value;
    }
}
