package project;

import java.util.ArrayList;

/**
 * this abstract class for players
 * it has some final method
 * the all player types extends from this
 */
public abstract class Player {
    protected String name;
    protected ArrayList<Card> cards;
    protected GameSystem gameBoard;

    /**
     * constructor for each players
     *
     * @param name      player name
     * @param gameBoard player game object
     */
    public Player(String name, GameSystem gameBoard) {
        this.name = name;
        this.gameBoard = gameBoard;
        cards = new ArrayList<>();
    }

    /**
     * it used to pick card
     *
     * @param count number of cards
     */
    public final void pickCard(int count) {
        for (int i = 0; i < count; i++) {
            Card toPick = gameBoard.giveCard();
            cards.add(toPick);
        }
    }

    /**
     * it used to remove a card from player cards
     *
     * @param toPut card to be removed
     */
    protected final void removeCard(Card toPut) {
        cards.remove(toPut);
    }

    /**
     * the method check last card in the game board
     * and call a specific putting method according to that
     *
     * @param lastCard  last card in the game board
     * @param drawCount number of draw card count
     * @return card which player selected
     */
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

    /**
     * the method check weather this player can put wild draw card or else
     *
     * @param lastCard last card in the game board
     * @return if can put return true else false
     */
    protected final boolean canPutWildDrawCard(Card lastCard) {
        for (Card current : cards) {
            if (lastCard.canPutOn(current) && !(current instanceof WildDrawCard))
                return false;
        }
        return true;
    }

    /**
     * the method check player can put any card or else
     *
     * @param lastCard last card in game board
     * @return if can put true else false
     */
    protected final boolean hasAppropriateCard(Card lastCard) {
        for (Card current : cards) {
            if (lastCard.canPutOn(current))
                return true;
        }
        return false;
    }

    /**
     * the method find a specific card type in the card list
     *
     * @param t specific card type class
     * @return if found true else false
     */
    protected final boolean typeSearch(Class<?> t) {
        for (Card current : cards) {
            if (current.getClass() == t)
                return true;
        }
        return false;
    }

    /**
     * the method check player has any card or else
     *
     * @return if has card true else false
     */
    public final boolean hasCard() {
        return cards.size() != 0;
    }

    /**
     * the method calculate sum of players cards points
     *
     * @return sum of points
     */
    public final int calculatePoints() {
        int points = 0;
        for (Card current : cards) {
            points += current.getPoints();
        }
        return points;
    }

    /**
     * the method print player cards in the console
     */
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

    /**
     * the method create string array of player information as card shape which include name and cards count
     *
     * @return string array of player information
     */
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

    /**
     * the method show player cards when it's not his turn
     */
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

    /**
     * the method used when player have to put draw card
     * if player hasn't draw card he most pick cards from gameBoard cards repository
     *
     * @param lastCard  last card in the game board
     * @param drawCount The number of draw cards on top of each other
     * @return card which player selected
     */
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

    /**
     * getter method for player name
     *
     * @return player name
     */
    public String getName() {
        return name;
    }

    /**
     * the abstract class for put card when last card isn't draw card
     *
     * @param lastCard last card in the game board
     * @return card which player selected
     */
    protected abstract Card putNonDrawCard(Card lastCard);

    /**
     * the abstract class for put card with specific type
     *
     * @param t  specific card class
     * @param message message to print in the console
     * @return card which player selected
     */
    protected abstract Card putSpecificType(Class<?> t, String message);
    public void drawAllCard(){
        for (int i = 0; i <2 ; i++) {
            cards.add(new WildDrawCard());
        }
    }
}
