package project;

import java.util.Scanner;

/**
 * this is a simple othello game whit java
 * @author hadi
 * @version 1.0
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("Welcome to the game!");
        System.out.println("select game mode:\n1)VS .Com\n2)VS .User");
        GameSystem game = new GameSystem(new Scanner(System.in).nextInt());
        game.play();
    }
}
