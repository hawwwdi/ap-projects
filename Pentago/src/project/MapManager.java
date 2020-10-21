package project;

/**
 * the map manger class
 */
public class MapManager {
    private Map[][] map;
    private Nut[][] visualMap;
    private final int SIZE = 2;
    private final int VisualSIZE = 3;

    public MapManager() {
        map = new Map[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                map[i][j] = new Map();
            }
        }
        visualMap = new Nut[6][6];
        updateVisualMap();
    }

    public void putNut(int color, int x, int y) {
        Map selectedMap = findMap(x, y);
        selectedMap.putNut(color, x % 3, y % 3);
    }

    public boolean isEmpty(int x, int y) {
        Map selectedMap = findMap(x, y);
        return selectedMap.isEmpty(x % 3, y % 3);
    }

    public void rotateMap(int mapNum, int direction) {
        if (mapNum == 1)
            map[0][0].rotate(direction);
        else if (mapNum == 2)
            map[0][1].rotate(direction);
        else if (mapNum == 3)
            map[1][0].rotate(direction);
        else
            map[1][1].rotate(direction);
        updateVisualMap();
    }

    public void printMap() {
        System.out.println("   1     2    3         4     5    6 ");
        for (int i = 0; i < 2 * VisualSIZE; i++) {
            if (i == 3)
                System.out.println("  --    --    --        --    --   -- ");
            System.out.printf("%d  ", i + 1);
            for (int j = 0; j < 2 * VisualSIZE; j++) {
                if (j == 3)
                    System.out.print("|    ");
                System.out.printf("%s", visualMap[i][j].toString());
            }
            if (i == 2)
                System.out.println();
            else
                System.out.println("\n");
        }
    }

    private Map findMap(int x, int y) {
        if (x < 3 && y < 3)
            return map[0][0];
        else if (x < 3)
            return map[0][1];
        else if (y < 3)
            return map[1][0];
        else
            return map[1][1];
    }

    private void updateVisualMap() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                Nut[][] tmp = map[i][j].getMap();
                for (int k = 0; k < VisualSIZE; k++) {
                    System.arraycopy(tmp[k], 0, visualMap[(i * 3) + k], (j * 3), VisualSIZE);
                }
            }
        }
    }

    public boolean endGame() {
        boolean red = checkResult(0);
        boolean black = checkResult(1);
        if (red && black) {
            System.out.println("DRAW!!!");
            return true;
        } else if (red) {
            System.out.println("Red WIN!!!");
            return true;
        } else if (black) {
            System.out.println("Black WIN!!!");
            return true;
        } else
            return false;
    }

    private boolean checkResult(int color) {
        for (int i = 0; i < 2 * VisualSIZE; i++) {
            for (int j = 0; j < 2; j++) {
                int currentY = j;
                while (visualMap[i][currentY].getColor() == color) {
                    currentY++;
                    if (!checkValidIndex(currentY))
                        break;
                }
                if (currentY - j == 5)
                    return true;
            }
        }
        for (int i = 0; i < (2 * VisualSIZE); i++) {
            for (int j = 0; j < 2; j++) {
                int currentX = j;
                while (visualMap[currentX][i].getColor() == color) {
                    currentX++;
                    if (!checkValidIndex(currentX))
                        break;
                }
                if (currentX - j == 5)
                    return true;
            }
        }
        int[][] coordinates = {{0, 0}, {0, 1}, {1, 0}, {1, 1}, {5, 0}, {5, 1}, {4, 0}, {4, 1}};
        int[][] direction = {{1, 1}, {-1, 1}};
        for (int j = 0; j < 8; j++) {
            int currentX = coordinates[j][0];
            int currentY = coordinates[j][1];
            while (visualMap[currentX][currentY].getColor() == color) {
                currentX += direction[j / 4][0];
                currentY += direction[j / 4][1];
                if (!(checkValidIndex(currentX) && checkValidIndex(currentY)))
                    break;
            }
            if (currentY - coordinates[j][1] == 5)
                return true;
        }
        return false;
    }
 
    private boolean checkValidIndex(int index) {
        return index >= 0 && index < 6;
    }

    public Map[][] getMap() {
        return map;
    }

    public Nut[][] getVisualMap() {
        return visualMap;
    }
}
