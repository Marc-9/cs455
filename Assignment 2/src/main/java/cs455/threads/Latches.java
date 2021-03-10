package cs455.threads;
import cs455.threads.Matrix;
import java.util.concurrent.CountDownLatch;

public class Latches extends Thread{
	public CountDownLatch latch;
	public Matrix mRef;
	public CountDownLatch startTimer = new CountDownLatch(1);

	public Latches(CountDownLatch latch, Matrix matrixReference){
		this.latch = latch;
		this.mRef = matrixReference;
	}

	public void run(){
		try{
			this.startTimer.await();
			long startTime = System.currentTimeMillis();
			this.latch.await();
			this.mRef.printCalculation();
			long endTime = System.currentTimeMillis();
        	long duration = (endTime - startTime);
        	System.out.println("Time to compute matrix " + this.mRef.name + ": " + duration/1000F + "s");
        	MatrixThreads.masterLatch.countDown();

        }
        catch(Exception e){
        	System.out.println(e);
        }
	}

}