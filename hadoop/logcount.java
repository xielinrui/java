import java.io.IOException;
import java.util.StringTokenizer;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
public class logcount {
  public static class TokenizerMapper extends Mapper<Object, Text, NullWritable, Text>{
    private Text word = new Text();
    public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
      String str = value.toString();
      String[] items = str.split("\r\n"); 
      for (int i = 0, len = items.length; i < len; i++) {
        word.set(items[i].substring(7, 15) + items[i].substring(36));
        context.write(NullWritable.get(), word);
      }
    }
  }
  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
    String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
    if (otherArgs.length != 2) {
      System.err.println("Usage: logcount <in> <out>");
      System.exit(2);
    }
    Job job = new Job(conf, "log count");
    job.setJarByClass(logcount.class);
    job.setMapperClass(TokenizerMapper.class);
    job.setOutputKeyClass(NullWritable.class);
    job.setOutputValueClass(Text.class);
    FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
    FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));
    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}