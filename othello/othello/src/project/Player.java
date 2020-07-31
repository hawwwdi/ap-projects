package project;

import java.util.Scanner;

/**
 * the player class
 * it use to save player details
 * color = -1 for blank nuts, 0 for white and 1 for black nuts
 */
public class Player {
    protected int color;
    protected Map gameBoard;

    /**
     * constructor for player class
     * it use to create a new abject of this class
     * @param color new player color
     * @param gameBoard new player game board
     */
    public Player(int color, Map gameBoard) {
        this.color = color;
        this.gameBoard = gameBoard;
    }

    /**
     *it use to put player nuts on the map
     * user input the coordinate in this method
     */
    public void putNut() {
        Scanner scanner = new Scanner(System.in);
        if (color == 0)
            System.out.println("white turn");
        else
            System.out.println("black turn");
        System.out.println("please enter the coordinate");
        try {
            String coordinate = scanner.nextLine();
            int x = value(coordinate.charAt(0));
            int y = value(coordinate.toUpperCase().charAt(2));
            //if selected coordinate is invalid or full
            if (!gameBoard.putNut(color, x, y))
                this.putNut();
            //if invalid input given
        } catch (StringIndexOutOfBoundsException e) {
            System.out.println("invalid input!!!");
            this.putNut();
        }
    }

    /**
     * this method check map blocks and find valid blocks for put nuts
     * @return  if any valid block not found this method return false else return true
     */
    public boolean hasTurn() {
        Nut[][] map = gameBoard.getMap();
        boolean flag = false;
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                if (map[i][j].getColor() == -1 && hasNutAround(i, j))
                    if (gameBoard.isValid(color, i, j, false)) {
                        map[i][j].setValidLocate(true);
                        flag = true;
                    }
            }
        }
        if (!flag)
            System.out.println("pass");
        return flag;

    }

    /**
     * this method check a specific coordinate with given x and y
     * @param x given x
     * @param y given y
     * @return if the coordinate has nut around return true else return false
     */
    protected boolean hasNutAround(int x, int y) {
        Nut[][] map = gameBoard.getMap();
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (map[x + i][y + j].getColor() != -1 && map[x + i][y + j].getColor() != color)
                    return true;
            }
        }
        return false;
    }

    /**
     * this method use to convert user input into the real coordinate
     * @param y specific character at use input string
     * @return int value of user input
     */
    protected int value(char y) {
        if (y > 64)
            return y - 64;
        else
            return y - 48;
    }

}
