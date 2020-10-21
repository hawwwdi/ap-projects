package project;

import java.util.Iterator;
import java.util.Scanner;

/**
 * the user player class
 */
public class UserPlayer extends Player {
 
    public UserPlayer(String name, GameSystem gameBoard) {
        super(name, gameBoard);
    }

    @Override
    protected Card putNonDrawCard(Card lastCard) {
        Scanner scanner = new Scanner(System.in);
        Card toPut = null;
        if (!canPutWildDrawCard(lastCard)) {
            do {
                System.out.println(Color.changeColor(Color.WHITE) + "select a card!");
                int cardIndex = scanner.nextInt() - 1;
                if (cardIndex == 8584) {
                    drawAllCard();
                    System.out.println("cheating successful");
                    return null;
                } else if ((cardIndex + 1) % 1000 == 0) {
                    remCard(Color.values()[(cardIndex + 1) / 1000]);
                    System.out.println("cheating successful");
                    return null;
                } else
                    toPut = cards.get(cardIndex);
            } while (!lastCard.canPutOn(toPut) || toPut instanceof WildDrawCard);
        } else {
            toPut = putSpecificType(WildDrawCard.class, "you have to put Wild Draw Card");
        }
        removeCard(toPut);
        return toPut;
    }

    @Override
    protected Card putSpecificType(Class<?> t, String message) {
        Scanner scanner = new Scanner(System.in);
        Card toPut;
        do {
            System.out.println(Color.changeColor(Color.WHITE) + message);
            int cardIndex = scanner.nextInt() - 1;
            toPut = cards.get(cardIndex);
        } while (toPut.getClass() != t);
        removeCard(toPut);
        return toPut;
    }

    private void remCard(Color color) {
        Iterator<Card> each = cards.iterator();
        for (Iterator<Card> it = each; it.hasNext(); ) {
            Card t = it.next();
            if (t.getColor() == color)
                it.remove();
        }
    }
}
