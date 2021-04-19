package cs455.hadoop.q1;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.StringTokenizer;
import java.util.Arrays;
import java.util.List;

/**
 * Mapper: Reads line by line, splits into words. Emit <word, 1> pairs.
 */

public class MajorHubsMapper extends Mapper<LongWritable, Text,Text, IntWritable>{
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        // tokenize into words.
        List<String> test = Arrays.asList(value.toString().split(","));

        try{
            String[] monthConverter = new String[] {"January", "Febuary", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
            String[] dayConverter = new String[] {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
            int delay = (int)Double.parseDouble(test.get(14));
            String month = monthConverter[Integer.parseInt(test.get(1))];
            String dow = dayConverter[Integer.parseInt(test.get(3))];
            String scheduledTime = test.get(12);

            context.write(new Text(month), new IntWritable(delay));
            context.write(new Text(dow), new IntWritable(delay));
            context.write(new Text(scheduledTime), new IntWritable(delay));
        }catch (Exception e){

        }
    }
}