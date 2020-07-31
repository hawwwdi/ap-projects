package project;

import java.util.Scanner;

/**
 * the player class
 * we can create new player via create object of this class
 */
public class Player {
    protected MapManager gameBoard;
    protected final int color;

    /**
     * constructor for player class
     * it use to create new player object
     *
     * @param gameBoard player game
     * @param color     player colors
     */
    public Player(MapManager gameBoard, int color) {
        this.gameBoard = gameBoard;
        this.color = color;
    }

    /**
     * the turn method
     * its call in the gameBoard object when it is the player's turn
     */
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

    /**
     * the method check the input number is valid index if game map or else
     *
     * @param index input index
     * @return if it is valid true else false
     */
    protected boolean checkValidIndex(int index) {
        return index >= 0 && index < 6;
    }

    /**
     * the method print players turn each time
     */
    protected void showTurn() {
        if (color == 0)
            System.out.println("RED TURN");
        else
            System.out.println("BLUE TURN");
    }

}
