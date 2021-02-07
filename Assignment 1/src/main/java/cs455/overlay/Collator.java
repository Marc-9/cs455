package cs455.overlay;

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
    public ArrayList<Node> nodes = new ArrayList<Node>();

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

            this.din = new DataInputStream(this.socket.getInputStream());
            int nameLength = din.readInt();
            byte[] identifierBytes = new byte[nameLength];
            din.readFully(identifierBytes);
            this.connections++;
            //this.nodes.add(new String(identifierBytes));
            System.out.println(new String(identifierBytes));

            /*DataInputStream dis = new DataInputStream(s.getInputStream()); 
            DataOutputStream dos = new DataOutputStream(s.getOutputStream()); 
            int nameLength = din.readInt();
            byte[] identifierBytes = new byte[nameLength];
            din.readFully(identifierBytes);
            String nodeHostname = new String(identifierBytes);
            ArrayList<String> alteredServerList = newServerList(nodeHostName, this.serverList);


            Thread t = new ClientHandler(s, dis, dos, new String(identifierBytes), alteredServerList); 
            nodes.push(t);*/
            this.socket.close();

            this.connections++;

        }

       

    }



    public void startMessagePassing() throws IOException{ 
        for(int j = 0; j < 5000; j++){
            for(int i = 0; i < this.connections; i++){
                int recieverIndex = this.randomNum(this.connections, i);
                this.nodes.get(i).sender();
                this.nodes.get(recieverIndex).reciever(this.nodes.get(i).hostName, 8952);
            }
        }
    }

    public int randomNum(int max, int currentIndex){
        int ranNum = currentIndex;
        while(ranNum == currentIndex){
            ranNum = ThreadLocalRandom.current().nextInt(0,max+1);
        }
        return ranNum;
    }

    public static void main(String[] args) {
        String name;
        int portNum =0;
        int totHosts = 0;
        Collator master;
        try {
            name = InetAddress.getLocalHost().getHostName();
            File myObj = new File("machines.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String[] data = myReader.nextLine().split(",",2);
                if(data[0].equals(name)){
                    portNum = Integer.parseInt(data[1]);  
                }
                else{
                    ++totHosts;
                }
                
            }
            myReader.close();
            master = new Collator(name,portNum,totHosts);
            master.listenForRegistrations();
            master.startMessagePassing();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
        }

    }
       

}
