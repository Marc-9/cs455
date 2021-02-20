# CS455 Distributed Systems


## Assignment 1

- TCP Connections over the internet between a variable number of nodes
- Nodes randomly initiate connections with other nodes in the network and send random integers
- Correctly recieve and read marshalled data and close the connection
- Nodes keep track of how many messages they have sent and received and the sums of these two numbers
- Institute threading such that multiple connections can be made at once to optimize the network
- At the end the # of Messages sent and Messages recieved as well as the Sum send and Sum recieved should be equal
- Implement safe threading procedures with locks to prevent race conditions

Below is a working demo of the program, for ease of use a script can be utlized to ssh into each machine and start the jar file.

![hw1demo](demo/hw1.gif)

