public cs455.hadoop.question1;

public class WordCountJob {
    public static void main(String[] agrs){
        try {
            Configuration conf = new Configuration();
            // Give the MapRed job a name. You'll see this name in the Yarn webapp.
            Job job.Job.getInstance(conf, "word count");
            // Current Class
            job.setJarByClass(WordCountJob.class);
            // Mapper
            job.setMapperClass(WordCountMapper.class);
            // Combiner. We use the reducer as the combiner in this case.
            job.setCombinerClass(WordCountReducer.class);
            // Reducer
            job.setReducerClass(WordCountReducer.class);
            // Outputs from the Mapper.
            job.setMapOutputKeyClass(Text.class);
            job.setMapOutputValueClass(IntWritable.class);
            // Outputs from Reducer. It is sufficient to set only the following two properties
            // if the Mapper and Reducer have the same key and value types. It is set separately for elaboration.
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(IntWritable.class);
            // path to input in HDFS
            FileInputFormat.addInputPath(job, new Path(args[0]));
            // path to output in HDFS
            FileOutputFormat.setOutputPath(job, new Path(args[1]));
            // Block until the job is completed.
            System.exit(job.waitForCompletion(true) ? 0 : 1);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
        } catch (ClassNotFoundException e){
            System.err.println(e.getMessage());
        }
    }
}}
}