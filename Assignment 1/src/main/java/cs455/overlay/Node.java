package cs455.overlay;

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
    public long recieveSummation = 0;
    public long sendSummation = 0;
    private int port;
    public String hostName;

    public Node(String hostName, int port) {
        this.port = port;
        this.hostName = hostName;
    }

    public void reciever(String serverAddress, int port) throws IOException {
        try {
            this.socket = new Socket(serverAddress, port);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        this.din = new DataInputStream(this.socket.getInputStream());
        for (int i = 0; i < 5; i++) {
            this.recieveTracker++;
            int temp = din.readInt();
            this.recieveSummation += temp;
            System.out.println("Recieved the number " + temp);
        }
    }

    public void closeReciever() throws IOException {
        this.din.close();
        this.socket.close();
    }

    public void sender() throws IOException {
        this.serverSocket = new ServerSocket(this.port, 5);
        this.socket = this.serverSocket.accept();
        this.dout = new DataOutputStream(this.socket.getOutputStream());
        Random rand = new Random();
        int randInt = 0;
        for (int i = 0; i < 5; i++) {
            randInt = rand.nextInt();
            this.dout.writeInt(randInt);
            this.sendTracker++;
            this.sendSummation += randInt;
            System.out.println("Sending the number " + randInt);
        }
    }

    public void closeSender() throws IOException {
        this.dout.close();
        this.serverSocket.close();
    }

    public void register() throws IOException {
        try {
            this.socket = new Socket("sacramento.cs.colostate.edu", 8952);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        this.dout = new DataOutputStream(this.socket.getOutputStream());
        byte[] identifier = this.hostName.getBytes();
        int hostNameLength = identifier.length;
        this.dout.writeInt(hostNameLength);
        this.dout.write(identifier);
        this.dout.close();
        this.socket.close();

    }

    public static void main(String[] args) {
        Node test = new Node("cod", 8652);
        if (args[0].equals("Server")) {
            try {
                test.sender();
                test.closeSender();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            try {
                test.reciever("sacramento.cs.colostate.edu", 8652);
                test.closeReciever();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}