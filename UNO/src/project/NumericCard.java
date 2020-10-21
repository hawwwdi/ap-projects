package project;

/**
 * the numeric card class
 */
public class NumericCard implements Card, Colorful {
    private final Color color;
    private final int number;
    private final int points;
    private final char character;

    public NumericCard(Color color, int number) {
        this.color = color;
        this.number = number;
        this.points = number;
        character = Color.getChar(color);
    }

    @Override
    public Color getColor() {
        return color;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public int getPoints() {
        return points;
    }

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
