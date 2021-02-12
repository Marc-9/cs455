package cs455.overlay;

import java.net.ServerSocket;
import java.io.DataInputStream;
import java.net.Socket;
import java.io.IOException;
import java.util.Random;

public class TCPServer extends Thread{
    private ServerSocket serverSocket;
    //private Socket socket;
    public DataInputStream din;
    public boolean cycle = true;
    public Long recievedSummation = 0L;
    public Long sendSummation = 0L;



    public TCPServer(DataInputStream din){
        this.din = din;

    }


    public void run(){
        try {
            while(cycle){
                int messageType = this.din.readInt();
                if(messageType == 0){
                    int nameLength = this.din.readInt();
                    byte[] identifierBytes = new byte[nameLength];
                    this.din.readFully(identifierBytes);
                    String socketName = new String(identifierBytes);
                    int sendTracker = this.din.readInt();
                    int recieveTracker = this.din.readInt();
                    this.recievedSummation = this.din.readLong();
                    this.sendSummation= this.din.readLong();
                    this.alterGlobals();

                    String output = socketName + " sent " + String.valueOf(sendTracker) + " for a total of " + String.valueOf(this.sendSummation) + " and recieved " + String.valueOf(recieveTracker) + " for a total of " + String.valueOf(this.recievedSummation);
                    System.out.println(output);
                }
                break;
                
            }

        }
        catch(IOException e){

        }
    }

    public synchronized void alterGlobals(){
        Collator.recievedSummation += this.recievedSummation;
        Collator.sendSummation += this.sendSummation;
    }


}