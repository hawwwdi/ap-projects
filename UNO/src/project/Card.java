package project;

public interface Card {
    Color getColor();

    int getPoints();

    Boolean canPutOn(Card CardToPut);

    String[] toShape();
}
