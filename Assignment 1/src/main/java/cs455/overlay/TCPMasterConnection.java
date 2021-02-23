package cs455.overlay;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;

public class TCPMasterConnection extends Thread{
    private ServerSocket serverSocket;
    public int port;

    public TCPMasterConnection(int port){
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
            this.serverSocket = new ServerSocket(this.port, 10);
            while(true){
                Socket socket = this.serverSocket.accept();
                TCPConnection temp = new TCPConnection(socket);
                temp.start();
            }

        }
        catch(IOException e){

        }
    }


}