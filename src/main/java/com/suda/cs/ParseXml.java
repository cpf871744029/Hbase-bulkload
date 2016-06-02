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
/**
 * Created by fei on 2016/5/31.
 */

public class ParseXml {
        public static String inputPath;
        public static String fieldSeparator;
        public static Table table;
    //������
        public static void parserXml(String fileName)
        {
            File inputXml = new File(fileName);
            SAXReader saxReader = new SAXReader();
            try
            {   
            	//��ȡXML�ļ�,���document����
                Document document = saxReader.read(inputXml);
                //��ȡ�ĵ��ĸ��ڵ�
                Element stations = document.getRootElement();
                
              //��ȡHDFSPath
                Element path = stations.element("hdfsPath");
                inputPath=path.getText();
                
                //��ȡ�ָ���
                Element separator = stations.element("fieldSeparator");
                fieldSeparator=separator.getText();
                
                
                Element tab = stations.element("table");
                //��ĳ�ڵ��µ������ӽڵ���б���
                for(Iterator i = tab.elementIterator(); i.hasNext();)
                {
                    Element tableNode = (Element) i.next();
                    table.setName(tableNode.attributeValue("name"));
                    String rowkey = tableNode.attributeValue("rowkey");
                    table.setRwokey(rowkey);
                    for(Iterator j = tableNode.elementIterator(); j.hasNext();)
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
                }
            }
            catch (DocumentException e)
            {
                System.out.println(e.getMessage());
            }
        }
}
