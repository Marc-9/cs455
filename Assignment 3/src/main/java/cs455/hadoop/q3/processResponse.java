package cs455.hadoop.q3;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Arrays;
import java.util.List;

public class processResponse {
    public static void main(String[] args){
        try {
            File myObj = new File(args[0]);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                List<String> test = Arrays.asList(data.split("\t",-1));
                String airline = test.get(0);
                if(airline.indexOf('-') == -1){
                    //System.out.print(airline);
                    //System.out.println("," + test.get(1));
                }
                else{
                    List<String> test2 = Arrays.asList(airline.split("-"));
                    System.out.print(test2.get(0) + ",");
                    System.out.print(test2.get(1) + ",");
                    System.out.println(test.get(1));
                }


            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}