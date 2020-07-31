package project;
/**
 * the computer player class
 * it is inheritance from player class
 * its used to creating computer player for uno game
 */
public class ComputerPlayer extends Player {
    /**
     * constructor for user player class
     * its used to create new object of this class
     * @param name user name
     * @param gameBoard the game in which computer plays
     */
    public ComputerPlayer(String name, GameSystem gameBoard) {
        super(name, gameBoard);
    }
    /**
     * this is an Override of putNonDrawCard
     * its used when last card isn't draw or wild draw card
     * @param lastCard object of last card in game
     * @return new card which computer selected
     */
    @Override
    protected Card putNonDrawCard(Card lastCard) {
        Card toPut = null;
        if (!canPutWildDrawCard(lastCard)) {
            int cardIndex = -1;
            System.out.println(Color.changeColor(Color.WHITE) + "select a card!");
            do {
                toPut = cards.get(++cardIndex);
            } while (!lastCard.canPutOn(toPut) || toPut instanceof WildDrawCard);
            System.out.println((cardIndex + 1));
        } else {
            toPut = putSpecificType(WildDrawCard.class, "you have to put Wild Draw Card");
        }
        removeCard(toPut);
        return toPut;
    }
    /**
     * it used when user most put specific card type such as wild or wild draw card
     * @param t specific cart type class
     * @param message message to show user
     * @return selected card
     */
    @Override
    protected Card putSpecificType(Class<?> t, String message) {
        Card toPut;
        int cardIndex = -1;
        System.out.println(Color.changeColor(Color.WHITE) + message);
        do {
            toPut = cards.get(++cardIndex);
        } while (toPut.getClass() != t);
        System.out.println((cardIndex + 1));
        removeCard(toPut);
        return toPut;
    }

}
