package project;

/**
 * The reverse card class
 */
public class ReverseCard implements Card, Colorful {
    private final Color color;
    private final int points;
    private boolean isUsed;
    private final char character;

    public ReverseCard(Color color) {
        this.color = color;
        this.points = 20;
        this.isUsed = false;
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

    public boolean isUsed() {
        return isUsed;
    }

    public void resetCard() {
        isUsed = !isUsed;
    }

    @Override
    public Boolean canPutOn(Card CardToPut) {
        if (CardToPut instanceof ReverseCard)
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
        shape[2] = Color.changeColor(color) + "|REVERSE|";
        shape[3] = Color.changeColor(color) + "|      " + character + "|";
        shape[4] = Color.changeColor(color) + "┕━━━━━━━┙";
        return shape;
    }
}
