package project;

public class Map {
    private Nut[][] map;
    private int[] nutCount;

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

    public boolean putNut(int nutColor, int x, int y) {
        if (isValid(nutColor, x, y, true)) {
            System.out.println("put successful");
            return true;
        } else {
            System.out.printf("map[%d][%c] is invalid\n", x, y+64);
            return false;
        }
    }

    public boolean isValid(int color, int x, int y, boolean edit) {
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

    private int PrimeColor(int color) {
        if (color == 1)
            return 0;
        else
            return 1;
    }

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

    private void resetMap() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                map[i][j].setValidLocate(false);
            }
        }
    }

    public void showResult() {
        if (nutCount[0] > nutCount[1])
            System.out.println("white win");
        else
            System.out.println("black win!!");
    }

    public Nut[][] getMap() {
        return map;
    }
}
