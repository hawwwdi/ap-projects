package Insomnia.Server;

import Insomnia.Model.RequestModel;
import Insomnia.Model.ResponseModel;
import Insomnia.commandLine.RequestSender;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * this is the request client class
 * it use to send a request to proxy server and receive response
 */
public class RequestClient {
    private RequestModel model;

    /**
     * constructor of this class
     * it use to create new object of this class
     *
     * @param model request model
     */
    public RequestClient(RequestModel model) {
        this.model = model;
    }

    /**
     * the send method
     * it send request model to server proxy with  socket class
     * and receive response of this request and return it
     *
     * @return request response
     * @throws IOException
     * @throws InterruptedException
     */
    public ResponseModel send() throws IOException, ConnectException {
        try {
            System.out.println("ip=" + model.getServerIp() + "&port=" + model.getPort());
            Socket socket = new Socket(model.getServerIp(), model.getPort());
            OutputStream out = socket.getOutputStream();
            ObjectOutputStream writer = new ObjectOutputStream(out);
            System.out.println("sending req...");
            writer.writeObject(model.getCopy());
            System.out.println("req send!");
            System.out.println("receiving response...");
            InputStream in = socket.getInputStream();
            ObjectInputStream reader = new ObjectInputStream(in);
            ResponseModel res = (ResponseModel) reader.readObject();
            System.out.println("response received!");
            socket.close();
            return res;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new IOException("server : " + model.getServerIp() + " | port: " + model.getPort() + " not found!");
        }
    }
}
