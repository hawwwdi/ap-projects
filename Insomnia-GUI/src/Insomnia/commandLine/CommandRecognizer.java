package Insomnia.commandLine;

import Insomnia.GUI.Request;
import Insomnia.Utils.Method;
import Insomnia.Model.RequestModel;
import Insomnia.Utils.FileUtils;
import org.apache.commons.validator.routines.UrlValidator;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Pattern;

import static Insomnia.Utils.Method.methodRecognizer;

/**
 * command recognizer class
 *
 * @version 1.0
 */
public class CommandRecognizer {
    private static final String URL_REGEX = "((http?|https|ftp|file)://)?((W|w){3}.)?[a-zA-Z0-9]+\\.[a-zA-Z]+.*";
    private static final String FILE_REGEX = ".+[.]\\w+";
    private static final String IP_REGEX = "^((0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)\\.){3}(0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)$";

    
    public static boolean recognizeCommand(String[] command, ArrayList<RequestModel> reqModels) {
        RequestModel model = new RequestModel(false);
        reqModels.add(model);
        boolean save = false;
        String listToSave = null;
        for (int i = 0; i < command.length; i++) {
            String current = command[i].trim();
            if ("--help".equals(current) || "-H".equals(current)) {
                help();
                return false;
            } else if (Pattern.matches(URL_REGEX, current)) {
                if (UrlValidator.getInstance().isValid(current))
                    model.setUrl(current);
                else {
                    System.err.println("invalid url!!!");
                    return false;
                }
            } else if ("-i".equalsIgnoreCase(current))
                model.setShowResponseHeaders(true);
            else if ("-M".equalsIgnoreCase(current) || "--method".equalsIgnoreCase(current)) {
                String next = command[++i];
                Method method = methodRecognizer(next);
                if (method != null)
                    model.setMethod(method);
                else {
                    System.err.println("invalid method!!!");
                    return false;
                }
            } else if ("-h".equals(current) || "--headers".equalsIgnoreCase(current)) {
                try {
                    String next = command[++i];
                    model.setHeaders(next);
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("headers not Found");
                    return false;
                }
            } else if ("-f".equals(current))
                model.setFollowRedirect(true);
            else if ("-O".equalsIgnoreCase(current) || "--outPut".equalsIgnoreCase(current)) {
                try {
                    String next = command[i + 1];
                    if (Pattern.matches(FILE_REGEX, next)) {
                        model.setSavedResponseName(next);
                        i++;
                    } else {
                        String name = new SimpleDateFormat("'output_'yyyyMMddHHmm'.txt'").format(new Date());
                        model.setSavedResponseName(name);
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("name selected");
                    String name = new SimpleDateFormat("'output_'yyyyMMddHHmm'.txt'").format(new Date());
                    model.setSavedResponseName(name);
                }
            } else if ("-S".equalsIgnoreCase(current) || "--save".equalsIgnoreCase(current)) {
                try {
                    String next = command[++i];
                    if (next.charAt(0) != '-') {
                        listToSave = next;
                        save = true;
                    } else {
                        System.err.println("invalid list name");
                        return false;
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("list name not found!!!");
                    return false;
                }
            } else if ("-d".equalsIgnoreCase(current) || "--data".equalsIgnoreCase(current)) {
                try {
                    String next = command[++i];
                    if (next.charAt(0) == '"')
                        model.setMessageBody(next);
                    else {
                        System.err.println("invalid message body");
                        return false;
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("message body not found!!!");
                    return false;
                }
            } else if ("-j".equalsIgnoreCase(current) || "--json".equalsIgnoreCase(current)) {
                try {
                    String next = command[++i];
                    if ("{".equals(next.substring(0, 1))) {
                        System.out.println(next);
                        String toSet = "Json=" + next;
                        model.setMessageBody(toSet);
                    } else {
                        System.err.println("invalid Json");
                        return false;
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("Json not found!!!");
                    return false;
                }
            } else if ("--upload".equalsIgnoreCase(current)) {
                try {
                    String next = command[++i];
                    File toUpload = new File(next);
                    if (toUpload.exists()) {
                        String toSet = "file=" + next;
                        model.setMessageBody(toSet);
                    } else {
                        System.err.println("invalid file type or file not found!!!");
                        return false;
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("file patch not found!!!");
                    return false;
                }
            } else if ("create".equals(current)) {
                if (i != 0 || command.length != 2) {
                    System.err.println("bad input format!!!");
                } else try {
                    String next = command[i + 1];
                    FileUtils.addList(next);
                    System.out.println("list " + next + " created!");
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("list name not found!!!");
                }
                return false;
            } else if ("list".equals(current)) {
                if (i == 0 && command.length == 1)
                    FileUtils.showLists();
                else if (i != 0 || command.length != 2) {
                    System.err.println("bad input format!!!");
                } else try {
                    String next = command[i + 1];
                    FileUtils.showRequestsList(next);
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("list name not found!!!");
                }
                return false;
            } else if ("fire".equals(current)) {
                reqModels.remove(0);
                try {
                    String list = command[++i];
                    while (++i < command.length) {
                        RequestModel tmp = new RequestModel(false);
                        FileUtils.loadRequest(tmp, list, Integer.parseInt(command[i]) - 1);
                        reqModels.add(tmp);
                    }
                    return true;
                } catch (FileNotFoundException e) {
                    System.err.println("file not found!!!");
                    return false;
                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println("bad input format!!!");
                    return false;
                }
            } else if ("--proxy".equals(current)) {
                try {
                    String ip = command[++i];
                    if (!"--ip".equalsIgnoreCase(ip)) {
                        System.err.println("command '--ip' not found!");
                        return false;
                    }
                    String ipAddress = command[++i];
                    if (!(Pattern.matches(IP_REGEX, ipAddress) || "localhost".equals(ipAddress))) {
                        System.err.println("ip '" + ipAddress + "' is invalid!");
                        return false;
                    }
                    String port = command[++i];
                    if (!"--port".equalsIgnoreCase(port)) {
                        System.err.println("command '--port' not found!");
                        return false;
                    }
                    int portNumber = 20;
                    try {
                        portNumber = Integer.parseInt(command[++i]);
                    } catch (IllegalArgumentException e) {
                        System.err.println("port number is invalid");
                        return false;
                    }
                    model.setSendToServer(true);
                    model.setServerIp(ipAddress);
                    model.setPort(portNumber);
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("ip and port not found!");
                    return false;
                }
            } else {
                System.err.println("command \'" + current + "\' not found!!!");
                return false;
            }
        }
        if ("".equals(model.getUrl())) {
            System.err.println("url not found!!!");
            return false;
        } else {
            if (save)
                FileUtils.saveRequest(model, listToSave);
            return true;
        }
    }

    private static void help() {
        System.out.println("--url  : set uri address. e.g [--url www.google.com]");
        System.out.println("-M , --method  : set method of request. e.g [-M POST]");
        System.out.println("-d , --data  : set body massage in form data. e.g [-d \"name=value&name2=value2\"]");
        System.out.println("-H , --header  : set headers . e.g [-H \"name:value;name2:value2\"]");
        System.out.println("-S , --save  : save the request in a file.");
        System.out.println("-j , --json  : set body massage in json string.");
        System.out.println("--upload  : set a file to be upload. e.g [--upload C:/user/system/text.png");
        System.out.println("fire  : execute all requests mentioned of folder. e.g [fire myRequests 5 6]");
        System.out.println("list  : shows all request of folder e . e.g [list myRequests]");
        System.out.println("-O , --output  : save response body in file. e.g [ -O  C:/user/system/text.txt]\nif file not exists,will make a file name");
        System.out.println("-f  : actives follow redirect. ");
        System.out.println("-i  : show headers of response.");
        System.out.println("create  : create a new request folder. e.g [create myRequests]");
    }
}

