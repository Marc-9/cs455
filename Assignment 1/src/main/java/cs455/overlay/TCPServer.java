package cs455.overlay;

import java.net.ServerSocket;
import java.io.DataInputStream;
import java.net.Socket;
import java.io.IOException;
import java.util.Random;

public class TCPServer extends Thread{
    private ServerSocket serverSocket;
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
                // If the message is 0 that means all nodes have finsihed message passing and you should expect data coming in to the data input stream
                if(messageType == 0){
                    int nameLength = this.din.readInt();
                    byte[] identifierBytes = new byte[nameLength];
                    this.din.readFully(identifierBytes);
                    String socketName = new String(identifierBytes);
                    int sendTracker = this.din.readInt();
                    int recieveTracker = this.din.readInt();
                    this.recievedSummation = this.din.readLong();
                    this.sendSummation= this.din.readLong();
                    // Alter the static variables in collator, alterGlobals in a sychronized method in Collator so the lock for Collator must be obtained to alter its values
                    // This way several threads cant alter these values at the same time.
                    Collator.alterGlobals(sendTracker,recieveTracker, this.recievedSummation, this.sendSummation);

                    String output = socketName + " sent " + String.valueOf(sendTracker) + " for a total of " + String.valueOf(this.sendSummation) + " and received " + String.valueOf(recieveTracker) + " for a total of " + String.valueOf(this.recievedSummation);
                    System.out.println(output);
                }
                // A 0 was not sent meaning it was a 5, therfore this node that sent the 5 has finished sending messages but other nodes may not be finished, lets kill the thread
                
                break;

            }

        }
        catch(IOException e){

        }
    }



}