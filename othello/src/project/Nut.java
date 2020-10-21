package project;

/**
 * this is Nut class
 */
public class Nut {
    //0 -> white 1->black
    int color;
    boolean validLocate;

    public Nut(int color) {
        this.color = color;
        validLocate = false;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setValidLocate(boolean validLocate) {
        this.validLocate = validLocate;
    }

    @Override
    public String toString() {
        if (color == 0)
            return String.valueOf('\u26AB') + "    ";
        else if (color == 1)
            return String.valueOf('\u26AA') + "    ";
        else if (validLocate)
            return "\u001B[33m" + String.valueOf('\u22C5') + "     " + "\u001B[0m";
        else
            return String.valueOf('\u22C5') + "     ";
    }
}
