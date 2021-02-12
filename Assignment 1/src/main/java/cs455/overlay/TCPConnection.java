package cs455.overlay;

import java.net.ServerSocket;
import java.io.DataInputStream;
import java.net.Socket;

public class TCPConnection() extends Thread{
    private ServerSocket serverSocket;
    //private Socket socket;
    public int port;

    public void run(){
        try {
            this.serverSocket = new ServerSocket(this.port, 10);

            while(Socket socket = this.serverSocket.accept()){
                DataOutputStream newDout = new DataOutputStream(socket.getOutputStream());
                for(int i = 0; i < 5; i++){
                    randInt = rand.nextInt();
                    newDout.writeInt(randInt);
                }
                newDout.close();
                socket.close();
            }

        }
    }


}