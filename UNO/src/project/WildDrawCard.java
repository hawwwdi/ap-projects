package project;

import java.util.Random;
import java.util.Scanner;

/**
 * this is wildDrawCard class
 */
public class WildDrawCard implements Card, Wild {
    private Color color;
    private final int points;

    public WildDrawCard() {
        this.color = Color.BLACK;
        this.points = 50;
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

    @Override
    public Color getColor() {
        return color;
    }

    public int getPoints() {
        return points;
    }

    @Override
    public Boolean canPutOn(Card CardToPut) {
        return CardToPut.getColor() == this.color || CardToPut instanceof Wild;
    }
 
    public void resetColor() {
        this.color = Color.BLACK;
    }
 
    @Override
    public String[] toShape() {
        String[] shape = new String[5];
        shape[0] = Color.changeColor(color) + "┍━━━━━━━┑";
        shape[1] = Color.changeColor(color) + "| WILD  |";
        shape[2] = Color.changeColor(color) + "|       |";
        shape[3] = Color.changeColor(color) + "| DRAW  |";
        shape[4] = Color.changeColor(color) + "┕━━━━━━━┙";
        return shape;
    }
}
