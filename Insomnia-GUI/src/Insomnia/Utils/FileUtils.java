package Insomnia.Utils;

import Insomnia.Model.RequestModel;

import java.io.*;
import java.util.HashMap;

/**
 * the file utils class
 * all file working is done here
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

    /**
     * this method returns array of Gui saved workSpace
     * @return array of Gui saved workSpace
     */
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
     * this method show all saved request in given list
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

    /**
     * it use to create new req list
     * @param name new list name
     */
    public static void addList(String name) {
        File newList = new File(REQUESTS_SAVE_PATH + name);
        if (!newList.exists())
            newList.mkdirs();
        else
            System.err.println("list \'" + name + "\' already exists!");
    }

    /**
     * it use to save a request model to given list
     * @param model model to save
     * @param listName list name
     */
    public static void saveRequest(RequestModel model, String listName) {
        if (!new File(REQUESTS_SAVE_PATH + listName).exists())
            addList(listName);
        String filePath = getFileName(model, listName);
        writeReq(filePath, model);
    }

    /**
     * this an overload of save request model
     * it use to save gui request
     * @param model request to save
     * @param workSpaceName request work space
     * @param listName request list name
     * @param name request name
     */
    public static void saveRequest(RequestModel model, String workSpaceName, String listName, String name) {
        File saveFolder = new File(GUI_SAVE_PATH + workSpaceName + File.separator + listName);
        saveFolder.mkdirs();
        String filePath = saveFolder.getAbsolutePath() + File.separator + name + ".txt";
        writeReq(filePath, model);
    }

    /**
     * it use to write a req model to a txt file
     * @param filePath file save path
     * @param model req model
     */
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

    /**
     * it use to load a req model from saved files
     * @param model reqModel object
     * @param list reqModel list
     * @param index req model index in the list
     * @throws FileNotFoundException
     */
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

    /**
     * it use to load request of a work space and put these in a hash map
     * @param workSpaceName list workSpace name
     * @param listName list to load
     * @return hash map of request name -> reqModel
     */
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

    /**
     * it use to load a saved request file from given path
     * @param model model to be load
     * @param selected file path
     */
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

    /**
     * it use to get appropriate name for saving given request
     * @param model given req
     * @param list list name
     * @return appropriate name
     */
    private static String getFileName(RequestModel model, String list) {
        StringBuilder path = new StringBuilder(REQUESTS_SAVE_PATH + list + "\\");
        path.append(model.toString().substring(0, 82).replaceAll("[\\\\/:\\*\\?\"\\>\\<\\|]", "-") + ".txt");
        return path.toString();
    }

    /**
     * it use to save a request response body as a file
     * @param toSave response body string
     * @param model request model
     */
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

    /**
     * it use to save gui option in a file
     * @param toSave gui option as String
     * @param name saved file name
     */
    public static void saveObject(String toSave, String name) {
        try (DataOutputStream writer =
                     new DataOutputStream(new FileOutputStream(SAVE_PATH + File.separator + name + ".txt"))) {
            writer.writeUTF(toSave);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * it use to load gui option from disk
     * @param name file name
     * @return option as string
     * @throws FileNotFoundException
     */
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

