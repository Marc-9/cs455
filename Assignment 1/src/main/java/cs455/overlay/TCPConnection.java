package cs455.overlay;

import java.net.ServerSocket;
import java.io.DataInputStream;
import java.net.Socket;
import java.io.IOException;
import java.util.Random;

public class TCPConnection extends Thread{
    private ServerSocket serverSocket;
    //private Socket socket;
    public int port;
    public boolean cycle = true;

    public TCPConnection(int port){
        this.port = port;
    }

    public void setCycle(){
        this.cycle = false;
    }

    public void run(){
        try {
            this.serverSocket = new ServerSocket(this.port, 10);
            Socket socket;
            //Random rand = new Random();
            //int randInt = 0;
            while(cycle){
                socket = this.serverSocket.accept();
                DataInputStream newDin = new DataInputStream(socket.getInputStream());
                for(int i = 0; i < 5; i++){
                    int temp = newDin.readInt();
                    System.out.println("Recieved the number " + temp);
                }
                newDin.close();
                socket.close();
            }

        }
        catch(IOException e){

        }
    }


}