package cs455.overlay.node;

import java.io;
import java.net;
import java.util.Random;

public class Node(){
    private Socket socket;
    private ServerSocket serverSocket;
    private DataOutputStream dout;
    private DataInputStream din;
    public int sendTracker = 0;
    public int recieveTracker = 0;
    public long totRecived = 0;
    public long totSent = 0;
    private int port;

    public Node(int port){
        this.port = port;
    }

    public reciever(String serverAddress, int port){
        this.socket = new Socket(serverAddress,port);
        this.din = new DataInputStream(this.socket.getInputStream());
        for(int i = 0; i < 5; i++){
            this.recieveTracker++;
            this.totRecived += din.readInt();
        }
    }

    public closeReciever(){
        this.din.close();
        this.socket.close();
    }

    public sender(){
        this.serverSocket = new ServerSocket(this.port, 5);
        this.socket = this.serverSocket.accept();
        this.dout = new DataOutputStream(this.socket.getOutputStream());
        Random rand = new Random();
        int randInt = 0;
        for(int i = 0; i < 5; i++){
            randInt = Random.nextInt();
            this.dout.writeInt(randInt);
            this.sendTracker++;
            this.totSent += randInt;
        }
    }

    public closeSender(){
        this.dout.close();
        this.serverSocket.close();
    }
}