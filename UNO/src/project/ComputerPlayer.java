package project;
/**
 * the computer player class
 */
public class ComputerPlayer extends Player {
 
    public ComputerPlayer(String name, GameSystem gameBoard) {
        super(name, gameBoard);
    }
 
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
