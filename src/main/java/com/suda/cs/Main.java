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
        job.setMapOutputKeyClass(ImmutableBytesWritable.class);//ָ���������
        job.setMapOutputValueClass(Put.class);//ָ�����ֵ��
        job.setMapperClass(BulkLoadMapper.class);//ָ��Map����
        FileInputFormat.addInputPaths(job, ParseXml.inputPath);//����·��
        FileSystem fs = FileSystem.get(configuration);
        Path output = new Path(ParseXml.inputPath+"/output");
        if (fs.exists(output)) {
            fs.delete(output, true);//������·�����ڣ��ͽ���ɾ��
        }
        FileOutputFormat.setOutputPath(job, output);//���·��
        Connection connection = ConnectionFactory.createConnection(configuration);
        HTable table = new HTable(configuration, ParseXml.table.getName());
        HFileOutputFormat2.configureIncrementalLoad(job, table);
        job.waitForCompletion(true);
        if (job.isSuccessful()){
            HFileLoader.doBulkLoad(output.toString(), ParseXml.table.getName());//��������
            return 0;
        } else {
            return 1;
        }
    }
		
	}
	

