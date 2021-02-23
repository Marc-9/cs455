How to run
	*IMPORTANT* 
	It is mandatory that the machines.txt file be in the SAME directory has the jar file when you run it, it is also required that the first node you start correspond to the first node in your machines.txt. As that node becomes the Collator
	You may run the pre-built jar file or you may re build it yourself using gradle build. Of course it is then recommended you replace the current jar file with the newly generates one in /build/libs


Files
-build.gradle
	* The file that handles the gradle build so that a runnable jar file can be created
-machines.txt
	* The required text file that contains the comma delimited machines to run, must be [hostname,port] no spaces inbetween and full .cs.colostate.edu names
-Assignment 1.jar
	* The runnable jar file to start the node

[In src/main/java/cs455/overlay]
-Start
	* This is the file that gradle builds the jar off of. It will read the nodes in machines.txt and then start the collator and the message passing
-Collator
	* The collator handles all the nodes, it tells them when to start message passing and when to end

-Node
	* The nodes report to the collator and do message passing, they randomly decide which node to connect to and what random into to send them

-TCPServer
	* A listener thread so the collator can know when Nodes have finished and get their reports on how much they sent and recieved

-TCPConnection
	* A listener thread so that nodes may connect to other nodes and receive the 5 messages of random ints they send

-TCPMasterConnection
	* Now we have a thread just for the server socket, it then spins off a new thread for each connection