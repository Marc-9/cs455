package cs455.hadoop.q1;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * Reducer: Input to the reducer is the output from the mapper. It recieves word, list<count> pair
 * Sums up individual counts per given word. Emits <word, total count> pairs.
 */


public class BestTimeReducer extends Reducer<Text, IntWritable, Text, IntWritable>{
    protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        int count = 0;
        // calculate the total count
        for(IntWritable val : values){
            count += val.get();
        }
        context.write(key, new IntWritable(count));
    }
}