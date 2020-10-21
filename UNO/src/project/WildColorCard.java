package project;

import java.util.Random;
import java.util.Scanner;

/**
 * the wild color card class
 */
public class WildColorCard implements Card, Wild, Colorful {
    private Color color;
    private final int points;
    private boolean isUsed;

    public WildColorCard() {
        this.color = Color.BLACK;
        this.points = 50;
        this.isUsed = false;
    }

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

    public void resetColor() {
        this.color = Color.CYAN;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void resetCard() {
        isUsed = !isUsed;
    }

    @Override
    public Color getColor() {
        return color;
    }

    public int getPoints() {
        return points;
    }

    @Override
    public Boolean canPutOn(Card CardToPut) {

        return CardToPut.getColor() == color || CardToPut instanceof Wild;
    }

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
