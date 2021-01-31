
package cs455.overlay;

import java.net.InetAddress;
import java.net.UnknownHostException;
import cs455.overlay.node.Collator;
import cs455.overlay.node.Node;

public class start {

    public static void main(String[] args) {
        String hostname;
        try {
            hostname = InetAddress.getLocalHost().getHostName();
            if (!isCollator(hostname)) {
                isNode(hostname);
            }

        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static boolean isCollator(String hostname) {
        if (hostname.equals("sacramento")) {
            Collator master = new Collator(hostname, 8952);
            return true;

        } else
            return false;
    }

    public static void isNode(String hostname) {
        Node messagingNode = new Node(hostname, 8952);
    }

}
