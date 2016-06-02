package com.suda.cs;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.junit.Test;
/**
 * Created by fei on 2016/5/31.
 */

public class ParseXml {
        public static String inputPath;
        public static String fieldSeparator;
        public static Table table = new Table();
    //读入数
        public static void parserXml(String fileName)
        {
            File inputXml = new File(fileName);
            SAXReader saxReader = new SAXReader();
            try
            {   
            	//读取XML文件,获得document对象
                Document document = saxReader.read(inputXml);
                //获取文档的根节点
                Element stations = document.getRootElement();
                
              //获取HDFSPath
                Element path = stations.element("hdfsPath");
                inputPath=path.getText();
                //System.out.println(inputPath);
                //获取分隔符
                Element separator = stations.element("fieldSeparator");
                fieldSeparator=separator.getText();
                //System.out.println(fieldSeparator);
                
                Element tab = stations.element("table");
                table.setName(tab.attributeValue("name"));
                String rowkey = tab.attributeValue("rowkey");
                table.setRwokey(rowkey);
                //对某节点下的所有子节点进行遍历
                    for(Iterator j = tab.elementIterator(); j.hasNext();)
                    {
                        Family family = new Family();
                        Element familyNode =(Element) j.next();
                        family.setName(familyNode.attributeValue("name"));
                        for(Iterator k = familyNode.elementIterator(); k.hasNext();){
                            Element qualify =(Element) k.next();
                            if(qualify.getName().equals("qualify")){
                                family.getQualifies().add(qualify.getText());
                                
                            }
                        }
                        table.getFamilies().add(family);
                        
                    }    
                    //System.out.println(table.toString());
                
            }
            catch (DocumentException e)
            {
                System.out.println(e.getMessage());
            }
        }
}
