package cs455.overlay;

import java.io.DataInputStream;
import java.net.Socket;
import java.io.IOException;

public class TCPConnection extends Thread{

    public int port;
    public int received = 0;
    public Long receivedSummation = 0L;
    public Socket socket;

    public TCPConnection(Socket socket){
        this.socket = socket;
    }

    public void run(){
        try {
            DataInputStream newDin = new DataInputStream(this.socket.getInputStream());
            this.received = 0;
            this.receivedSummation = 0L;
            for(int i = 0; i < 5; i++){
                int temp = newDin.readInt();
                ++this.received;
                this.receivedSummation += temp;

            }
            Node.alterVars(this.received, this.receivedSummation);
            newDin.close();
            this.socket.close();

        }
        catch(IOException e){

        }
    }


}