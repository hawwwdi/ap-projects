package project;

/**
 * the map class
 * game map created from 4 objects of this class
 */
public class Map {
    private Nut[][] map;
    private final int SIZE = 3;

    /**
     * constructor for map class
     * in this method we initialize map array
     */
    public Map() {
        map = new Nut[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                map[i][j] = new Nut(-1);
            }
        }
    }

    /**
     * this method used to put a nut with given color on the given coordinate
     * @param color nut color
     * @param x x coordinate
     * @param y y coordinate
     */
    public void putNut(int color, int x, int y) {
        map[x][y].setColor(color);
        System.out.println("putting successful");
    }

    /**
     * this method used to rotate this map in the given direction
     * @param direct rotate direction
     */
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

    /**
     * getter for map array
     * @return map array
     */
    public Nut[][] getMap() {
        return map;
    }

    /**
     * this method check the given coordinate state
     * @param x x coordinate
     * @param y y coordinate
     * @return if the given coordinate is empty true else false
     */
    public boolean isEmpty(int x, int y) {
        return map[x][y].isEmpty();
    }

}
