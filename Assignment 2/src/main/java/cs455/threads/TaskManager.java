package cs455.threads;

import cs455.threads.Task;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class TaskManager{

    private ArrayList<Task> taskList = new ArrayList<Task>();
    public AtomicInteger nextTask = new AtomicInteger(0);
    
    public TaskManager(int dimensions){
        this.fillTaskList(dimensions);
    }

    // Fill the task list, starts by calculating the rows in X and the columns in Y so that Z can start very soon
    public void fillTaskList(int dimensions){
        for(int rows = 0; rows < dimensions; rows++) {
            for(int columns = 0; columns < dimensions; columns++) {
                taskList.add(new Task(0,1,columns,rows,4,columns,rows));
                taskList.add(new Task(2,3,rows,columns,5,rows,columns));
            }
        }
        for(int rows = 0; rows < dimensions; rows++) {
            for(int columns = 0; columns < dimensions; columns++) {
                taskList.add(new Task(4,5,columns,rows,6,columns,rows));
                
            }
        }

    }

    public void printTaskList(){
        for(int i = 0; i < this.taskList.size(); i++){
            this.taskList.get(i).printTask();
        }
    }

    // For some reason if incrementing our Atomic int is not synchronized I get a race condition, who knows?
    // Just removed the synchronized and I seemingly have no RC.
    public int nextTaskInt(){
    	int rds = nextTask.getAndIncrement();
    	return rds;
    }


    public Task getNextTask(int index){
    	if(index >= taskList.size()){
    		return new Task(-1);
    	}
    	else{
    		return taskList.get(index);
    	}

    }


}