package cs455.overlay;

import cs455.overlay.Node;
import cs455.overlay.Collator;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.net.InetAddress;
import java.io.File;
import java.io.FileReader;
import java.util.Scanner;
import java.net.UnknownHostException;

public class Start{

	public static void main(String[] args){
		String name;
		int portNum = 0;
		int totHosts = 0;
		boolean isCollator;
		try{
			name = InetAddress.getLocalHost().getHostName() + ".cs.colostate.edu";
			if(name.equals("sacramento.cs.colostate.edu")){
				isCollator = true;
			}
			else{
				isCollator = false;
			}

			File myObj = new File("machines.txt");
            Scanner myReader = new Scanner(myObj);
            String[][] machineNames = new String[10][2];

            while (myReader.hasNextLine()) {
            	String[] data = myReader.nextLine().split(",",2);
            	if(data[0].equals(name)){
                    portNum = Integer.parseInt(data[1]);
                    ++totHosts;
                }
                else if(data[0].equals("sacramento.cs.colostate.edu")){
                	continue;
                }
                else{
                	Node.hostNames.add(data);
                	++totHosts;
                }
            }
            --totHosts;
            myReader.close();


            if(isCollator){
            	Collator master = new Collator(name, portNum, totHosts);
            	master.listenForRegistrations();
            	master.startMessagePassing();
            	master.test();
            }
            else{
            	Node node = new Node(name, portNum);
            	node.register();
            }

		} catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
	}
}