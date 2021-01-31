package cs455.overlay.node;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.net.ServerSocket;
import java.net.Socket;
import cs455.overlay.node.Node;

public class Collator {
    private Socket socket;
    private ServerSocket serverSocket;
    private DataOutputStream dout;
    private DataInputStream din;
    public int portNumber;
    public String hostName;
    public int connections = 0;

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
        while (this.connections < 11) {
            this.socket = this.serverSocket.accept();
            this.din = new DataInputStream(this.socket.getInputStream());
        }

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
