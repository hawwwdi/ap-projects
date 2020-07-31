package project;

/**
 * the numeric card class
 * it use to create new numeric card object
 */
public class NumericCard implements Card, Colorful {
    private final Color color;
    private final int number;
    private final int points;
    private final char character;

    /**
     * constructor for numeric card
     * to create new object of this class
     *
     * @param color  new card color
     * @param number new card number
     */
    public NumericCard(Color color, int number) {
        this.color = color;
        this.number = number;
        this.points = number;
        character = Color.getChar(color);
    }

    /**
     * getter method for card color
     *
     * @return card color
     */
    @Override
    public Color getColor() {
        return color;
    }

    /**
     * getter method for card number
     *
     * @return card number
     */
    public int getNumber() {
        return number;
    }

    /**
     * getter method for card points
     *
     * @return card points
     */
    @Override
    public int getPoints() {
        return points;
    }

    /**
     * its shows that can we put new card on this card or else
     *
     * @param CardToPut new card we decide to put it
     * @return if we can put new card on this card true else false
     */
    @Override
    public Boolean canPutOn(Card CardToPut) {
        if (CardToPut instanceof NumericCard)
            return this.number == ((NumericCard) CardToPut).getNumber() || this.color == CardToPut.getColor();
        else if (CardToPut instanceof Wild)
            return true;
        else if (CardToPut instanceof Colorful)
            return this.color == CardToPut.getColor();
        else
            return true;
    }

    /**
     * its return card shape as String array
     *
     * @return String array of card shape
     */
    @Override
    public String[] toShape() {
        String[] shape = new String[5];
        shape[0] = Color.changeColor(color) + "┍━━━━━━━┑";
        shape[1] = Color.changeColor(color) + "|" + character + "      |";
        shape[2] = Color.changeColor(color) + "|   " + number + "   |";
        shape[3] = Color.changeColor(color) + "|      " + character + "|";
        shape[4] = Color.changeColor(color) + "┕━━━━━━━┙";
        return shape;
    }
}
