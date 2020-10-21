package project;

import java.util.Scanner;
import java.util.regex.Pattern;

public class Player {
    protected int color;
    protected Map gameBoard;

    public Player(int color, Map gameBoard) {
        this.color = color;
        this.gameBoard = gameBoard;
    }

    public void putNut() {
        Scanner scanner = new Scanner(System.in);
        if (color == 0)
            System.out.println("white turn");
        else
            System.out.println("black turn");
        try {
            String in;
            do {
                System.out.println("please enter the coordinate");
                in= scanner.nextLine();
            }while (!Pattern.matches("[1-8].[A-Ha-h]", in));
            int x = value(in.charAt(0));
            int y = value(in.toUpperCase().charAt(2));
            //if selected coordinate is invalid or full
            if (!gameBoard.putNut(color, x, y))
                this.putNut();
            //if invalid input given
        } catch (StringIndexOutOfBoundsException e) {
            System.out.println("invalid input!!!");
            this.putNut();
        }
    }

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

    protected int value(char y) {
        if (y > 64)
            return y - 64;
        else
            return y - 48;
    }

}
