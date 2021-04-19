package cs455.hadoop.q3;

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
            String origin = test.get(7);
            String year = test.get(0);
            String orgin_year = origin + "-" + year;

            context.write(new Text(origin), new IntWritable(1));
            context.write(new Text(origin_year), new IntWritable(1));
        }catch (Exception e){

        }
    }
}