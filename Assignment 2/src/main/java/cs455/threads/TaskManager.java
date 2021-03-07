package cs455.threads;

import cs455.threads.Task;
import java.util.ArrayList;
import java.util.*;

public class TaskManager{
    private List<Task> taskList = new ArrayList<Task>();
    private List<Task> synlist;


    public TaskManager(int dimensions){
        this.fillTaskList(dimensions);
        synlist = Collections.synchronizedList(taskList);
    }

    public void fillTaskList(int dimensions){
        for(int rows = 0; rows < dimensions; rows++) {
            for(int columns = 0; columns < dimensions; columns++) {
                taskList.add(new Task(0,1,columns,rows,4,columns,rows));
                taskList.add(new Task(2,3,rows,columns,5,rows,columns));
                this.size += 2;

            }
        }
        for(int rows = 0; rows < dimensions; rows++) {
            for(int columns = 0; columns < dimensions; columns++) {
                taskList.add(new Task(4,5,columns,rows,6,columns,rows));
                this.size++;
            }
        }


    }

    public void printTaskList(){
        for(int i = 0; i < this.taskList.size(); i++){
            this.taskList.get(i).printTask();
        }
    }


    public synchronized Task getNextTask(){
        Task p;
        synchronized (synlist) {
            if (synlist.isEmpty()) {
                p = null;
            } else {
                p = synlist.remove(0);
            }
        }
        if (p != null) {
            return p;
        }
        else{
            return new Task(-1);
        }

    }


}