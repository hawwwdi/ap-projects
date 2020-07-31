package project;

import java.util.Scanner;

/**
 * the main class of pentago game
 * it is a simple pentago with java
 * @author hadi
 * @version 1.0
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("\u001B[32m" + "welcome to the pentago\nselect game mode:\n" + "\u001B[35m" + "1)Vs user | 2)Vs computer" + "\u001B[0m");
        GameSystem pentago = new GameSystem(new Scanner(System.in).nextInt());
        pentago.play();
    }
}
