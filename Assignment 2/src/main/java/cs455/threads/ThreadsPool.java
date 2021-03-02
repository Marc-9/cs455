package cs455.threads;

import cs455.threads.ThreadWorker;

import java.util.ArrayList;

public class ThreadPool{
    private int poolSize;
    private ArrayList<ThreadWorker> workerThreads = new ArrayList<ThreadWorker>();

    public ThreadPool(int maxPoolSize){
        this.poolSize = maxPoolSize;
        for(int i = 0; i < maxPoolSize; i++){
            workerThreads.add(new ThreadWorker());
        }
    }


}