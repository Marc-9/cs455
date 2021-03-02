package cs455.threads;

import cs455.threads.Task;
import java.util.ArrayList;

public class TaskManager{
    private ArrayList<Task> taskList = new ArrayList<Task>();

    public TaskManager(int dimensions){
        this.fillTaskList(dimensions);
    }

    public void fillTaskList(int dimensions){
        for(int i = 0; i < dimensions; i++) {
            for(int j = 0; j < dimensions; j++) {
                taskList.add(new Task(0,1,i,-1,-1,j));
            }
        }
        for(int i = 0; i < dimensions; i++) {
            for(int j = 0; j < dimensions; j++) {
                taskList.add(new Task(2,3,-1,j,i,-1));
            }
        }

    }

    public void printTaskList(){
        for(int i = 0; i < this.taskList.size(); i++){
            this.taskList.get(i).printTask();
        }
    }

    public synchronized void addTask(Task newTask){
        taskList.add(newTask);
    }

    public synchronized Task getNextTask(){
        // ArrayList pop method?
        return taskList.get(0);
    }


}