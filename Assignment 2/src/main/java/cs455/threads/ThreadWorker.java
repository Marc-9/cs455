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
            if(nextTask.arrayReference1 == -1){
                ThreadsPool.latch.countDown();
                break;
            }
            else{
                if(nextTask.outputReference == 6){
                    MatrixThreads.zMatrix.startTimer.countDown();
                }
                int val = 0;

                for(int i = 0; i < this.dimensions; i++){
                    val += ThreadsPool.matrixReferences[nextTask.arrayReference1].matrixValues[nextTask.rowNum][i] * ThreadsPool.matrixReferences[nextTask.arrayReference2].matrixValues[i][nextTask.colNum];
                }
                ThreadsPool.matrixReferences[nextTask.outputReference].matrixValues[nextTask.rowOutput][nextTask.colOutput] = val;
                if(nextTask.outputReference == 4){
                    ThreadsPool.latch2.countDown();
                }
                if(nextTask.outputReference == 5){
                    ThreadsPool.latch3.countDown();
                }
                if(nextTask.outputReference == 6){
                    ThreadsPool.latch4.countDown();
                }
            }
        }

    }

}