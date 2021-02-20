#!/bin/bash

USERNAME=frenchy9
HOSTS="albany.cs.colostate.edu cod.cs.colostate.edu annapolis.cs.colostate.edu atlanta.cs.colostate.edu augusta.cs.colostate.edu jackson.cs.colostate.edu olympia.cs.colostate.edu  phoenix.cs.colostate.edu richmond.cs.colostate.edu"
SCRIPT="cd cs455/Assignment\ 1/src/main/java;
make client;"

for HOSTNAME in ${HOSTS} ; do
	ssh -l ${USERNAME} ${HOSTNAME} "${SCRIPT}"
done
