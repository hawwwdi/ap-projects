package Insomnia.Server;


import Insomnia.Model.RequestModel;
import Insomnia.Model.ResponseModel;
import Insomnia.commandLine.RequestSender;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * server proxy class
 * it is a client handler
 * it is create a server with server socket class
 */
public class ServerProxy {
    public static void main(String[] args) {
        ExecutorService pool = Executors.newCachedThreadPool();
        int count = 1;
        ServerSocket server;
        try {
            server = new ServerSocket(1232);
            while (true) {
                System.out.println("waiting for request " + count);
                Socket client = server.accept();
                System.out.println("request " + count + " accepted");
                pool.execute(new Thread(new clientHandler(client)));
                count++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

/**
 * this is client handler class
 * it run method send request to server with request sender class
 * and send request response with socket to the client
 */
class clientHandler implements Runnable {
    private Socket client;

    /**
     * constructor of this class
     * @param client client socket
     */
    public clientHandler(Socket client) {
        this.client = client;
    }

    /**
     * this is an override of run method
     * this method send request to server and get request response
     */
    @Override
    public void run() {
        try {
            InputStream in = client.getInputStream();
            ObjectInputStream reader = new ObjectInputStream(in);
            System.out.println("receiving req...");
            RequestModel req = (RequestModel) reader.readObject();
            System.out.println("request received!");
            ResponseModel res = new RequestSender(req).send();
            System.out.println("sending response...");
            OutputStream out = client.getOutputStream();
            ObjectOutputStream writer = new ObjectOutputStream(out);
            writer.writeObject(res);
            System.out.println("response send!");
            System.out.println("------------------------------------------------------");
            Thread.sleep(2000);
            client.close();
        } catch (IOException | InterruptedException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

