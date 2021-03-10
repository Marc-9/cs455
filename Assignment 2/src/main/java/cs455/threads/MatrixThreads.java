package cs455.threads;
import cs455.threads.Matrix;
import cs455.threads.TaskManager;
import cs455.threads.ThreadsPool;
import cs455.threads.Latches;
import java.util.concurrent.CountDownLatch;

public class MatrixThreads{
    static Latches zMatrix;
    static CountDownLatch masterLatch;

    public static void main(String[] args){
    	// All matrixes share the same dimensions and same seed for generating random integers
        Matrix.dimensions = Integer.parseInt(args[1]);
        Matrix.generator.setSeed(Integer.parseInt(args[2]));

        // Create the matrixes A,B,C,D and fill their contents
        Matrix A = new Matrix('A');
        A.fillMatrix();

        Matrix B = new Matrix('B');
        B.fillMatrix();

        Matrix C = new Matrix('C');
        C.fillMatrix();

        Matrix D = new Matrix('D');
        D.fillMatrix();


        // Create the output Matrixes X,Y,Z
        Matrix X = new Matrix('X');
        Matrix Y = new Matrix('Y');
        Matrix Z = new Matrix('Z');

        // Used to pass the references of the matrixes to the threads
        Matrix[] matrixReferences = new Matrix[]{A,B,C,D,X,Y,Z};

        // This will generate the ArrayList that holds all the tasks that must be completed
        TaskManager master = new TaskManager(Matrix.dimensions);

        // The master latch that will finish when all threads have finished their tasks
        masterLatch = new CountDownLatch(Integer.parseInt(args[0])+3);

        // The following latches are used to time how long the calculation of these matrixes take
        CountDownLatch xMatrixLatch = new CountDownLatch((int)Math.pow(Matrix.dimensions, 2));
        Latches xMatrix = new Latches(xMatrixLatch, X);

        CountDownLatch yMatrixLatch = new CountDownLatch((int)Math.pow(Matrix.dimensions, 2));
        Latches yMatrix = new Latches(yMatrixLatch, Y);

        CountDownLatch zMatrixLatch = new CountDownLatch((int)Math.pow(Matrix.dimensions, 2));
        zMatrix = new Latches(zMatrixLatch, Z);


        CountDownLatch[] allLatches = new CountDownLatch[]{masterLatch,xMatrixLatch,yMatrixLatch,zMatrixLatch};


        // Intialize the thread pool with all the references needed
        ThreadsPool threadPool = new ThreadsPool(Integer.parseInt(args[0]), matrixReferences, master, allLatches);

        // Start the printing
        System.out.println("The thread pool size has been initialized to: " + Integer.parseInt(args[0]));
        A.printSum();
        B.printSum();
        C.printSum();
        D.printSum();

        // Start the latches thread and the timers
        xMatrix.start();
        yMatrix.start();
        zMatrix.start();
        xMatrix.startTimer.countDown();
        yMatrix.startTimer.countDown();

        long startTime = System.currentTimeMillis();
        threadPool.startWorkers();
        try{
            masterLatch.await();
            long endTime = System.currentTimeMillis();
            long duration = (endTime - startTime);
            System.out.println("Cumulative time to compute matrixes X, Y, and Z using a thread pool of size = " + Integer.parseInt(args[0]) + " is: " + duration/1000F + "s");
        }
        catch(Exception e){

        }



    }

}
