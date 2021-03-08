package cs455.threads;
import cs455.threads.Matrix;
import cs455.threads.TaskManager;
import cs455.threads.ThreadsPool;
import cs455.threads.Latches;
import java.util.concurrent.CountDownLatch;

public class MatrixThreads{
    static Latches zMatrix;

    public static void main(String[] args){
        Matrix.dimensions = Integer.parseInt(args[1]);
        Matrix.generator.setSeed(Integer.parseInt(args[2]));
        Matrix A = new Matrix('A');
        A.fillMatrix();
       //A.printMatrix();

        Matrix B = new Matrix('B');
        B.fillMatrix();
       // B.printMatrix();
        Matrix C = new Matrix('C');
        C.fillMatrix();
        //C.printMatrix();
        Matrix D = new Matrix('D');
        D.fillMatrix();
        //D.printMatrix();
        Matrix X = new Matrix('X');
        Matrix Y = new Matrix('Y');
        Matrix Z = new Matrix('Z');
        Matrix[] matrixReferences = new Matrix[]{A,B,C,D,X,Y,Z};
        TaskManager master = new TaskManager(Matrix.dimensions);
        CountDownLatch latch = new CountDownLatch(Integer.parseInt(args[0]));

        CountDownLatch latch2 = new CountDownLatch((int)Math.pow(Matrix.dimensions, 2));
        Latches xMatrix = new Latches(latch2, X);
        CountDownLatch latch3 = new CountDownLatch((int)Math.pow(Matrix.dimensions, 2));
        Latches yMatrix = new Latches(latch3, Y);
        CountDownLatch latch4 = new CountDownLatch((int)Math.pow(Matrix.dimensions, 2));
        zMatrix = new Latches(latch4, Z);


        ThreadsPool threadPool = new ThreadsPool(Integer.parseInt(args[0]), matrixReferences, master, latch, latch2, latch3, latch4);
        System.out.println("The thread pool size has been initialized to: " + Integer.parseInt(args[0]));
        A.printSum();
        B.printSum();
        C.printSum();
        D.printSum();
        xMatrix.start();
        yMatrix.start();
        zMatrix.start();
        xMatrix.startTimer.countDown();
        yMatrix.startTimer.countDown();

        long startTime = System.currentTimeMillis();
        threadPool.startWorkers();
        try{
            latch.await();
            long endTime = System.currentTimeMillis();
            long duration = (endTime - startTime);
            System.out.println("Cumulative time to compute matrixes X, Y, and Z using a thread pool of size =  " + Integer.parseInt(args[0]) + " is: " + duration/1000F + "s");
        }
        catch(Exception e){

        }

       // X.printMatrix();
        //Y.printMatrix();
        //Z.printMatrix();



    }

}
