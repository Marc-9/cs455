package cs455.threads;

public class Task{
    public int arrayReference1;
    public int arrayReference2;
    public int outputReference;
    public int colNum;
    public int rowNum;
    public int rowOutput;
    public int colOutput;

    public Task(int arrayReference1){
        this.arrayReference1 = arrayReference1;
    }

    public Task(int arrayReference1, int arrayReference2, int colNum, int rowNum, int outputReference, int colOutput, int rowOutput){
        this.arrayReference1 = arrayReference1;
        this.arrayReference2 = arrayReference2;
        this.outputReference = outputReference;
        this.colNum = colNum;
        this.rowNum = rowNum;
        this.colOutput = colOutput;
        this.rowOutput = rowOutput;
    }

    public void printTask(){
        // Forgot how to do simple java array inline initilization**
        char[] temp = new char[]{'A', 'B', 'C', 'D', 'X', 'Y', 'Z'};
        System.out.println("Row " + this.rowNum + " in Matrix " + temp[this.arrayReference1] + " * Column " + this.colNum
        + " in Matrix " + temp[this.arrayReference2] + " to set column " + this.colOutput + " and row " + this.rowOutput
        + " in Matrix " + temp[this.outputReference]);
    }
}