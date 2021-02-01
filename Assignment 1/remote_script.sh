#!/bin/bash

USERNAME=frenchy9
HOSTS="sacramento.cs.colostate.edu albany.cs.colostate.edu atlanta.cs.colostate.edu madison.cs.colostate.edu concord.cs.colostate.edu boise.cs.colostate.edu phoenix.cs.colostate.edu salem.cs.colostate.edu topeka.cs.colostate.edu bangkok.cs.colostate.edu bejing.cs.colostate.edu"
SCRIPT "cd cs455/Assignment\ 1;
make run"
for HOSTNAME in ${HOSTS} ; do
	ssh -l ${USERNAME} ${HOSTNAME} "${SCRIPT}"
done
