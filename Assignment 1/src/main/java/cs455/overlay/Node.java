package cs455.overlay;


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

    public void test(){
        try{
            System.out.println(InetAddress.getLocalHost().getHostName());
        }
        
        catch (UnknownHostException e) {
            e.printStackTrace();
        }
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

       /* DataOutputStream serverOut = new DataOutputStream(s.getOutputStream()); 
        DataInputStream serverIn = new DataInputStream(s.getInputStream());
        byte[] identifier = this.hostName.getBytes();
        int hostNameLength = identifier.length;
        this.dout.writeInt(hostNameLength);
        this.dout.write(identifier);
        while(true){
            int messageType = din.readInt();
            if(messageType == 1){
                int nameLength = din.readInt();
                byte[] identifierBytes = new byte[nameLength];
                din.readFully(identifierBytes);
                String nodeHostname = new String(identifierBytes);
                int port = din.readInt();


            }

        }
        this.dout.close();
        this.socket.close();*/

    }

    public static void main(String[] args) {
        String name;
        int portNum = 0;
        Node node;
        try {
            name = InetAddress.getLocalHost().getHostName();
            File myObj = new File("machines.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String[] data = myReader.nextLine().split(",",2);
                if(data[0].equals(name)){
                    portNum = Integer.parseInt(data[1]);
                    break;
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