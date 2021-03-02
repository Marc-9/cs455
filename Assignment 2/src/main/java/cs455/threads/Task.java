package cs455.threads;

public class Task{
    public int arrayReference1;
    public int arrayReference2;
    public int columnReference1;
    public int rowReference1;
    public int columnReference2;
    public int rowReference2;

    public Task(int arrayReference1, int arrayReference2, int columnReference1, int rowReference1, int columnReference2, int rowReference2){
        this.arrayReference1 = arrayReference1;
        this.arrayReference2 = arrayReference2;
        this.columnReference1 = columnReference1;
        this.rowReference1 = rowReference1;
        this.columnReference2 = columnReference2;
        this.rowReference2 = rowReference2;
    }

    public void printTask(){
        // Forgot how to do simple java array inline initilization**
        char[] temp = new char[4];
        temp[0] = 'A';
        temp[1] = 'B';
        temp[2] = 'C';
        temp[3] = 'D';
        System.out.println("Task between matrixes " + temp[this.arrayReference1] + " and " + temp[this.arrayReference2] +
                " column " + this.columnReference1 + " * " + this.columnReference2 + " and row " + this.rowReference1 + " * " + this.rowReference2);
    }
}