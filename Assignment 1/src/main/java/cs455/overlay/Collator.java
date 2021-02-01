package cs455.overlay;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Collator {
    private Socket socket;
    private ServerSocket serverSocket;
    private DataOutputStream dout;
    private DataInputStream din;
    public int portNumber;
    public String hostName;
    public int connections = 0;
    public int totRounds = 0;
    public ArrayList<Node> nodes = new ArrayList<Node>();

    public Collator(String hostName, int portNumber) {
        this.hostName = hostName;
        this.portNumber = portNumber;
        try {
            this.serverSocket = new ServerSocket(this.portNumber, 10);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void listenForRegistrations() throws IOException {
        while (this.connections == 0 || (this.totRounds / 25000) < this.connections) {
            this.socket = this.serverSocket.accept();
            this.din = this.socket.getInputStream();
            ObjectInputStream objectInputStream = new ObjectInputStream(this.din);
            this.nodes.add(objectInputStream.readObject());
            //this.din = new DataInputStream(this.socket.getInputStream());
            //int nameLength = din.readInt();
            //byte[] identifierBytes = new byte[nameLength];
            //din.readFully(identifierBytes);
            this.connections++;
            //this.nodes.add(new String(identifierBytes));
            //System.out.println(new String(identifierBytes));
            this.socket.close();
            this.din.close();
            if(this.connections >= 2){
                this.startMessagePassing();
            }
        }

       

    }

    public void startMessagePassing(){
        for(int i = 0; i < this.connections; i++){
            if(this.nodes[i].sendTracker < 25000){
                int recieverIndex = this.randomNumber(this.connections, i);
                this.nodes[i].sender();
                this.nodes[recieverIndex].reciever(this.nodes[i].hostName, 8952);
                this.totRounds += 25000;
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
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader("machines.txt"));
            Collator master = new Collator(reader.readLine(), 8952);
            String line = reader.readLine();
            while (line != null) {

            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
