package project;

/**
 * the gameSystem class
 * it use to config new game
 * each object of this class is a pentago game
 * it has two mode 1)user vs. user 2)user vs. computer
 */
public class GameSystem {
    private Player[] players;
    private MapManager gameBoard;

    /**
     * constructor for gameSystem class
     * it use to create new object of this class
     * @param mode game mode number
     */
    public GameSystem(int mode) {
        gameBoard = new MapManager();
        players = new Player[2];
        players[0] = new Player(gameBoard, 0);
        if (mode == 2)
            players[1] = new AiPlayer(gameBoard, 1);
        else
            players[1]= new Player(gameBoard, 1);
    }

    /**
     * the play method
     * when we call this method, the game will begins
     * if each players win the game ends and program stops
     */
    public void play() {
        gameBoard.printMap();
        int i = 0;
        while (true) {
            if (i % 2 == 0)
                players[0].turn();
            else
                players[1].turn();
            i++;
        }
    }
}
