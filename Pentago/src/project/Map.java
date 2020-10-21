package project;

/**
 * the map class
 */
public class Map {
    private Nut[][] map;
    private final int SIZE = 3;

    public Map() {
        map = new Nut[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                map[i][j] = new Nut(-1);
            }
        }
    }

   public void putNut(int color, int x, int y) {
        map[x][y].setColor(color);
        System.out.println("putting successful");
    }

    public void rotate(int direct) {
        Nut[][] tmp = new Nut[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (direct == 1)
                    tmp[j][SIZE - i - 1] = map[i][j];
                else
                    tmp[SIZE - j - 1][i] = map[i][j];
            }

        }
        map = tmp;
    }

    public Nut[][] getMap() {
        return map;
    }

    public boolean isEmpty(int x, int y) {
        return map[x][y].isEmpty();
    }

}
