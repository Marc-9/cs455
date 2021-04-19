package cs455.hadoop.q7;

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

public class ElapsedMapper extends Mapper<LongWritable, Text,Text, IntWritable>{
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        // tokenize into words.
        List<String> test = Arrays.asList(value.toString().split(","));

        try{

            int scheduled = (int)Double.parseDouble(test.get(23));
            int actual = (int)Double.parseDouble(test.get(24));
            int diff = actual - scheduled;
            String carrier = test.get(4);
            String carrier_tot = carrier + "-tot";
            context.write(new Text(carrier), new IntWritable(diff));
            context.write(new Text(carrier), new IntWritable(1));

        }catch (Exception e){

        }
    }
}