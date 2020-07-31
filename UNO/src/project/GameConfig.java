package project;

import java.util.ArrayList;
import java.util.Collections;

/**
 * this game config class
 * it used to config a new uno game
 */
public class GameConfig {
    /**
     * the method create game cards object and game players
     * and with created object, create a object of unoGame and return this
     * @param users number of user players
     * @param computers number of computer players
     * @return GameSystem object of new game
     */
    public GameSystem configGame(int users, int computers) {
        int numberOfPlayer = users + computers;
        int cardsPackCount = (int) Math.ceil(numberOfPlayer / 10d);
        ArrayList<Card> cards = new ArrayList<>();
        for (int i = 0; i < cardsPackCount; i++) {
            for (Color color : Color.values()) {
                if (color == Color.BLACK || color == Color.WHITE || color == Color.CYAN)
                    continue;
                for (int j = 0; j < 10; j++) {
                    cards.add(new NumericCard(color, j));
                    if (j != 0)
                        cards.add((new NumericCard(color, j)));
                }
                cards.add(new ReverseCard(color));
                cards.add(new SkipCard(color));
                cards.add(new DrawCard(color));
                cards.add(new ReverseCard(color));
                cards.add(new SkipCard(color));
                cards.add(new DrawCard(color));
            }
            for (int j = 0; j < 4; j++) {
                cards.add(new WildColorCard());
                cards.add(new WildDrawCard());
            }
        }
        Collections.shuffle(cards);
        GameSystem gameSystem = new GameSystem(cards, numberOfPlayer);
        Player[] players = new Player[numberOfPlayer];
        for (int i = 0; i < users; i++) {
            players[i] = new UserPlayer("USER " + String.valueOf(i + 1), gameSystem);
            players[i].pickCard(7);
        }
        for (int i = 0; i <computers ; i++) {
            players[i+users]= new ComputerPlayer("COMP "+ String.valueOf(i+1+users), gameSystem);
            players[i+users].pickCard(7);
        }
        gameSystem.setPlayers(players);
        return gameSystem;
    }
}
