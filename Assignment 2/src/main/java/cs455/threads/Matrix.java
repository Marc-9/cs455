package cs455.threads;
import java.util.Random;


public class Matrix{
    static int dimensions;
    static Random generator = new Random();
    public int[][] matrixValues;

    public Matrix(){
        this.matrixValues = new int[Matrix.dimensions][Matrix.dimensions];
    }

    public void fillMatrix(){
        for(int i = 0; i < Matrix.dimensions; i++){
            for(int j = i; j < Matrix.dimensions; j++){
                this.matrixValues[i][j] = generator.nextInt();
            }
        }
    }

}