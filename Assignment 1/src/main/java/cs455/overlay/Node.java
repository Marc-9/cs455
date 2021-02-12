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

public class Node implements Serializable{
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


    public Node(String hostName, int port) {
        this.port = port;
        this.hostName = hostName;
        this.tcp = new TCPConnection(port);
        this.tcp.start();
    }

    public void reciever(String serverAddress, int port) throws IOException {
        try {
            Socket socket = new Socket(serverAddress, port);
            DataInputStream newDin = new DataInputStream(socket.getInputStream());
            for (int i = 0; i < 5; i++) {
                this.recieveTracker++;
                int temp = newDin.readInt();
                this.recieveSummation += temp;
                //System.out.println("Recieved the number " + temp);
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

    }

    public void closeReciever() throws IOException {
        this.din.close();
        this.socket.close();
    }

    public void sender() throws IOException {
        this.serverSocket = new ServerSocket(this.port, 5);
        this.dout.writeInt(0);
        Socket socket = this.serverSocket.accept();
        //System.out.println("connection accepted");
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
        this.serverSocket.close();
        newdout.close();
    }

    public void closeSender() throws IOException {
        this.dout.close();
        this.serverSocket.close();
    }


    public void messageType1(){
        try{
            this.dout.writeInt(this.sendTracker);
            this.dout.writeInt(this.recieveTracker);
            this.dout.writeLong(this.recieveSummation);
            this.dout.writeLong(this.sendSummation);
        }
        catch(IOException e){
        }
    }

    public void messageType2(){
        try{
            this.sender();
        }
        catch(IOException e){

        }

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

    public void messageType3(){
        try{
            //System.out.println("Here to set up connection");
            int nameLength = this.din.readInt();
            byte[] identifierBytes = new byte[nameLength];
            this.din.readFully(identifierBytes);
            String connectHostName = new String(identifierBytes);
            //System.out.println(connectHostName);
            int connectPortNum = this.din.readInt();

            this.reciever(connectHostName, connectPortNum);
        }
        catch(IOException e){

        }
    }

    public void register() throws IOException {
        try {
            this.socket = new Socket("sacramento.cs.colostate.edu", 8952);
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
            else if(messageType == 1){
                this.messageType1();
                this.dout.writeInt(0);
            }
            else if(messageType == 2){
                this.messageType2();
                this.dout.writeInt(0);
            }
            else if(messageType == 3){
                this.messageType3();
                this.dout.writeInt(0);
            }
            else if(messageType == 4){
                this.testThread();
                this.dout.writeInt(5);
            }

        }

    }

    public void testThread(){
        try {
        	for(int rounds = 0; rounds < 5000; rounds++){
	        	int ranNum = ThreadLocalRandom.current().nextInt(0,3+1);
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

    public static void main(String[] args) {
        String name;
        int portNum = 0;
        Node node;
        try {
            name = InetAddress.getLocalHost().getHostName() + ".cs.colostate.edu";
            File myObj = new File("machines.txt");
            Scanner myReader = new Scanner(myObj);
            String[][] machineNames = new String[10][2];
            int machineCount = 0;
            while (myReader.hasNextLine()) {
                String[] data = myReader.nextLine().split(",",2);
                if(data[0].equals(name)){
                    portNum = Integer.parseInt(data[1]);
                }
                else if(data[0].equals("sacramento.cs.colostate.edu")){
                	continue;
                }
                else{
                	Node.hostNames.add(data);
                	++machineCount;
                }
            }
            node = new Node(name,portNum);
            myReader.close();
            node.register();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }


    }
}