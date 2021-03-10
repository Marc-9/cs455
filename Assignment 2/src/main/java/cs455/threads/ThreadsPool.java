package cs455.threads;

import cs455.threads.ThreadWorker;
import cs455.threads.Matrix;
import cs455.threads.TaskManager;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

import java.util.ArrayList;

public class ThreadsPool{
    private int poolSize;
    private ArrayList<ThreadWorker> workerThreads = new ArrayList<ThreadWorker>();
    // Static because life is easier when I dont pass a million references
    static Matrix[] matrixReferences;
    static TaskManager masterReference;
    static CountDownLatch[] allLatches;
    static AtomicBoolean zStart = new AtomicBoolean();

   public ThreadsPool(int maxPoolSize, Matrix[] matrixReferences, TaskManager master, CountDownLatch[] allLatches){ 
        this.poolSize = maxPoolSize;
        this.matrixReferences = matrixReferences;
        this.masterReference = master;
        this.allLatches = allLatches;
        for(int i = 0; i < maxPoolSize; i++){
            workerThreads.add(new ThreadWorker(Matrix.dimensions));
        }
    }

    public void startWorkers(){
        for(int i = 0; i < this.poolSize; i++){
            workerThreads.get(i).start();
        }
    }



}