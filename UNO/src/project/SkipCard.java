package project;

/**
 * the SkipCard class
 */
public class SkipCard implements Card, Colorful {
    private final Color color;
    private final int points;
    private final char character;

    public SkipCard(Color color) {
        this.color = color;
        this.points = 20;
        character = Color.getChar(color);
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public int getPoints() {
        return points;
    }

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
