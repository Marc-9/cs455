SRCPATH = src/main/java/cs455/overlay
FILES = $(SRCPATH)/node/*.java $(SRCPATH)/util/*.java $(SRCPATH)/*.java
all:
	javac $(FILES)
run:
	java $(SRCPATH)/start.java
clean:
	rm *.class
package:
	zip -r Ruffalo-Burgat-Jean-Marc-HW1.zip $(FILES) $(SRCPATH)/node/machines.txt Makefile
