package cs455.threads;
import cs455.threads.Matrix;
import cs455.threads.TaskManager;
import cs455.threads.ThreadsPool;
import java.util.concurrent.CountDownLatch;

public class MatrixThreads{

    public static void main(String[] args){
        long startTime = System.nanoTime();
        Matrix.dimensions = Integer.parseInt(args[1]);
        Matrix.generator.setSeed(Integer.parseInt(args[2]));
        Matrix A = new Matrix();
        A.fillMatrix();
        //A.printMatrix();

        Matrix B = new Matrix();
        B.fillMatrix();
        //B.printMatrix();
        Matrix C = new Matrix();
        C.fillMatrix();
       // C.printMatrix();
        Matrix D = new Matrix();
        D.fillMatrix();
       // D.printMatrix();
        Matrix X = new Matrix();
        Matrix Y = new Matrix();
        Matrix Z = new Matrix();
        Matrix[] matrixReferences = new Matrix[]{A,B,C,D,X,Y,Z};
        TaskManager master = new TaskManager(Matrix.dimensions);
        CountDownLatch latch = new CountDownLatch(Integer.parseInt(args[0]));

        ThreadsPool threadPool = new ThreadsPool(Integer.parseInt(args[0]), matrixReferences, master, latch);

        threadPool.startWorkers();
        try{
            latch.await();
            long endTime = System.nanoTime();
            long duration = (endTime - startTime);
            int sum = 0;
            for(int i = 0; i < Matrix.dimensions; i++){
                for(int j = 0; j < Matrix.dimensions; j++){
                    sum += Z.matrixValues[i][j];
                }
            }
            System.out.println(sum);
            System.out.println("Duration " + duration/1000000);
        }
        catch(Exception e){

        }

       // X.printMatrix();
        //Y.printMatrix();
        //Z.printMatrix();



    }

}
