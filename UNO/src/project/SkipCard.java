package project;

/**
 * the SkipCard class
 * it used to create new Skip card object
 */
public class SkipCard implements Card, Colorful {
    private final Color color;
    private final int points;
    private final char character;

    /**
     * constructor for this class
     * to crete new object of this
     *
     * @param color new card color
     */
    public SkipCard(Color color) {
        this.color = color;
        this.points = 20;
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
        if (CardToPut instanceof SkipCard)
            return true;
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
        shape[2] = Color.changeColor(color) + "|  SKIP |";
        shape[3] = Color.changeColor(color) + "|      " + character + "|";
        shape[4] = Color.changeColor(color) + "┕━━━━━━━┙";
        return shape;
    }
}
