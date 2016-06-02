package com.suda.cs;

import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/**
 * Created by fei on 2016/6/2.
 */
public class BulkLoadMapper extends Mapper<LongWritable, Text, ImmutableBytesWritable, Put> {
    private Table table;
    private String fieldSeperator;
    private int qualifyNum=0;
    public void setup(Context context) {
        table = ParseXml.table;
        fieldSeperator = ParseXml.fieldSeparator;
        //caculate total qualify num
        for(Family family : table.getFamilies()){
            for(String qualify : family.getQualifies()){
                qualifyNum++;
            }
        }
    }

    public void map(LongWritable key, Text value, Context context){
        try {
            String[] values = value.toString().split(fieldSeperator);
            if(values.length != qualifyNum){
                return;
            }
            ImmutableBytesWritable rowKey = new ImmutableBytesWritable(values[0].getBytes());
            Put put = new Put(Bytes.toBytes(values[0]));
            int i=1;
            for(Family family : table.getFamilies()){
                for(String qualify : family.getQualifies()){
                    put.add(Bytes.toBytes(family.getName()),Bytes.toBytes(qualify),Bytes.toBytes(values[i++]));
                }
            }
            context.write(rowKey, put);
        } catch(Exception exception) {
            exception.printStackTrace();
        }

    }
}
