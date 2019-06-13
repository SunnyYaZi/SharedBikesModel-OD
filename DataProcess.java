package com.zf.ofobike.simulationwithmodel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
/**
 * 根据tptp网络获得每个区域单车出去的时间间隔
 * @author 小丫子
 *
 */
public class DataProcess {
	public static void main(String[] args) {
		try {
			File fr = new File("E:\\ProgramProcess\\SharedBikes\\ofo\\2017-11-30\\createNet\\createNetWithTime\\part-00000.csv");
			File fw = new File("E:\\ProgramProcess\\SharedBikes\\ofo\\2017-11-30\\simulation\\aveNodeOutBikeTimeInterval\\aveNodeOutBikeTimeInterval.csv");
			CsvReader cr = new CsvReader(fr.getAbsolutePath(),',',Charset.forName("UTF-8"));
			CsvWriter cw = new CsvWriter(fw.getAbsolutePath(),',',Charset.forName("UTF-8"));
			HashMap<String,ArrayList<Long>> hm = new HashMap<String,ArrayList<Long>>();
			while(cr.readRecord()){
				if(!hm.containsKey(cr.get(1))){
					ArrayList<Long> al = new ArrayList<Long>();
					al.add(Long.parseLong(cr.get(0)));
					hm.put(cr.get(1), al);
				}else{
					ArrayList<Long> al = hm.get(cr.get(1));
					al.add(Long.parseLong(cr.get(0)));
					hm.put(cr.get(1),al);
				}
			}
			for(Entry<String,ArrayList<Long>> e : hm.entrySet()){
				cw.write(e.getKey());
				ArrayList<Long> al = e.getValue();
				Collections.sort(al);
				long preTime = 1511971200;//1130
				for(long l : al){
					cw.write((l - preTime) + "");
					preTime = l;
				}
				cw.endRecord();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
