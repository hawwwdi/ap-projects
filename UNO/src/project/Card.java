package project;

/**
 * the interface use for create all cards in same format
 */
public interface Card {
    Color getColor();

    int getPoints();

    Boolean canPutOn(Card CardToPut);

    String[] toShape();
}
