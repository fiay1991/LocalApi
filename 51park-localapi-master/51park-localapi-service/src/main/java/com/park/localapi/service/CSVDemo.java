package com.park.localapi.service;



import java.nio.charset.Charset;
import java.util.ArrayList;

import com.csvreader.CsvReader;

	public class CSVDemo {

	public static void main(String[] args) { 
		    try {      
		        ArrayList<String[]> csvList = new ArrayList<String[]>(); //用来保存数据  
		        String csvFilePath = "C:\\Users\\Administrator\\Desktop\\123.csv";  
		         CsvReader reader = new CsvReader(csvFilePath,',',Charset.forName("UTF-8"));    //一般用这编码读就可以了      
		           
		         //reader.readHeaders(); // 跳过表头   如果需要表头的话，不要写这句。  
		           
		         while(reader.readRecord()){ //逐行读入除表头的数据      
		             csvList.add(reader.getValues());  
		         }              
		         reader.close();  
		         String[] strings = csvList.get(0);
		         String s = strings[0];
		         String d = strings[1];
		         String f = strings[2];
		         String g = strings[3];
		         String h = strings[4];
		         for(int row=0;row<csvList.size();row++){  
		               for(int i=0;i<csvList.get(row).length;i++){
		            	   String  cell = csvList.get(row)[i]; //取得第row行第0列的数据  
				             System.out.print(cell+","); 
		               }
		               System.out.println(); 
		         }  
		           
		           
		    }catch(Exception ex){  
		        System.out.println(ex);  
		    }  
		}  
	} 
	


