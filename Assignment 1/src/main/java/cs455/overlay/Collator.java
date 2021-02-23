package cs455.overlay;

import cs455.overlay.TCPServer;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Collator {
    private Socket socket;
    private ServerSocket serverSocket;
    private DataOutputStream dout;
    private DataInputStream din;
    public int portNumber;
    private String hostName;
    private int totConnections;
    private int connections = 0;
    private ArrayList<Socket> nodes = new ArrayList<Socket>();
    private ArrayList<DataOutputStream> outputStreams = new ArrayList<DataOutputStream>();
    private ArrayList<TCPServer> threads = new ArrayList<TCPServer>();

    // System variables, static so they are easier to modify from the TCP Server class, this is bad practice
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

            // Data input and output streams have been created, the output stream is saved as the node is waiting for a message to start
            // The input stream is used now to get the nodes name, not necessary but I like to see that connections are made
            int nameLength = this.din.readInt();
            byte[] identifierBytes = new byte[nameLength];
            this.din.readFully(identifierBytes);
            String socketName = new String(identifierBytes);

            // Start a seperate thread with the input stream so threads can notify the collator when they are finished sending messages and the results     
            TCPServer thread1 = new TCPServer(this.din);
            this.threads.add(thread1);
            System.out.println(socketName);
            this.connections++;
        }
        System.out.println("All nodes connected, message passing will now commence");


    }



    public void startMessagePassing() throws IOException{
    	// Start the TCPServer listening threads so that the nodes can notify us when they finish
        for(int i = 0; i < this.connections; i++){
            this.threads.get(i).start();
        }

        // The special number 4 tells the nodes to begin message passing
        for(int i = 0; i < this.connections; i++){
            this.outputStreams.get(i).writeInt(4);
        }

        // This loop prevents the Collator from continuing by checking if all its listening threads are still active
        // Check the method for a description of how it works
        while(this.checkThreads()){

        }

        // Every node has finished message passing, now clear the old array of listening threads, and start again listening
        // The nodes will now report back how much they sent and recieved, once they have send the special number 0 to end execution
        for(int i = 0; i < this.connections; i++){
            this.threads.clear();
            this.din = new DataInputStream(this.nodes.get(i).getInputStream());
            TCPServer thread1 = new TCPServer(this.din);
            this.threads.add(thread1);
            thread1.start();
            this.outputStreams.get(i).writeInt(0);
        }

        // Again we wait on every thread to report back so that we dont report the results untill they are complete
        while(this.checkThreads()){

        }
        System.out.println("The system sent a total of " + Collator.sendTracker + " messages and recieved " + Collator.recieveTracker +
                " for a total sum of " + Collator.sendSummation + " sent and " + Collator.recievedSummation + " received");

    }

    // A bad practice way to allow the threads to alter the global tracking variables
    public static synchronized void alterGlobals(int sendTracker,int recieveTracker, Long recievedSummation, Long sendSummation){
        Collator.recievedSummation += recievedSummation;
        Collator.sendSummation += sendSummation;
        Collator.recieveTracker += recieveTracker;
        Collator.sendTracker += sendTracker;
    }

    // To prevent us from continuing untill every thread has finished, we keep a listening thread running, when a thread is finished it send us a message
    // and we kill the thread, as long as 1 thread is still running this will return false and prevent us from continuing execution
    public boolean checkThreads(){
        boolean alive = false;
        for(int i = 0; i < this.threads.size(); i++){
            if(this.threads.get(i).isAlive()){
                alive = true;
            }
        }
        return alive;
    }


    // Apparently necessary to prevent race conditions idk I thought I sent the kill signal 0 at the end of message passing but idk
    public void endConnection(){
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