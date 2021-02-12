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
    public ArrayList<Socket> nodes = new ArrayList<Socket>();
    public ArrayList<String[]> socketInfo = new ArrayList<String[]>();
    public ArrayList<DataInputStream> inputStreams = new ArrayList<DataInputStream>();
    public ArrayList<DataOutputStream> outputStreams = new ArrayList<DataOutputStream>();
    public int totSent = 0;
    public long recieveSummation = 0;
    public long sendSummation = 0;

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
            this.connections++;
            DataInputStream din2 = new DataInputStream(this.socket.getInputStream());
            DataOutputStream dout9 = new DataOutputStream(this.socket.getOutputStream());
            inputStreams.add(din2);
            outputStreams.add(dout9);
            int nameLength = din2.readInt();
            byte[] identifierBytes = new byte[nameLength];
            din2.readFully(identifierBytes);
            int socketPort = din2.readInt();
            this.nodes.add(this.socket);

            String socketName = new String(identifierBytes);
            String[] temp = {socketName, String.valueOf(socketPort)};
            this.socketInfo.add(temp);
            System.out.println(socketName);
        }


    }



    public void startMessagePassing() throws IOException{
        for(int i = 0; i < this.connections-1; i++){
            System.out.println("Starting with " + this.socketInfo.get(i)[0]);
            this.outputStreams.get(i).writeInt(4);
        }
    		/*for(int j = 0; j < 100; j++){





    			//DataOutputStream dout2 = new DataOutputStream(this.nodes.get(i).getOutputStream());
    			//DataInputStream din5 = new DataInputStream(this.nodes.get(i).getInputStream());
    			this.outputStreams.get(i).writeInt(2);
    			this.inputStreams.get(i).readInt();
    			int randomNum = this.randomNum(this.connections-1, i);
    			//DataOutputStream dout3 = new DataOutputStream(this.nodes.get(randomNum).getOutputStream());
    			//DataInputStream din8 = new DataInputStream(this.nodes.get(randomNum).getInputStream());
    			this.outputStreams.get(randomNum).writeInt(3);
    			//dout3.writeInt(3);
    			byte[] identifier = this.socketInfo.get(i)[0].getBytes();
        		int hostNameLength = identifier.length;
        		this.outputStreams.get(randomNum).writeInt(hostNameLength);
        		this.outputStreams.get(randomNum).write(identifier);
        		this.outputStreams.get(randomNum).writeInt(Integer.parseInt(this.socketInfo.get(i)[1]));
        		//dout3.writeInt(hostNameLength);
        		//dout3.write(identifier);
        		//dout3.writeInt(Integer.parseInt(this.socketInfo.get(i)[1]));
        		//din5.readInt();
        		this.inputStreams.get(i).readInt();
        		this.inputStreams.get(randomNum).readInt();
        		//din8.readInt();

    		}
    	}
    	for(int i = 0; i < this.connections; i++){
    		DataOutputStream dout4 = new DataOutputStream(this.nodes.get(i).getOutputStream());
    		DataInputStream din4 = new DataInputStream(this.nodes.get(i).getInputStream());
    		dout4.writeInt(1);
    		int sendTracker = din4.readInt();
    		int recieveTracker = din4.readInt();
    		Long recieveSummation2 = din4.readLong();
    		Long sendSummation2 = din4.readLong();
    		this.recieveSummation += recieveSummation2;
    		this.sendSummation += sendSummation2;
    		String output = this.socketInfo.get(i)[0] + " sent " + String.valueOf(sendTracker) + " for a total of " + String.valueOf(sendSummation2) + " and recieved " + String.valueOf(recieveTracker) + " for a total of " + String.valueOf(recieveSummation2);
    		System.out.println(output);
    		din4.readInt();

    	}*/
        //String output = "Sent- " + String.valueOf(this.sendSummation) + " Recieved- " + String.valueOf(this.recieveSummation);
        //System.out.println(output);
        this.test();

    }



    public void test(){
        try {
            for(int i = 0; i < this.connections; i++){
                DataOutputStream dout5 = new DataOutputStream(this.nodes.get(i).getOutputStream());
                dout5.writeInt(0);

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
            //master.test();
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
