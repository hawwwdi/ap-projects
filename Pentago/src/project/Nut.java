package project;

/**
 * the nut class
 */
public class Nut {
    private int color;

    public Nut(int color) {
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public boolean isEmpty() {
        return color == -1;
    }

    @Override
    public String toString() {
        if (color == 0)
            return "\u001B[31m" + String.valueOf('\u26AB') + "    " + "\u001B[0m";
        else if (color == 1)
            return "\u001B[34m" + String.valueOf('\u26AB') + "    " + "\u001B[0m";
        else
            return String.valueOf('\u22C5') + "     ";
    }
}
