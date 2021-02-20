package cs455.overlay;

import cs455.overlay.TCPConnection;
import java.io.DataInputStream;
import java.io.ObjectOutputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.ArrayList;

public class Node{
    public String collatorHostName;
    public int collatorPort;
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
    public TCPConnection tcp;
    static ArrayList<String[]> hostNames = new ArrayList<String[]>();


    public Node(String hostName, int port, String collatorHostName, int collatorPort) {
        this.port = port;
        this.hostName = hostName;
        this.collatorHostName = collatorHostName;
        this.collatorPort = collatorPort;
        this.tcp = new TCPConnection(port);

    }


    public void printData(){
        try{
            byte[] identifier = this.hostName.getBytes();
            int hostNameLength = identifier.length;
            this.dout.writeInt(hostNameLength);
            this.dout.write(identifier);
            this.dout.writeInt(this.sendTracker);
            this.dout.writeInt(this.tcp.received);
            this.dout.writeLong(this.tcp.receivedSummation);
            this.dout.writeLong(this.sendSummation);
        }
        catch(IOException e){

        }
    }


    public void register() throws IOException {
        this.tcp.start();
        try {
            this.socket = new Socket(this.collatorHostName, this.collatorPort);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        this.dout = new DataOutputStream(this.socket.getOutputStream());
        this.din = new DataInputStream(this.socket.getInputStream());
        byte[] identifier = this.hostName.getBytes();
        int hostNameLength = identifier.length;
        this.dout.writeInt(hostNameLength);
        this.dout.write(identifier);
        while(true){
            int messageType = this.din.readInt();
            if(messageType == 0){
                this.tcp.interrupt();
                this.dout.writeInt(0);
                this.printData();
                break;
            }
            else if(messageType == 4){
                this.testThread();
                this.dout.writeInt(5);
            }

        }

    }

    public void testThread(){
        try {
            for(int rounds = 0; rounds < 10000; rounds++){
                int ranNum = ThreadLocalRandom.current().nextInt(0,Node.hostNames.size());
                Socket socket = new Socket(Node.hostNames.get(ranNum)[0], Integer.parseInt(Node.hostNames.get(ranNum)[1]));
                DataOutputStream newdout = new DataOutputStream(socket.getOutputStream());
                Random rand = new Random();
                int randInt = 0;
                for (int i = 0; i < 5; i++) {
                    randInt = rand.nextInt();
                    newdout.writeInt(randInt);
                    this.sendTracker++;
                    this.sendSummation += randInt;
                    //System.out.println("Sending the number " + randInt);
                }
            }
        }
        catch(IOException e){

        }
    }

}