package Insomnia.commandLine;

import Insomnia.Model.RequestModel;
import Insomnia.Model.ResponseModel;
import Insomnia.Utils.FileUtils;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * console class
 * this the Main class of CUI
 *
 * @author haadi
 * @version 1.0
 */
public class Console {
    public static void main(String[] args) {
        while (true) {
            System.out.println("input:");
            String input = new Scanner(System.in).nextLine();
            ArrayList<RequestModel> models = new ArrayList<>();
            boolean state = CommandRecognizer.recognizeCommand(input.split(" "), models);
            if (state) {
                for (RequestModel model : models) {
                    ResponseModel response = new RequestSender(model).send();
                    FileUtils.saveResponse(response.toString(), model);
                    System.out.println(response.toString());
                }
            }
        }
    }
}

