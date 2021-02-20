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
		boolean initializeCollator = true;
		String name;
		String collatorHostName = "";
		int portNum = 0;
		int totHosts = 0;
		int collatorPort = 0;
		boolean isCollator = false;
		try{
			name = InetAddress.getLocalHost().getHostName() + ".cs.colostate.edu";



			File myObj = new File("machines.txt");
			Scanner myReader = new Scanner(myObj);
			String[][] machineNames = new String[10][2];

			while (myReader.hasNextLine()) {
				// Only to be run on first line to determine if current node is the Collator
				if(initializeCollator){
					initializeCollator = false;
					String[] data = myReader.nextLine().split(",",2);

					/* If your hostname and the first line in the files hostname match you become the collator
					 *  this means however the first machine to run the jar file must be the collator machine i.e the first machine in machines.txt
					 *  an ok approach if a script is used but could run into issues if you run it manually and forget this
					 */
					if(data[0].equals(name)){
						isCollator = true;
						portNum = Integer.parseInt(data[1]);
						collatorHostName = name;
						collatorPort = portNum;

					}
					else{
						isCollator = false;
						collatorHostName = data[0];
						collatorPort = Integer.parseInt(data[1]);
						++totHosts;
					}
				}
				else{
					String[] data = myReader.nextLine().split(",",2);
					if(isCollator){
						++totHosts;
					}
					else{
						if(data[0].equals(name)){
							portNum = Integer.parseInt(data[1]);
						}
						else{

							Node.hostNames.add(data);
						}
					}
				}
			}
			myReader.close();


			if(isCollator){
				Collator master = new Collator(name, portNum, totHosts);
				master.listenForRegistrations();
				master.startMessagePassing();
				master.test();
			}
			else{
				Node node = new Node(name, portNum, collatorHostName, collatorPort);
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