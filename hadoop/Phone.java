import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import java.io.IOException;


public class Phone extends Configured implements Tool {

    enum Counter {
        LINESKIP;
    }

//    @Override
    public int run(String[] args) throws Exception{
        Configuration conf = getConf();
        Job job = new Job(conf, "Phone");
        job.setJarByClass(Phone.class);//
        FileInputFormat.addInputPath(job, new Path(args[0]));//
        FileOutputFormat.setOutputPath(job, new Path(args[1]));//

        job.setMapperClass(Map.class);//
        job.setReducerClass(Reduce.class);//
        job.setOutputFormatClass(TextOutputFormat.class);
        job.setOutputKeyClass(Text.class);//
        job.setOutputValueClass(Text.class);//
        job.waitForCompletion(true);

        return job.isSuccessful() ? 0 : 1;
    }

    public static class Map extends
            Mapper<LongWritable, Text, Text, Text> {    //<KEYIN, VALUEIN, KEYOUT, VALUEOUT>
        @Override
        protected void map(LongWritable key, Text value, Context context)
                throws IOException, InterruptedException{
            try {
                String line = value.toString();//
                String[] lineSplit = line.split(" ");  //

                String A_user = "";//
                String B_user = "";  //
                boolean flag_one = false;//
                for (String split : lineSplit) {

                    if (!split.equals("") && flag_one == false){  //
                        A_user = split;
                        flag_one = true;
                        continue;
                    }
                    else if (!split.equals("") && flag_one == true){//
                        B_user += split;
                    }
                }

                context.write(new Text(B_user), new Text(A_user));//
            }
            catch (java.lang.ArrayIndexOutOfBoundsException e) {
                context.getCounter(Counter.LINESKIP).increment(1);
            }
        }
    }

    public static class Reduce extends Reducer<Text, Text, Text, Text> {

        @Override
        protected void reduce(Text key, Iterable<Text> values,
                              Context context)
                throws IOException, InterruptedException{
            String valueStr;
            String out = "";
            for (Text value : values){  //
                valueStr = value.toString() + "| ";  //
                out += valueStr;
            }
            context.write(key, new Text(out));      //
        }
    }
    //main方法运行的时候需要指定输入路径和输出路径
    public static void main(String[] args) throws Exception{
        int res = ToolRunner.run(new Configuration(), new Phone(), args);
        System.exit(res);
    }
}