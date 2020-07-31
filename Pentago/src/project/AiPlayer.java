package project;

import java.util.Random;

/**
 * the AiPlayer class
 * it is inheritance from player class
 * it use to create computer player object
 */
public class AiPlayer extends Player {
    /**
     * constructor for AiPlayer class
     * it used to create new object of this class
     *
     * @param gameBoard player game boards
     * @param color     player color
     */
    public AiPlayer(MapManager gameBoard, int color) {
        super(gameBoard, color);
    }

    /**
     * this is an override of turn method
     * it use findCoordinate method instead of user input
     */
    @Override
    public void turn() {
        System.out.printf("COMPUTER ");
        showTurn();
        System.out.println("enter coordinate");
        long time = System.currentTimeMillis();
        coordinate calculated = findCoordinate();
        int x = calculated.getX();
        int y = calculated.getY();
        if (calculated.isRandom())
            System.out.println("randomize coordinate:)");
        System.out.println((x + 1) + " " + (y + 1));
        System.out.println("took " + (long) (System.currentTimeMillis() - time) + "ms to calculate.");
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
            int selectedMap = calculated.getMapNum();
            System.out.println(selectedMap);
            System.out.println("select direction\n1)90\n2)-90");
            int direction = calculated.getRotate();
            System.out.println(direction);
            gameBoard.rotateMap(selectedMap, direction);
            gameBoard.printMap();
            if (gameBoard.endGame())
                System.exit(color);
        }
    }

    /**
     * the method use calculate coordinate method to find best coordinate to put nut
     *
     * @return the best coordinate which calculated by calculateCoordinate method
     */
    private coordinate findCoordinate() {
        coordinate bestCoordinate = new coordinate(0, 0);
        for (int i = 1; i < 5; i++) {
            for (int j = 1; j < 3; j++) {
                gameBoard.rotateMap(i, j);
                coordinate tmp = new coordinate(i, j);
                calculateCoordinate(tmp);
                if (tmp.getMax() >= bestCoordinate.getMax()) {
                    bestCoordinate = tmp;
                }
                gameBoard.rotateMap(i, j == 1 ? 2 : 1);
            }
        }
        return bestCoordinate;
    }

    /**
     * the method calculate max nuts around  given coordinate in 8 state
     * the method likes humans brain for the game Ai
     *
     * @param current given coordinate
     */
    private void calculateCoordinate(coordinate current) {
        Nut[][] visualMap = gameBoard.getVisualMap();
        int nutCounter = -1;
        for (int color = 0; color < 2; color++) {
            int opColor = color == 0 ? 1 : 0;
            for (int i = 0; i < 6; i++) {
                int Xcount = 0;
                int Ycount = 0;
                int y = -1;
                int x = -1;
                for (int j = 0; j < 6; j++) {
                    if (visualMap[i][j].getColor() == opColor)
                        Xcount++;
                    if (visualMap[i][j].getColor() == -1)
                        y = j;
                    if (visualMap[j][i].getColor() == opColor)
                        Ycount++;
                    if (visualMap[j][i].getColor() == -1)
                        x = j;
                }

                if (Xcount >= current.getMax() && x != -1 && y != -1 && Math.abs(current.getY() - 3) > Math.abs(y - 3)) {
                    current.setMax(Xcount);
                    current.setCoordinates(i, y);
                }
                if (Ycount >= current.getMax() && x != -1 && y != -1 && Math.abs(current.getX() - 3) > Math.abs(x - 3)) {
                    current.setMax(Ycount);
                    current.setCoordinates(x, i);
                }
            }

            int[][] coordinates = {{0, 0}, {0, 1}, {1, 0}, {5, 0}, {5, 1}, {4, 0}};
            int[][] direction = {{1, 1}, {-1, 1}};
            for (int j = 0; j < 6; j++) {
                int counter = 0;
                int currentX = coordinates[j][0];
                int currentY = coordinates[j][1];
                int x = -1;
                int y = -1;
                for (int i = 0; i < 6 && checkValidIndex(currentX) && checkValidIndex(currentY); i++) {

                    if (visualMap[currentX][currentY].getColor() == opColor)
                        counter++;
                    else if (visualMap[currentX][currentY].getColor() == -1) {
                        x = currentX;
                        y = currentY;
                    }
                    currentX += direction[j / 3][0];
                    currentY += direction[j / 3][1];
                }
                if (counter >= current.getMax() && x != -1 && y != -1 && (Math.abs(current.getY() - 3) > Math.abs(y - 3) || Math.abs(current.getX() - 3) > Math.abs(x - 3))) {
                    current.setMax(counter);
                    current.setCoordinates(x, y);
                }
                if (current.getMax() == 0) {
                    Random random = new Random();
                    int randX = 0;
                    int randY = 0;
                    do {
                        randX = random.nextInt(6);
                        randY = random.nextInt(6);
                    } while (visualMap[randX][randY].getColor() != -1);
                    current.setCoordinates(randX, randY);
                    current.setRotate(random.nextInt(2) + 1);
                    current.changeMapNum();
                }
            }
        }
    }

    /**
     * this is inner class for save the coordinate
     * its holds coordinate status and information
     * like rotate direction
     */
    private class coordinate {
        private int x;
        private int y;
        private int mapNum;
        private int rotate;
        private int max;
        private boolean random;

        /**
         * constructor to create new object of this class
         *
         * @param mapNum number of map pieces
         * @param rotate rotate direction
         */
        public coordinate(int mapNum, int rotate) {
            this.mapNum = mapNum;
            this.rotate = rotate;
            x = y = max = 0;
            random = false;
        }

        /**
         * the method convert the visual map coordinate to real piece of map coordinates
         *
         * @param x coordinate x
         * @param y coordinate y
         * @return real x
         */
        private int getRealX(int x, int y) {
            if (mapNum != findMap(x, y))
                return x % 3;
            else if (rotate == 1) {
                return 2 - y % 3;
            } else {
                return y % 3;
            }
        }

        /**
         * the method convert the visual map coordinate to real piece of map coordinates
         *
         * @param x coordinate x
         * @param y coordinate y
         * @return real y
         */
        private int getRealY(int x, int y) {

            if (mapNum != findMap(x, y))
                return y % 3;
            else if (rotate == 1) {
                return x % 3;
            } else {
                return 2 - x % 3;
            }
        }

        /**
         * the method give coordinate as visual map coordinate then convert it to the real coordinate and save it
         *
         * @param x x coordinate
         * @param y y coordinate
         */
        public void setCoordinates(int x, int y) {
            int map = findMap(x, y);
            int newX = getRealX(x, y);
            int newY = getRealY(x, y);
            if (map == 1) {
                this.x = newX;
                this.y = newY;
            } else if (map == 2) {
                this.x = newX;
                this.y = newY + 3;
            } else if (map == 3) {
                this.x = newX + 3;
                this.y = newY;
            } else {
                this.x = newX + 3;
                this.y = newY + 3;
            }
        }

        /**
         * the method find each pieces of map based visual map coordinate
         *
         * @param x x coordinate
         * @param y y coordinate
         * @return piece of map number
         */
        private int findMap(int x, int y) {
            if (x < 3 && y < 3)
                return 1;
            else if (x < 3)
                return 2;
            else if (y < 3)
                return 3;
            else
                return 4;
        }

        /**
         * this method select a piece of map randomly, when the calculate coordinate method not found best state
         */
        public void changeMapNum() {
            do {
                this.mapNum = new Random().nextInt(4) + 1;
            } while (mapNum == findMap(x, y));
            random = true;

        }

        /**
         * setter for rotate direction
         *
         * @param rotate rotate direction
         */
        public void setRotate(int rotate) {
            this.rotate = rotate;
        }

        /**
         * set count if max nut are around this coordinate
         *
         * @param max new calculated max nit around count
         */
        public void setMax(int max) {
            this.max = max;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getMapNum() {
            return mapNum;
        }

        public int getRotate() {
            return rotate;
        }

        public int getMax() {
            return max;
        }

        public boolean isRandom() {
            return random;
        }
    }

}
