package cs455.overlay;

import cs455.overlay.TCPMasterConnection;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.ArrayList;

public class Node{
    private String collatorHostName;
    private int collatorPort;
    private Socket socket;
    private DataOutputStream dout;
    private DataInputStream din;
    static int sendTracker = 0;
    static int recieveTracker = 0;
    static long recieveSummation = 0;
    public long sendSummation = 0;
    private int port;
    private String hostName;
    private TCPMasterConnection tcp;
    static ArrayList<String[]> hostNames = new ArrayList<String[]>();


    public Node(String hostName, int port, String collatorHostName, int collatorPort) {
        this.port = port;
        this.hostName = hostName;
        this.collatorHostName = collatorHostName;
        this.collatorPort = collatorPort;
      	this.tcp = new TCPMasterConnection(port);

    }

    // I hate that I did this but I found a monster race condition the night before it was due as I was double checking my work
    // The condition almost never appears but I think this may fix it
    public static synchronized void alterVars(int recieveTracker, Long recieveSummation){
    	Node.recieveTracker += recieveTracker;
    	Node.recieveSummation += recieveSummation;
    }

    // Sends necessary data to Collator to print
    // Including hostname, sent amount, recieved amount, sent summation, and recieved summation
    // This data is recieved and handled in TCPServer
    public void printData(){
        try{
            byte[] identifier = this.hostName.getBytes();
            int hostNameLength = identifier.length;
            this.dout.writeInt(hostNameLength);
            this.dout.write(identifier);
            this.dout.writeInt(this.sendTracker);
            this.dout.writeInt(this.recieveTracker);
            this.dout.writeLong(this.recieveSummation);
            this.dout.writeLong(this.sendSummation);
        }
        catch(IOException e){

        }
    }

    // Registers node with Collator, and waits for messages untill the special kill message of 0 is sent
    public void register() throws IOException {
    	// Start the thread that will listen for incoming connections
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

        // Send hostname, not required but so I may see which node connects and when
        this.dout.writeInt(hostNameLength);
        this.dout.write(identifier);
        while(true){
            int messageType = this.din.readInt();
            if(messageType == 0){
            	// Kill the thread listening for connections
            	this.tcp.interrupt();
            	// Notify the Collator you are done and to expect data from you
                this.dout.writeInt(0);
                // Send your data to the Collator
                this.printData();

                break;
            }
            else if(messageType == 4){
            	// Notify the collator you have finished all your message passing round
                this.startMessagePassing();
                this.dout.writeInt(5);
            }
            else{
            	// Do Nothing
            }

        }

    }

    public void startMessagePassing(){
        try {
            for(int rounds = 0; rounds < 5000; rounds++){
            	// Get a random num within the bounds which will correspond to a Node to connect to
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