package com.suda.cs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.mapreduce.HFileOutputFormat2;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * Created by fei on 2016/6/2.
 */
public class Main {
	public static int main (String[] args) throws Exception{
		if(args.length !=1){
            System.out.println("xml file is not one, you should provide only one xml file");
            return 0;
        }
		String fileName = args[0];		
		ParseXml.parserXml(fileName);
		Configuration configuration = new Configuration();
        Job job = Job.getInstance(configuration, "Bulk Loading HBase Table::" + ParseXml.table.getName());
        job.setJarByClass(Main.class);
        job.setInputFormatClass(TextInputFormat.class);
        job.setMapOutputKeyClass(ImmutableBytesWritable.class);//指定输出键类
        job.setMapOutputValueClass(Put.class);//指定输出值类
        job.setMapperClass(BulkLoadMapper.class);//指定Map函数
        FileInputFormat.addInputPaths(job, ParseXml.inputPath);//输入路径
        FileSystem fs = FileSystem.get(configuration);
        Path output = new Path(ParseXml.inputPath+"/output");
        if (fs.exists(output)) {
            fs.delete(output, true);//如果输出路径存在，就将其删除
        }
        FileOutputFormat.setOutputPath(job, output);//输出路径
        Connection connection = ConnectionFactory.createConnection(configuration);
        HTable table = new HTable(configuration, ParseXml.table.getName());
        HFileOutputFormat2.configureIncrementalLoad(job, table);
        job.waitForCompletion(true);
        if (job.isSuccessful()){
            HFileLoader.doBulkLoad(output.toString(), ParseXml.table.getName());//导入数据
            return 0;
        } else {
            return 1;
        }
    }
		
	}
	

