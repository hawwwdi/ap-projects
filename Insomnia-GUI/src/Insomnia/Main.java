package Insomnia;


import Insomnia.GUI.Application;
import Insomnia.commandLine.Console;

import java.util.Scanner;

/**
 * this is main class
 * it is a simple java http client application
 *
 * @author hadi
 * @version 1.0
 */
public class Main {
    public static void main(String[] args) {/*  while (true) {
            System.out.println("inpuuuuuuuuuuuuut");
            String in = scanner.nextLine();
            if (in.equals("stop"))
                break;
            Console.main(in.split(" "));
        }*/
        //Console.main("http://apapi.haditabatabaei.ir/docs --proxy --ip localhost --port 100".split(" "));
         Application app= new Application();
    }
}
