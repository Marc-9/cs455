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
            int index = ThreadsPool.masterReference.nextTaskInt();
            Task nextTask = ThreadsPool.masterReference.getNextTask(index);
            // If the next task has an array reference of 1 the task pool is empty and the thread should die
            if(nextTask.arrayReference1 == -1){
                ThreadsPool.allLatches[0].countDown();
                break;
            }
            else{
            	// Once we get a task to calculate a component in Z start its timer
                if(nextTask.outputReference == 6){
                    MatrixThreads.zMatrix.startTimer.countDown();
                }
                int val = 0;

                // Do the maths
                for(int i = 0; i < this.dimensions; i++){
                    val += ThreadsPool.matrixReferences[nextTask.arrayReference1].matrixValues[nextTask.rowNum][i] * ThreadsPool.matrixReferences[nextTask.arrayReference2].matrixValues[i][nextTask.colNum];
                }
                // Set the maths
                ThreadsPool.matrixReferences[nextTask.outputReference].matrixValues[nextTask.rowOutput][nextTask.colOutput] = val;

                // We have done 1 task for the matrix and are 1 task closer to stopping its timer.
                ThreadsPool.allLatches[nextTask.outputReference-3].countDown();
                
            }
        }

    }

}