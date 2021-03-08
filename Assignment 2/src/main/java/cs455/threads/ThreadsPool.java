package cs455.threads;

import cs455.threads.ThreadWorker;
import cs455.threads.Matrix;
import cs455.threads.TaskManager;
import java.util.concurrent.CountDownLatch;

import java.util.ArrayList;

public class ThreadsPool{
    private int poolSize;
    private ArrayList<ThreadWorker> workerThreads = new ArrayList<ThreadWorker>();
    static Matrix[] matrixReferences;
    static TaskManager masterReference;
    static CountDownLatch latch;
    static CountDownLatch latch2;
    static CountDownLatch latch3;

    public ThreadsPool(int maxPoolSize, Matrix[] matrixReferences, TaskManager master, CountDownLatch latch, CountDownLatch latch2, CountDownLatch latch3){
        this.poolSize = maxPoolSize;
        this.matrixReferences = matrixReferences;
        this.masterReference = master;
        this.latch = latch;
        this.latch2 = latch2;
        this.latch3 = latch3;
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