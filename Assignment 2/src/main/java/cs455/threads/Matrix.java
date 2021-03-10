package cs455.threads;
import java.util.Random;



public class Matrix{
    static int dimensions;
    static Random generator = new Random();
    public int[][] matrixValues;
    public char name;

    public Matrix(char name){
        this.matrixValues = new int[Matrix.dimensions][Matrix.dimensions];
        this.name = name;
    }

    public void fillMatrix(){
        for(int i = 0; i < Matrix.dimensions; i++){
            for(int j = 0; j < Matrix.dimensions; j++){
                this.matrixValues[i][j] = generator.nextInt();
            }
        }
    }

    public void printSum(){
        int sum = 0;
        for(int i = 0; i < Matrix.dimensions; i++){
            for(int j = 0; j < Matrix.dimensions; j++){
                sum += this.matrixValues[i][j];
            }
        }
        System.out.println("Sum of the elements in input matrix " + this.name + " = " + sum);
    }

    public void printCalculation(){
        int sum = 0;
        for(int i = 0; i < Matrix.dimensions; i++){
            for(int j = 0; j < Matrix.dimensions; j++){
                sum += this.matrixValues[i][j];
            }
        }
        switch(this.name){
        	case 'X':
        		this.printSpecific('A','B', sum);
        		break;
        	case 'Y':
        		this.printSpecific('C','D', sum);
        		break;
        	case 'Z':
        		this.printSpecific('X','Y', sum);
        }
    }

    public void printSpecific(char matrix1, char matrix2, int sum){
    	System.out.println("Calculation of the matrix " + this.name + " (product of " + matrix1 + " and " + matrix2 + " ) complete - sum of the elements in " + this.name + 
                " is: " + sum);
    }

    public void printMatrix(){
        for(int[] row : this.matrixValues) {
            printRow(row);
        }
        System.out.println();
    }
    
    public void printRow(int[] row) {
        for (int i : row) {
            System.out.print(i);
            System.out.print(",");
        }
        System.out.println();

    }


}