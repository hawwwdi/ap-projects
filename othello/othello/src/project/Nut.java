package project;

/**
 * this is Nut class
 * game map is array of this class
 */
public class Nut {
    //0 -> white 1->black
    int color;
    boolean validLocate;

    /**
     * constructor for nut class
     * it use to create new object if this class
     *
     * @param color new nut color 1: black | 0 : white
     */
    public Nut(int color) {
        this.color = color;
        validLocate = false;
    }

    /**
     * getter for nut color
     *
     * @return nut color
     */
    public int getColor() {
        return color;
    }

    /**
     * setter for nut color
     *
     * @param color new color
     */
    public void setColor(int color) {
        this.color = color;
    }

    /**
     * setter for valid locate
     * if we can put nut at this blank nut block the valid locate boolean change to true
     *
     * @param validLocate new value of this nut
     */
    public void setValidLocate(boolean validLocate) {
        this.validLocate = validLocate;
    }

    /**
     * this is an Override of toString method
     * color=0 : return white circle
     * color=1 : return black circle
     * color=-1 : return a point
     *
     * @return Nut character
     */
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
