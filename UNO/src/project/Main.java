package project;

import java.util.Scanner;

/**
 * the main class
 * this is a simple java UNO
 * @author hadi
 * @version 1.0
 */

public class Main {
    public static void main(String[] args) {
        Scanner scanner= new Scanner(System.in);
        GameConfig gameConfig= new GameConfig();
        System.out.println("enter number of Users :");
        int numberOfPlayers= scanner.nextInt();
        System.out.println("enter number of Computer players:");
        int numberOfComputerPlayers= scanner.nextInt();
        GameSystem game= gameConfig.configGame(numberOfPlayers, numberOfComputerPlayers);
        game.play();
    }
}

