package Insomnia.commandLine;

import Insomnia.Model.RequestModel;
import Insomnia.Model.ResponseModel;
import Insomnia.Utils.FileUtils;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * console class
 *
 * @author hadi 
 * @version 1.0
 */
public class Console {
    public static void main(String[] args) {
        ArrayList<RequestModel> models = new ArrayList<>();
        boolean valid = CommandRecognizer.recognizeCommand(args, models);
        if (valid) {
            for (RequestModel model : models) {
                ResponseModel response = new RequestSender(model).send();
                FileUtils.saveResponse(response.toString(), model);
                System.out.println(response.toString());
            }
        }
    }
}


