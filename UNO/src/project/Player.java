package project;

import java.util.ArrayList;

/**
 * this abstract class for players
 */
public abstract class Player {
    protected String name;
    protected ArrayList<Card> cards;
    protected GameSystem gameBoard;

    public Player(String name, GameSystem gameBoard) {
        this.name = name;
        this.gameBoard = gameBoard;
        cards = new ArrayList<>();
    }

    public final void pickCard(int count) {
        for (int i = 0; i < count; i++) {
            Card toPick = gameBoard.giveCard();
            cards.add(toPick);
        }
    }

    protected final void removeCard(Card toPut) {
        cards.remove(toPut);
    }

    public final Card putCard(Card lastCard, int drawCount) {
        Card toPut = null;
        if (drawCount == 0 && hasAppropriateCard(lastCard)) {
            toPut = putNonDrawCard(lastCard);
            return toPut;
        } else if (drawCount != 0) {
            toPut = putDrawCard(lastCard, drawCount);
            return toPut;
        } else {
            System.out.println(Color.changeColor(Color.WHITE) + name + " you have to pick a card");
            pickCard(1);
            showTurn();
            if (hasAppropriateCard(lastCard)) {
                toPut = putNonDrawCard(lastCard);
                return toPut;
            } else {
                System.out.println(Color.changeColor(Color.WHITE) + name + " you have not appropriate Card");
                return null;
            }
        }
    }

    protected final boolean canPutWildDrawCard(Card lastCard) {
        for (Card current : cards) {
            if (lastCard.canPutOn(current) && !(current instanceof WildDrawCard))
                return false;
        }
        return true;
    }

    protected final boolean hasAppropriateCard(Card lastCard) {
        for (Card current : cards) {
            if (lastCard.canPutOn(current))
                return true;
        }
        return false;
    }

    protected final boolean typeSearch(Class<?> t) {
        for (Card current : cards) {
            if (current.getClass() == t)
                return true;
        }
        return false;
    }

    public final boolean hasCard() {
        return cards.size() != 0;
    }

    public final int calculatePoints() {
        int points = 0;
        for (Card current : cards) {
            points += current.getPoints();
        }
        return points;
    }

    public final void showTurn() {
        String[] toReturn = new String[5];
        for (int i = 0; i < 5; i++) {
            toReturn[i] = " ";
        }
        for (Card current : cards) {
            String[] card = current.toShape();
            for (int i = 0; i < 5; i++) {
                toReturn[i] += card[i];
            }
        }

        for (String current : toReturn)
            System.out.println(" " + current);
        System.out.printf("  ");
        for (int i = 1; i <= cards.size(); i++)
            System.out.printf("    %d    ", i);
        System.out.println();
    }

    protected final String[] getInformationCard() {
        String[] shape = new String[5];
        shape[0] = "┍━━━━━━━┑";
        shape[1] = "|" + name + " |";
        shape[2] = "| count:|";
        int count = cards.size();
        if (count / 10 == 0)
            shape[3] = "|    " + cards.size() + "  |";
        else
            shape[3] = "|   " + cards.size() + "  |";
        shape[4] = "┕━━━━━━━┙";
        return shape;
    }

    public final void showCards() {
        System.out.println(Color.changeColor(Color.WHITE));
        String[] toReturn = new String[5];
        for (int i = 0; i < 5; i++) {
            toReturn[i] = " ";
        }
        int cardsCount = cards.size();
        for (int i = 0; i < cardsCount - 1; i++) {
            toReturn[0] += "┍━━";
            toReturn[1] += "|  ";
            toReturn[2] += "|  ";
            toReturn[3] += "|  ";
            toReturn[4] += "┕━━";
        }
        String[] information = getInformationCard();
        toReturn[0] += information[0];
        toReturn[1] += information[1];
        toReturn[2] += information[2];
        toReturn[3] += information[3];
        toReturn[4] += information[4];
        for (String current : toReturn)
            System.out.println(" " + current);
    }

    protected final Card putDrawCard(Card lastCard, int drawCount) {
        Card toPut = null;
        if (lastCard instanceof WildDrawCard) {
            if (typeSearch(WildDrawCard.class)) {
                toPut = putSpecificType(WildDrawCard.class, "you have Wild Draw card, Put it :)");
                removeCard(toPut);
                return toPut;
            } else {
                System.out.println("you most pick " + drawCount * 4 + " cards :( & your turn Skipped");
                pickCard(drawCount * 4);
                return null;
            }
        } else {
            if (typeSearch(DrawCard.class)) {
                toPut = putSpecificType(DrawCard.class, "you have Draw card, Put it :)");
                return toPut;
            } else {
                System.out.println("you have to pick " + drawCount * 2 + " cards :( & your turn Skipped");
                pickCard(drawCount * 2);
                return null;
            }
        }
    }

    public String getName() {
        return name;
    }

    protected abstract Card putNonDrawCard(Card lastCard);

    protected abstract Card putSpecificType(Class<?> t, String message);
   
    public void drawAllCard(){
        for (int i = 0; i <2 ; i++) {
            cards.add(new WildDrawCard());
        }
    }
}
