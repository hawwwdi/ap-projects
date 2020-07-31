package project;

import java.util.Random;
import java.util.Scanner;

/**
 * the wild color card class
 * it use to create whil color card objects
 */
public class WildColorCard implements Card, Wild, Colorful {
    private Color color;
    private final int points;
    private boolean isUsed;

    /**
     * constructor for this class
     * to create new object of this
     */
    public WildColorCard() {
        this.color = Color.BLACK;
        this.points = 50;
        this.isUsed = false;
    }

    /**
     * its use to select new color for game base
     *
     * @param isUser if is User we can select new color else new color select randomize
     */
    public void changeColor(boolean isUser) {
        System.out.println("please select color");
        System.out.println("1)RED 2)BLUE 3)YELLOW 4)GREEN");
        int select = 0;
        if (isUser) {
            Scanner in = new Scanner(System.in);
            select = in.nextInt();
        } else {
            Random random = new Random();
            select = random.nextInt(4) + 1;
            System.out.println(select);
        }
        switch (select) {
            case 1:
                this.color = Color.RED;
                break;
            case 2:
                this.color = Color.BLUE;
                break;
            case 3:
                this.color = Color.YELLOW;
                break;
            case 4:
                this.color = Color.GREEN;
                break;
            default:
                System.out.println("invalid select!");
                changeColor(isUser);
                break;
        }
    }

    /**
     * it use to reset the card color when its used
     */
    public void resetColor() {
        this.color = Color.CYAN;
    }

    /**
     * its shows whether this card has been used or else
     *
     * @return if card has been used true else false
     */
    public boolean isUsed() {
        return isUsed;
    }

    /**
     * its reset the card state
     */
    public void resetCard() {
        isUsed = !isUsed;
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

        return CardToPut.getColor() == color || CardToPut instanceof Wild;
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
        shape[1] = Color.changeColor(color) + "| WILD  |";
        shape[2] = Color.changeColor(color) + "|       |";
        shape[3] = Color.changeColor(color) + "| COLOR |";
        shape[4] = Color.changeColor(color) + "┕━━━━━━━┙";
        return shape;
    }
}
