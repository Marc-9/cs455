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

    public void test(){
        try{
            System.out.println(InetAddress.getLocalHost().getHostName());
        }

        catch (UnknownHostException e) {
            e.printStackTrace();
        }
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

    public void messageType3(){
        try{
            //System.out.println("Here to set up connection");
            int nameLength = this.din.readInt();
            byte[] identifierBytes = new byte[nameLength];
            this.din.readFully(identifierBytes);
            String connectHostName = new String(identifierBytes);
            connectHostName += ".cs.colostate.edu";
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
        this.dout.writeInt(this.port);
        while(true){
            int messageType = this.din.readInt();
            //System.out.println(messageType);
            if(messageType == 0){
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

        }

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