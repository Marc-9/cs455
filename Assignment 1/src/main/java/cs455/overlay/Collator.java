package cs455.overlay;

import cs455.overlay.TCPServer;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.io.File;
import java.io.FileReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
import java.lang.ClassNotFoundException;

public class Collator {
    private Socket socket;
    private ServerSocket serverSocket;
    private DataOutputStream dout;
    private DataInputStream din;
    public int portNumber;
    public String hostName;
    public int totConnections;
    public int connections = 0;
    public int totRounds = 0;
    public ArrayList<Socket> nodes = new ArrayList<Socket>();
    public ArrayList<String> socketInfo = new ArrayList<String>();
    public ArrayList<DataOutputStream> outputStreams = new ArrayList<DataOutputStream>();
    public ArrayList<TCPServer> threads = new ArrayList<TCPServer>();
    public int totSent = 0;
    public static Long recievedSummation = 0L;
    public static Long sendSummation = 0L;
    public static int sendTracker = 0;
    public static int recieveTracker = 0;

    public Collator(String hostName, int portNumber, int totConnections) {
        this.hostName = hostName;
        this.portNumber = portNumber;
        this.totConnections = totConnections;
        try {
            this.serverSocket = new ServerSocket(this.portNumber, totConnections);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void listenForRegistrations() throws IOException {
        while (this.connections < this.totConnections) {
            this.socket = this.serverSocket.accept();
            this.nodes.add(this.socket);
            this.din = new DataInputStream(this.socket.getInputStream());
            this.outputStreams.add(new DataOutputStream(this.socket.getOutputStream()));
            int nameLength = this.din.readInt();
            byte[] identifierBytes = new byte[nameLength];
            this.din.readFully(identifierBytes);
            String socketName = new String(identifierBytes);
            this.socketInfo.add(socketName);
            TCPServer thread1 = new TCPServer(this.din);
            this.threads.add(thread1);
            System.out.println(socketName);
            this.connections++;
        }
        System.out.println("finished message passing starting");


    }



    public void startMessagePassing() throws IOException{
        for(int i = 0; i < this.connections; i++){
            this.threads.get(i).start();
        }
        for(int i = 0; i < this.connections; i++){
            this.outputStreams.get(i).writeInt(4);
        }
        while(this.checkThreads()){

        }
        for(int i = 0; i < this.connections; i++){
            this.threads.clear();
            this.din = new DataInputStream(this.nodes.get(i).getInputStream());
            TCPServer thread1 = new TCPServer(this.din);
            this.threads.add(thread1);
            thread1.start();
            this.outputStreams.get(i).writeInt(0);
        }
        while(this.checkThreads()){

        }
        System.out.println("The system sent a total of " + Collator.sendTracker + " messages and recieved " + Collator.recieveTracker + 
            " for a total sum of " + Collator.sendSummation + " sent and " + Collator.recievedSummation + " received");
        


    }

    public static synchronized void alterGlobals(int sendTracker,int recieveTracker, Long recievedSummation, Long sendSummation){
        Collator.recievedSummation += recievedSummation;
        Collator.sendSummation += sendSummation;
        Collator.recieveTracker += recieveTracker;
        Collator.sendTracker += sendTracker;
    }

    public boolean checkThreads(){
        boolean alive = false;
        for(int i = 0; i < this.threads.size(); i++){
            if(this.threads.get(i).isAlive()){
                alive = true;
            }
        }
        return alive;
    }



    public void test(){
        try {
            for(int i = 0; i < this.connections; i++){
                this.outputStreams.get(i).writeInt(0);
            }

        }
        catch(IOException e){

        }

    }

    public int randomNum(int max, int currentIndex){
        int ranNum = currentIndex;
        while(ranNum == currentIndex){
            ranNum = ThreadLocalRandom.current().nextInt(0,max+1);
        }
        return ranNum;
    }



}