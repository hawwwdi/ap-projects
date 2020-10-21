package project;

/**
 * the gameSystem class
 */
public class GameSystem {
    private Player[] players;
    private MapManager gameBoard;

    public GameSystem(int mode) {
        gameBoard = new MapManager();
        players = new Player[2];
        players[0] = new Player(gameBoard, 0);
        if (mode == 2)
            players[1] = new AiPlayer(gameBoard, 1);
        else
            players[1]= new Player(gameBoard, 1);
    }

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
