package project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * this is the gameSystem class
 */
public class GameSystem {
    private Player[] players;
    private final int numberOfPlayers;
    private ArrayList<Card> cards;
    private int rotateDirection;
    private int turn;
    private Card lastCard;
    private int drawCardCount;
    private HashMap<Integer, String> rotateName;

    public GameSystem(ArrayList<Card> cards, int numberOfPlayers) {
        this.cards = cards;
        this.numberOfPlayers = numberOfPlayers;
        rotateDirection = 1;
        drawCardCount = 0;
        rotateName = new HashMap<>();
        rotateName.put(1, "clockwise ↻");
        rotateName.put(-1, "anticlockwise ↺");
    }

    public void setPlayers(Player[] players) {

        this.players = players;
    }

    public void play() {
        turn = new Random().nextInt(numberOfPlayers);
        do {
            lastCard = giveCard();
            updateLastCard(lastCard);
        } while (lastCard instanceof Wild);
        if (lastCard instanceof SkipCard || lastCard instanceof DrawCard || lastCard instanceof ReverseCard) {
            print();
            checkLastCard(true);
        }
        for (int i = 1; !finishGame(); i++) {
            System.out.println("┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉┉");
            print();
            Card newCard = players[turn].putCard(lastCard, drawCardCount);
            updateLastCard(newCard);
            checkLastCard(false);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            shiftTurn();
            if (i % numberOfPlayers == 0)
                printScoreBoard();
        }
        printScoreBoard();
        System.out.println("finished");
    }

    private void checkLastCard(boolean isFirstCard) {
        if (lastCard instanceof SkipCard) {
            if (!isFirstCard)
                shiftTurn();
            System.out.println(players[turn].getName() + " your turn is skipped!");
            if (isFirstCard)
                shiftTurn();
        } else if (lastCard instanceof ReverseCard) {
            if (!((ReverseCard) lastCard).isUsed()) {
                ((ReverseCard) lastCard).resetCard();
                rotateDirection = rotateDirection == 1 ? -1 : 1;
                System.out.println("rotate direction changed to " + rotateName.get(rotateDirection));
            }
        } else if (lastCard instanceof WildColorCard) {
            if (!((WildColorCard) lastCard).isUsed()) {
                ((WildColorCard) lastCard).changeColor(players[turn] instanceof UserPlayer);
                ((WildColorCard) lastCard).resetCard();
            }
        } else if (lastCard instanceof WildDrawCard && drawCardCount != 0) {
            ((WildDrawCard) lastCard).changeColor(players[turn] instanceof UserPlayer);
        } else if (isFirstCard && lastCard instanceof DrawCard) {
            System.out.println(players[turn].getName() + " you have to pick " + 2 + " cards :( & your turn Skipped");
            players[turn].pickCard(2);
            updateLastCard(null);
            shiftTurn();
        }
    }

    private void updateLastCard(Card newCard) {
        if (newCard != null) {
            if (lastCard instanceof Wild)
                ((Wild) lastCard).resetColor();
            else if (lastCard instanceof ReverseCard)
                ((ReverseCard) lastCard).resetCard();
            else if (lastCard instanceof WildColorCard)
                ((WildColorCard) lastCard).resetCard();
            cards.add(lastCard);
            lastCard = newCard;
            if (newCard instanceof WildDrawCard || newCard instanceof DrawCard) {
                drawCardCount++;
            }
        } else {
            drawCardCount = 0;
        }
    }

    private void shiftTurn() {
        turn += rotateDirection;
        turn = turn == -1 ? numberOfPlayers - 1 : turn % numberOfPlayers;
    }

    public Card giveCard() {
        Random random = new Random();
        int cardIndex = random.nextInt(cards.size());
        Card toReturn = cards.get(cardIndex);
        cards.remove(cardIndex);
        return toReturn;
    }

    private boolean finishGame() {
        for (Player current : players)
            if (!current.hasCard())
                return true;
        return false;
    }

    private void printScoreBoard() {
        Score[] playersScores = new Score[numberOfPlayers];
        for (int i = 0; i < numberOfPlayers; i++) {
            playersScores[i] = new Score(players[i].getName(), players[i].calculatePoints());
        }
        for (int i = 0; i < numberOfPlayers; i++) {
            for (int j = i + 1; j < numberOfPlayers; j++) {
                if (playersScores[i].Score > playersScores[j].Score) {
                    Score tmp = playersScores[i];
                    playersScores[i] = playersScores[j];
                    playersScores[j] = tmp;
                }
            }
        }
        System.out.println(Color.changeColor(Color.WHITE) + " ╔══════════════════════════╗");
        System.out.println(" ║ winner: ♕" + playersScores[0].name + "♕");
        for (int i = 0; i < numberOfPlayers; i++) {
            System.out.println(Color.changeColor(Color.WHITE) + " ║ " + playersScores[i].toString());
        }
        System.out.println(Color.changeColor(Color.WHITE) + " ╚══════════════════════════╝");
    }

    /**
     * this is an inner class for saving  players name and score 
     */
    private class Score {
        private String name;
        private int Score;

        public Score(String name, int Score) {
            this.name = name;
            this.Score = Score;
        }

        @Override
        public String toString() {
            return "name: " + name + ", Score: " + Score;
        }
    }

    private void print() {
        for (int i = 0; i < players.length; i++) {
            if (i != turn)
                players[i].showCards();
        }
        System.out.println(Color.changeColor(Color.WHITE) + " ╔═════════════════════════════════╗");
        String[] card = lastCard.toShape();
        String[] info = information();
        for (int i = 0; i < 5; i++) {
            System.out.println(Color.changeColor(Color.WHITE) + " ║ " + card[i] + " " + info[i]);
        }
        System.out.println(Color.changeColor(Color.WHITE) + " ╚═════════════════════════════════╝");
        players[turn].showTurn();
        System.out.println(Color.changeColor(Color.WHITE));
        System.out.println(players[turn].getName() + " your turn!");
    }

    private String[] information() {
        String[] shape = new String[5];
        shape[0] = Color.changeColor(Color.CYAN) + "rotate direction:" + Color.changeColor(Color.WHITE) + "     ║";
        if (rotateDirection == 1)
            shape[1] = Color.changeColor(Color.CYAN) + rotateName.get(1) + Color.changeColor(Color.WHITE) + "           ║";
        else
            shape[1] = Color.changeColor(Color.CYAN) + rotateName.get(-1) + Color.changeColor(Color.WHITE) + "       ║";
        shape[2] = Color.changeColor(Color.CYAN) + "turn: " + players[turn].getName() + Color.changeColor(Color.WHITE) + "          ║";
        shape[3] = Color.changeColor(Color.CYAN) + "number of game cards:" + Color.changeColor(Color.WHITE) + " ║";
        int numberOfCards = cards.size();
        if (numberOfCards / 10 < 10)
            shape[4] = Color.changeColor(Color.CYAN) + String.valueOf(cards.size()) + Color.changeColor(Color.WHITE) + "                    ║";
        else
            shape[4] = Color.changeColor(Color.CYAN) + String.valueOf(cards.size()) + Color.changeColor(Color.WHITE) + "                   ║";
        return shape;
    }

}
