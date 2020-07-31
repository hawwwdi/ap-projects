package project;

/**
 * Map class
 * any object of this class is a game board
 */
public class Map {
    private Nut[][] map;
    private int[] nutCount;

    /**
     * constructor for map class
     * it is initialize the map blocks
     */
    public Map() {
        map = new Nut[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                map[i][j] = new Nut(-1);
            }
        }
        map[4][4] = new Nut(0);
        map[5][5] = new Nut(0);
        map[4][5] = new Nut(1);
        map[5][4] = new Nut(1);
        nutCount = new int[2];
        nutCount[0] = 2;
        nutCount[1] = 2;
    }

    /**
     * this method check that player can put a nut at (x, y) coordinate or else
     * @param nutColor payer nut color
     * @param x x coordinate
     * @param y y coordinate
     * @return if can put at (x, y) return true else return false
     */
    public boolean putNut(int nutColor, int x, int y) {
        if (isValid(nutColor, x, y, true)) {
            System.out.println("put successful");
            return true;
        } else {
            System.out.printf("map[%d][%c] is invalid\n", x, y+64);
            return false;
        }
    }

    /**
     * the method check the give coordinate to put a new nut with given color
     * @param color nut to put color
     * @param x coordinate x
     * @param y coordinate y
     * @param edit if it is true map will update and change  the nuts else just check the given coordinate for putting a nit with given color
     * @return if we can put nut at given coordinate this method return true else return false
     */
    public boolean isValid(int color, int x, int y, boolean edit) {
        //they are directions to be checked
        int[][] direction = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
        if (map[x][y].getColor() != -1) {
            System.out.println("block is full");
            return false;
        }
        boolean valid = false;
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
                valid = true;
                if (edit)
                    updateMap(color, x, y, direction[i][0], direction[i][1]);
            }
        }
        if (edit && valid)
            nutCount[color]++;
        return valid;
    }

    /**
     * it is changed the map
     * start from given coordinate and change map color to the given color in the given direction until block colors is different from given color
     * @param color color of the new nut
     * @param x coordinate x
     * @param y coordinate y
     * @param signX direction x
     * @param signY direction y
     */
    private void updateMap(int color, int x, int y, int signX, int signY) {
        int primeColor = PrimeColor(color);
        do {
            map[x][y].setColor(color);
            x += signX;
            y += signY;
            nutCount[color]++;
            nutCount[primeColor]--;
        } while (map[x][y].getColor() == primeColor);
        nutCount[primeColor]++;
        nutCount[color]--;
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

    /**
     * this method use to print game board
     * it use uni code character
     * 0 : white circle
     * 1: black circle
     * -1: blank blocks
     */
    public void print() {
        System.out.println("white: " + nutCount[0] + " || black: " + nutCount[1]);
        System.out.println("    A    B     C    D    E     F    G    H ");
        for (int i = 1; i < 9; i++) {
            System.out.printf(" %d  ", i);
            for (int j = 1; j < 9; j++) {
                System.out.printf("%s", map[i][j].toString());
            }
            System.out.println("\n");
        }
        resetMap();
    }

    /**
     * this method reset all valid blocks for putting nuts
     */
    private void resetMap() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                map[i][j].setValidLocate(false);
            }
        }
    }

    /**
     * this method determine the game winner
     */
    public void showResult() {
        if (nutCount[0] > nutCount[1])
            System.out.println("white win");
        else
            System.out.println("black win!!");
    }

    /**
     * this is a getter method for map array
     * @return map array
     */
    public Nut[][] getMap() {
        return map;
    }
}
