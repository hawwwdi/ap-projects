package project;

/**
 * this is game System class
 */
public class GameSystem {
    Map board;
    Player[] players;

    public GameSystem(int mode) {
        board = new Map();
        players = new Player[2];
        players[0] = new Player(0, board);
        if (mode == 1)
            players[1] = new AiPlayer(1, board);
        else
            players[1] = new Player(1, board);
    }

    public void play() {
        int i = 0;
        while (true) {
            if (players[i%2].hasTurn()) {
                board.print();
                players[i].putNut();
            } else if (players[(i + 1)%2].hasTurn()) {
                board.print();
                players[(i + 1)%2].putNut();
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
