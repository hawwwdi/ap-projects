package project;

import java.util.Scanner;

/**
 * the player class
 */
public class Player {
    protected MapManager gameBoard;
    protected final int color;

    public Player(MapManager gameBoard, int color) {
        this.gameBoard = gameBoard;
        this.color = color;
    }

    public void turn() {
        showTurn();
        Scanner input = new Scanner(System.in);
        System.out.println("enter coordinate");
        int x = input.nextInt() - 1;
        int y = input.nextInt() - 1;
        boolean rotate = false;
        if (gameBoard.isEmpty(x, y) && checkValidIndex(x) && checkValidIndex(y)) {
            gameBoard.putNut(color, x, y);
            gameBoard.printMap();
            if (gameBoard.endGame())
                System.exit(color);
            rotate = true;
        } else {
            System.out.println("invalid block");
            this.turn();
        }
        if (rotate) {
            System.out.println("select a map");
            int selectedMap = input.nextInt();
            System.out.println("select direction\n1)90\n2)-90");
            int direction = input.nextInt();
            gameBoard.rotateMap(selectedMap, direction);
            gameBoard.printMap();
            if (gameBoard.endGame())
                System.exit(color);
        }
    }

    protected boolean checkValidIndex(int index) {
        return index >= 0 && index < 6;
    }

    protected void showTurn() {
        if (color == 0)
            System.out.println("RED TURN");
        else
            System.out.println("BLUE TURN");
    }

}
