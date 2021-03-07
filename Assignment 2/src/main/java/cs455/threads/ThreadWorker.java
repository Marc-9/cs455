package cs455.threads;
import cs455.threads.ThreadsPool;
import cs455.threads.Task;

public class ThreadWorker extends Thread{
    public int dimensions;

    public ThreadWorker(int dimensions){
        this.dimensions = dimensions;
    }

    public void run(){
        while(true){
            Task nextTask = ThreadsPool.masterReference.getNextTask();
            if(nextTask.arrayReference1 == -1){
                ThreadsPool.latch.countDown();
                break;
            }
            else{

                int val = 0;

                for(int i = 0; i < this.dimensions; i++){
                    val += ThreadsPool.matrixReferences[nextTask.arrayReference1].matrixValues[nextTask.rowNum][i] * ThreadsPool.matrixReferences[nextTask.arrayReference2].matrixValues[i][nextTask.colNum];
                }
                ThreadsPool.matrixReferences[nextTask.outputReference].matrixValues[nextTask.rowOutput][nextTask.colOutput] = val;
            }
        }

    }

}