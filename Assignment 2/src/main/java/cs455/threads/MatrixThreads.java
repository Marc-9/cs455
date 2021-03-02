package cs455.threads;
import cs455.threads.Matrix;
import cs455.threads.TaskManager;

public class MatrixThreads{

    public static void main(String[] args){
        Matrix.dimensions = Integer.parseInt(args[1]);
        Matrix A = new Matrix();
        A.fillMatrix();
        Matrix B = new Matrix();
        B.fillMatrix();
        Matrix C = new Matrix();
        C.fillMatrix();
        Matrix D = new Matrix();
        D.fillMatrix();
        TaskManager master = new TaskManager(Matrix.dimensions);
        master.printTaskList();

    }

}
