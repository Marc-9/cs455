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

public class BestTimeMapper extends Mapper<LongWritable, Text,Text, IntWritable>{
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        // tokenize into words.
        List<String> test = Arrays.asList(value.toString().split(","));

        try{
            int delay = (int)Double.parseDouble(test.get(14));
            String month = test.get(1);
            context.write(new Text(test.get(1)), new IntWritable((int)Double.parseDouble(test.get(14))));
        }catch (Exception e){

        }
    }
}