package project;

/**
 * this is Ai player class
 * it use a simple algorithm to compute best coordinate
 * it is inherited Player class
 */

public class AiPlayer extends Player {
    /**
     * constructor for ai player class with refers to player class
     * @param color ai player color
     * @param gameBoard game game board
     */
    public AiPlayer(int color, Map gameBoard) {
        super(color, gameBoard);
    }

    /**
     * it is an Override of putNut method in player class
     * in this class computer enter the input instead of user input
     */
    @Override
    public void putNut() {
        System.out.println("computer turn");
        System.out.println("please enter the coordinate");
        String coordinate = coordinateFind();
        System.out.println(coordinate);
        try {
            int x = value(coordinate.charAt(0));
            int y = value(coordinate.toUpperCase().charAt(1));
            //scanner.close();
            if (!gameBoard.putNut(color, x, y))
                this.putNut();
        } catch (StringIndexOutOfBoundsException e) {
            System.out.println("invalid input!!!");
            this.putNut();
        }
    }

    /**
     * this method compute the computer out put coordinate
     * it use nut count method for each valid coordinate and select the best coordinate
     * @return out put coordinate in format "%d %c"
     */
    public String coordinateFind() {
        Nut[][] map = gameBoard.getMap();
        int max = 0;
        String coordinate = "";
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                if (map[i][j].getColor() == -1 && hasNutAround(i, j)) {
                    int count = nutCount(i, j);
                    if (count >= max) {
                        max = count;
                        coordinate = String.valueOf((char) (i + 48)) + String.valueOf((char) (j + 64));
                    }
                }
            }
        }
        return coordinate;

    }

    /**
     * the method calculate count of opNut around the given coordinate
     * @param x coordinate x
     * @param y coordinate y
     * @return number of opNut
     */
    private int nutCount(int x, int y) {
        int[][] direction = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
        Nut[][] map = gameBoard.getMap();
        int counter = 0;
        int primeColor = PrimeColor(color);
        for (int i = 0; i < 8; i++) {
            boolean change = false;
            int currentX = x + direction[i][0];
            int currentY = y + direction[i][1];
            while (map[currentX][currentY].getColor() == primeColor && currentX > 0 && currentY > 0 && currentX < 9 && currentY < 9) {
                currentX += direction[i][0];
                currentY += direction[i][1];
                change = true;
            }
            if (map[currentX][currentY].getColor() == color && change) {
                if (direction[i][0] != 0)
                    counter += Math.abs(currentX - x)-1;
                else
                    counter += Math.abs(currentY - y)-1;
            }
        }
        return counter;
    }

    /**
     * this method return opNut color
     * @param color new nut color
     * @return opNut color
     */
    private int PrimeColor(int color) {
        if (color == 1)
            return 0;
        else
            return 1;
    }
}
