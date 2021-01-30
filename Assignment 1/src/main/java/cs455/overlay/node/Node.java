package cs455.overlay.node;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

public class Node {
    private Socket socket;
    private ServerSocket serverSocket;
    private DataOutputStream dout;
    private DataInputStream din;
    public int sendTracker = 0;
    public int recieveTracker = 0;
    public long totRecived = 0;
    public long totSent = 0;
    private int port;

    public Node(int port) {
        this.port = port;
    }

    public void reciever(String serverAddress, int port) throws IOException {
        try {
            this.socket = new Socket(serverAddress, port);
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        this.din = new DataInputStream(this.socket.getInputStream());
        for (int i = 0; i < 5; i++) {
            this.recieveTracker++;
            this.totRecived += din.readInt();
        }
    }

    public void closeReciever() throws IOException {
        this.din.close();
        this.socket.close();
    }

    public void sender() throws IOException{ 
        try {
            this.serverSocket = new ServerSocket(this.port, 5);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        this.socket = this.serverSocket.accept();
        this.dout = new DataOutputStream(this.socket.getOutputStream());
        Random rand = new Random();
        int randInt = 0;
        for (int i = 0; i < 5; i++) {
            randInt = rand.nextInt();
            this.dout.writeInt(randInt);
            this.sendTracker++;
            this.totSent += randInt;
        }
    }

    public void closeSender() throws IOException {
        this.dout.close();
        this.serverSocket.close();
    }
}