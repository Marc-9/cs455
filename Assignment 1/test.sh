#!/bin/bash

USERNAME=frenchy9
HOSTS="albany.cs.colostate.edu swordfish.cs.colostate.edu"
SCRIPT="cd cs455/Assignment\ 1/src/main/java;
make client;"

for HOSTNAME in ${HOSTS} ; do
	ssh -l ${USERNAME} ${HOSTNAME} "${SCRIPT}"
done
