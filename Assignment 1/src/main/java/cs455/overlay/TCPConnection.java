package cs455.overlay;

import java.net.ServerSocket;
import java.io.DataInputStream;
import java.net.Socket;
import java.io.IOException;
import java.util.Random;

public class TCPConnection extends Thread{
    private ServerSocket serverSocket;

    public int port;
    public boolean cycle = true;
    public int received = 0;
    public Long receivedSummation = 0L;

    public TCPConnection(int port){
        //System.out.println("here");
        this.port = port;
    }

    public void interrupt(){
        try{
            this.serverSocket.close();
        }
        catch(IOException e){

        }
    }

    public void run(){
        try {
            //System.out.println("here");
            this.serverSocket = new ServerSocket(this.port, 10);
            while(true){
                Socket socket = this.serverSocket.accept();
                DataInputStream newDin = new DataInputStream(socket.getInputStream());
                for(int i = 0; i < 5; i++){

                    int temp = newDin.readInt();
                    ++this.received;
                    this.receivedSummation += temp;
                }
                newDin.close();
                socket.close();
            }

        }
        catch(IOException e){

        }
    }


}