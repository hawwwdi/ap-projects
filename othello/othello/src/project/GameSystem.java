package project;

/**
 * this is game System class
 * it use to handle game System
 * we can create new game with create new object of this class
 * it has game players and game map
 */
public class GameSystem {
    Map board;
    Player[] players;

    /**
     * this is constructor for GameSystem class
     *  1 for play with computer and something else for play with another user
     * @param mode game mode its change player definition
     */
    public GameSystem(int mode) {
        board = new Map();
        players = new Player[2];
        players[0] = new Player(0, board);
        if (mode == 1)
            players[1] = new AiPlayer(1, board);
        else
            players[1] = new Player(1, board);
    }

    /**
     * thr play method
     * call it to start game :)
     */
    public void play() {
        int i = 0;
        while (true) {
            if (players[i].hasTurn()) {
                board.print();
                players[i].putNut();
            } else if (players[i + 1].hasTurn()) {
                board.print();
                players[i + 1].putNut();
                i = (i + 1) % 2;
            } else {
                board.showResult();
                System.out.println('\u2620'+""+'\u2620'+"game end!"+'\u2620'+'\u2620');
                break;
            }
            i = (i + 1) % 2;
        }
    }
}
