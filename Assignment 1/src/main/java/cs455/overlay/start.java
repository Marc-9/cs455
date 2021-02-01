
package cs455.overlay;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.net.Socket;
import cs455.overlay.Collator;

public class start {

    public static void main(String[] args) {
        String hostname;

        try {
            hostname = InetAddress.getLocalHost().getHostName();
            if (!isCollator(hostname)) {
                try {
                    isNode(hostname);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static boolean isCollator(String hostname) {
        if (hostname.equals("sacramento")) {
            Collator master = new Collator(hostname, 8952);
            try {
                master.listenForRegistrations();
                master.startMessagePassing();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
            return true;

        } else
            return false;
    }

    public static void isNode(String hostname) throws IOException {
        Node messagingNode = new Node(hostname, 8952);
        try {
            Socket socket = new Socket("sacramento.cs.colostate.edu", 8952);
            OutputStream outputStream = socket.getOutputStream;
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(messagingNode);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

    }

}
