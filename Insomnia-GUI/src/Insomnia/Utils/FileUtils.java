package Insomnia.Utils;

import Insomnia.Model.RequestModel;

import java.io.*;
import java.util.HashMap;

/**
 * the file utils class
 */
public class FileUtils {
    private static final String SAVE_PATH = "./save/";
    private static final String GUI_SAVE_PATH = SAVE_PATH + "GUI/";
    private static final String REQUESTS_SAVE_PATH = SAVE_PATH + "REQUESTS/";
    private static final String RESPONSE_SAVE_PATH = SAVE_PATH + "RESPONSE/";

    static {
        System.out.println("Creating " + GUI_SAVE_PATH + " directory is successful: " + new File(GUI_SAVE_PATH).mkdirs());
        System.out.println("Creating " + REQUESTS_SAVE_PATH + " directory is successful: " + new File(REQUESTS_SAVE_PATH).mkdirs());
        System.out.println("Creating " + RESPONSE_SAVE_PATH + " directory is successful: " + new File(RESPONSE_SAVE_PATH).mkdirs());
    }

    public static File[] getGuiWorkSpaces() {
        return new File(GUI_SAVE_PATH).listFiles();
    }

    /**
     * this method shows saved list in cli
     */
    public static void showLists() {
        File[] lists = new File(REQUESTS_SAVE_PATH).listFiles();
        int i = 1;
        for (File folder : lists)
            if (folder.isDirectory())
                System.out.println(i++ + ") " + folder.getName());
    }

    /**
     * this method shows all saved request in given list
     * @param listName selected list
     */
    public static void showRequestsList(String listName) {
        File folder = new File(REQUESTS_SAVE_PATH + listName);
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();
            int j = 1;
            for (File req : files) {
                System.out.println(j++ + ") " + req.getName());
            }
        } else
            System.err.println("list " + listName + "not found!!!");
    }

    public static void addList(String name) {
        File newList = new File(REQUESTS_SAVE_PATH + name);
        if (!newList.exists())
            newList.mkdirs();
        else
            System.err.println("list \'" + name + "\' already exists!");
    }

    public static void saveRequest(RequestModel model, String listName) {
        if (!new File(REQUESTS_SAVE_PATH + listName).exists())
            addList(listName);
        String filePath = getFileName(model, listName);
        writeReq(filePath, model);
    }

    public static void saveRequest(RequestModel model, String workSpaceName, String listName, String name) {
        File saveFolder = new File(GUI_SAVE_PATH + workSpaceName + File.separator + listName);
        saveFolder.mkdirs();
        String filePath = saveFolder.getAbsolutePath() + File.separator + name + ".txt";
        writeReq(filePath, model);
    }

    private static void writeReq(String filePath, RequestModel model) {
        try (DataOutputStream writer = new DataOutputStream(new FileOutputStream(filePath))) {
            writer.writeUTF(model.getUrl());
            writer.writeUTF(model.getMethod().toString());
            writer.writeUTF(model.getHeaders());
            writer.writeUTF(model.getMessageBody());
            writer.writeUTF(model.getSavedResponseName());
            writer.writeUTF(model.getQuery());
            writer.writeUTF(model.getServerIp());
            writer.writeInt(model.getPort());
            writer.writeBoolean(model.isSendToServer());
            writer.writeBoolean(model.isFollowRedirect());
            writer.writeBoolean(model.isShowResponseHeaders());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadRequest(RequestModel model, String list, int index) throws FileNotFoundException {
        File selectedList = new File(REQUESTS_SAVE_PATH + list);
        if (selectedList.exists()) {
            try {
                File selected = selectedList.listFiles()[index];
                loadReq(model, selected);
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new ArrayIndexOutOfBoundsException("index " + index + " is invalid!!!");
            }
        } else
            throw new FileNotFoundException("list " + list + " not exists!!1");
    }

    public static HashMap<String, RequestModel> loadRequests(String workSpaceName, String listName) {
        HashMap<String, RequestModel> toReturn = new HashMap<String, RequestModel>();
        File listFolder = new File(GUI_SAVE_PATH + workSpaceName + File.separator + listName);
        for (File file : listFolder.listFiles()) {
            if (file.isFile()) {
                RequestModel tmp = new RequestModel(false);
                loadReq(tmp, file);
                toReturn.put(file.getName(), tmp);
            }
        }
        return toReturn;

    }

    public static void loadReq(RequestModel model, File selected) {
        DataInputStream reader = null;
        try {
            reader = new DataInputStream(new FileInputStream(selected));
            model.setUrl(reader.readUTF());
            model.setMethod(Method.methodRecognizer(reader.readUTF()));
            model.setHeaders(reader.readUTF());
            model.setMessageBody(reader.readUTF());
            model.setSavedResponseName(reader.readUTF());
            model.setQuery(reader.readUTF());
            model.setServerIp(reader.readUTF());
            model.setPort(reader.readInt());
            model.setSendToServer(reader.readBoolean());
            model.setFollowRedirect(reader.readBoolean());
            model.setShowResponseHeaders(reader.readBoolean());
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getFileName(RequestModel model, String list) {
        StringBuilder path = new StringBuilder(REQUESTS_SAVE_PATH + list + "\\");
        path.append(model.toString().substring(0, 82).replaceAll("[\\\\/:\\*\\?\"\\>\\<\\|]", "-") + ".txt");
        return path.toString();
    }

    public static void saveResponse(String toSave, RequestModel model) {
        if (model.getSavedResponseName().equals(""))
            return;
        String path = RESPONSE_SAVE_PATH + model.getSavedResponseName();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            System.out.println(toSave);
            writer.write(toSave);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveObject(String toSave, String name) {
        try (DataOutputStream writer =
                     new DataOutputStream(new FileOutputStream(SAVE_PATH + File.separator + name + ".txt"))) {
            writer.writeUTF(toSave);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String loadObject(String name) throws FileNotFoundException {
        File Load = new File(SAVE_PATH + File.separator + name + ".txt");
        try {
            FileInputStream in = new FileInputStream(Load);
            DataInputStream reader = new DataInputStream(in);
            String option = reader.readUTF();
            reader.close();
            in.close();
            return option;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return "false true 0";
        }
    }
}

