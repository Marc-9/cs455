FILES = cs455/overlay/*.java
all:
	javac $(FILES)
run:
	java cs455/overlay/Start
server:
	java cs455/overlay/Collator
client:
	java cs455/overlay/Node
clean:
	rm *.class
package:
	zip -r Ruffalo-Burgat-Jean-Marc-HW1.zip $(FILES) machines.txt Makefile
