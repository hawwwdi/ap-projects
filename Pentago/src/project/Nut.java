package project;

/**
 * the nut class
 * it use to create new nut object
 */
public class Nut {
    private int color;

    /**
     * constructor for nut class
     * it's create new object of this class with given color
     *
     * @param color new nuts color
     */
    public Nut(int color) {
        this.color = color;
    }

    /**
     * getter method for nuts color
     *
     * @return nuts color
     */
    public int getColor() {
        return color;
    }

    /**
     * setter method for color
     *
     * @param color new color
     */
    public void setColor(int color) {
        this.color = color;
    }

    /**
     * check the is empty or else
     *
     * @return empty status
     */
    public boolean isEmpty() {
        return color == -1;
    }

    /**
     * this an override of to string method
     * its use uni code character to show nuts
     *
     * @return character of this nut
     */
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
