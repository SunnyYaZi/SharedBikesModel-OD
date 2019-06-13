package com.zf.ofobike.simulationwithmodel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Random;
import java.util.TreeMap;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;

public class Simulation {
	private static HashMap<String,Node> nodeMap = new HashMap<String,Node>();//����ڵ���Ϣ
	private static HashMap<String,Bike> bikeMap = new HashMap<String,Bike>();//���浥����Ϣ
	private static HashMap<String,TreeMap<String,Double>> ODMap = new HashMap<String,TreeMap<String,Double>>();//����OD���󣬼�����ת�ƾ���
	private static HashMap<String,ArrayList<Double>> differentLamdaMap = new HashMap<String,ArrayList<Double>>();//����ÿ������ÿ��ʱ�ε�ָ���ֲ�����lamda
    private static HashMap<String,Double> lamdaMap = new HashMap<String,Double>();//���浱ǰÿ�������ָ���ֲ�����Lamda
    private static HashMap<String,HashMap<String,Double>> distanceMap = new HashMap<String,HashMap<String,Double>>();//����������
    private static HashMap<String,ArrayList<Integer>> intervalSeqMap = new HashMap<String,ArrayList<Integer>>();//����ÿ�����������ʱ����
    private static HashMap<String,Integer> outBikeTimeMap = new HashMap<String,Integer>();//��¼ÿ�����򵥳�������ʱ��
    private static HashMap<String,Integer> bikeArrivalTimeMap = new HashMap<String,Integer>();//��¼��������ĳ�����ʱ��
    private static HashMap<String,Integer> bikeStartTimeMap = new HashMap<String,Integer>();//��¼������ĳ���������ʱ��
    private static HashMap<String,String> bikeArrivalNodeMap = new HashMap<String,String>();//��¼���������Ľڵ�
    private static HashMap<String,String> bikeStartNodeMap = new HashMap<String,String>();//��¼��������Ľڵ�
    
    private static double speed = 5.0;//�����ٶ�5m/s
    private static int time = 0;//����ʱ��
    private static int endTime = 60 * 60 * 24;//�������ʱ��
    private static double infectProportion = 1;//��Ⱦ����
    private static int time07 = 7 * 60 * 60;
    private static int time11 = 11 * 60 * 60;
    private static int time16 = 16 * 60 * 60;
    private static int time20 = 20 * 60 * 60;
    private static int time24 = 24 * 60 * 60;
    
    private static HashMap<Integer,Boolean> flagMap = new HashMap<Integer,Boolean>();
    
	//(nodeId,bikeId1,...)
	private static File nodeFile = new File("E:\\ProgramProcess\\SharedBikes\\ofo\\2017-11-30\\simulation\\nodeAndAllBikeId\\part-00000.csv");
	//(bikeId)
	private static File bikeFile = new File("E:\\ProgramProcess\\SharedBikes\\ofo\\2017-11-30\\simulation\\allBikeId\\part-00000.csv");
	//(node1,nodex proportion1,nodey proportion1+proportion2,....)
	private static File ODFile = new File("E:\\ProgramProcess\\SharedBikes\\ofo\\2017-11-30\\simulation\\centerNumToOtherCenterNumProportion\\part-00000.csv");
	//(node1,lamda1,lamda2,lamda3,lamda4,lamda5)
	private static File lamdaFile = new File("E:\\ProgramProcess\\SharedBikes\\ofo\\2017-11-30\\simulation\\centerNumAndDifferentLamda\\part-r-00000.csv");
	//(node1,nodex distance1,nodey distance2,...)
	private static File distanceFile = new File("E:\\ProgramProcess\\SharedBikes\\ofo\\2017-11-30\\simulation\\centerNumToOtherCenterNumDistance\\part-00000.csv");
	
	private static File file1 = new File("E:\\ProgramProcess\\SharedBikes\\ofo\\2017-11-30\\simulationResultWithModel\\infectedProportion1-5\\process5\\bikeMap.csv");
	private static File file2 = new File("E:\\ProgramProcess\\SharedBikes\\ofo\\2017-11-30\\simulationResultWithModel\\infectedProportion1-5\\process5\\nodeMap.csv");
	private static File file3 = new File("E:\\ProgramProcess\\SharedBikes\\ofo\\2017-11-30\\simulationResultWithModel\\infectedProportion1-5\\process5\\intervalSeqMap.csv");//���ÿ�������������ʱ��������
	private static File file4 = new File("E:\\ProgramProcess\\SharedBikes\\ofo\\2017-11-30\\simulationResultWithModel\\infectedProportion1-5\\process5\\process.csv");//��Ⱦ����
	private static File file5 = new File("E:\\ProgramProcess\\SharedBikes\\ofo\\2017-11-30\\simulationResultWithModel\\infectedProportion1-5\\process5\\initInfectedNode.csv");//��ʼ��Ⱦ�����Լ���Ⱦ����ID
	private static File file6 = new File("E:\\ProgramProcess\\SharedBikes\\ofo\\2017-11-30\\simulationResultWithModel\\infectedProportion1-5\\process5\\simulationNet.csv");
	
	public static void main(String[] args) {
		try {
			//��ʼ��flagMap
			for(int i=1;i<=4;i++){
				flagMap.put(i,true);
			}
			//��ʼ���ڵ���Ϣ
			CsvReader cr1 = new CsvReader(nodeFile.getAbsolutePath(),',',Charset.forName("UTF-8"));
			CsvWriter cw4 = new CsvWriter(file4.getAbsolutePath(),',',Charset.forName("utf-8"));
			CsvWriter cw5 = new CsvWriter(file5.getAbsolutePath(),',',Charset.forName("utf-8"));
			CsvWriter cw6 = new CsvWriter(file6.getAbsolutePath(),',',Charset.forName("utf-8"));
			//��ʼ���ڵ���Ϣ
			while(cr1.readRecord()){
				Node node = new Node();
				node.setId(cr1.get(0));
				ArrayList<String> bikeIdList = node.getBikeIdList();
				for(int i=1;i<cr1.getColumnCount();i++){
					bikeIdList.add(cr1.get(i));
				}
				node.setBikeIdList(bikeIdList);
				node.setBikeNum();
				nodeMap.put(node.getId(), node);
			}
			//��ʼ��������Ϣ
			CsvReader cr2 = new CsvReader(bikeFile.getAbsolutePath(),',',Charset.forName("UTF-8"));
			while(cr2.readRecord()){
				Bike bike = new Bike();
				bike.setBikeId(cr2.get(0));
				bikeMap.put(bike.getBikeId(), bike);
			}
			//��ʼ��OD����
			CsvReader cr3 = new CsvReader(ODFile.getAbsolutePath(),',',Charset.forName("UTF-8"));
			while(cr3.readRecord()){
				String node = cr3.get(0);
				TreeMap<String,Double> hm = new TreeMap<String,Double>();
				for(int i=1;i<cr3.getColumnCount();i++){
					hm.put(cr3.get(i).split(" ")[0], Double.parseDouble(cr3.get(i).split(" ")[1]));
				}
				ODMap.put(node, hm);
			}
			//��ʼ��ָ���ֲ��벴�ɷֲ��Ĳ���lamda
			CsvReader cr4 = new CsvReader(lamdaFile.getAbsolutePath(),',',Charset.forName("UTF-8"));
			while(cr4.readRecord()){
				lamdaMap.put(cr4.get(0), Double.parseDouble(cr4.get(1)));
				ArrayList<Double> lamdaList = new ArrayList<Double>();
				for(int i=1;i<cr4.getColumnCount();i++){
					lamdaList.add(Double.parseDouble(cr4.get(i)));
				}
				differentLamdaMap.put(cr4.get(0), lamdaList);
			}
			//��ʼ������
			CsvReader cr5 = new CsvReader(distanceFile.getAbsolutePath(),',',Charset.forName("UTF-8"));
			while(cr5.readRecord()){
				String node = cr5.get(0);
				HashMap<String,Double> hm = new HashMap<String,Double>();
				for(int i=1;i<cr5.getColumnCount();i++){
					hm.put(cr5.get(i).split(" ")[0], Double.parseDouble(cr5.get(i).split(" ")[1]));
				}
				distanceMap.put(node, hm);
			}
			
			//����ָ���ֲ��������ÿ�������ʼʱ����(��)
			for(Entry<String,Double> e : lamdaMap.entrySet()){
				ArrayList<Integer> intervalList = new ArrayList<Integer>();
				int interval = randomInterval(e.getValue());
				intervalList.add(interval);
				intervalSeqMap.put(e.getKey(), intervalList);
				
				if(interval == -1){
					//��ʼ��������ʱ��
					outBikeTimeMap.put(e.getKey(), interval);
				}else {
					//��ʼ��������ʱ��
					outBikeTimeMap.put(e.getKey(), time + interval);
				}
			}
			
			//���ѡ��һ����Ⱦ����
			Random rand = new Random();
			String initInfectedNode = rand.nextInt(10000) + "";
			cw5.write(initInfectedNode);
			//�ڸ����������ѡȡһ����Ⱦ����
			Node node = nodeMap.get(initInfectedNode);
			ArrayList<String> bikeIdList = node.getBikeIdList();
//			String initInfectedBike = bikeIdList.get(rand.nextInt(node.getBikeNum()));
//			
//			//�޸Ľڵ���Ϣ
//			ArrayList<String> infectedBikeIdList = node.getInfectedBikeIdList();
//			infectedBikeIdList.add(initInfectedBike);
//			node.setInfectedBikeIdList(infectedBikeIdList);
//			node.setInfectedBikeNum();
//			node.setInfected();
//			nodeMap.put(initInfectedNode, node);
//		    //�޸ĵ�����Ϣ
//			Bike bike = bikeMap.get(initInfectedBike);
//			bike.setInfected(true);
//			bikeMap.put(initInfectedBike, bike);
			
//			//���ø���������е�������Ⱦ
			ArrayList<String> infectedBikeIdList = node.getInfectedBikeIdList();
			for(String infectedBikeId : bikeIdList){
				//�޸Ľڵ���Ϣ
				infectedBikeIdList.add(infectedBikeId);
			    //�޸ĵ�����Ϣ
				Bike bike = bikeMap.get(infectedBikeId);
				bike.setInfected(true);
				bikeMap.put(infectedBikeId, bike);
				cw5.write(infectedBikeId);
			}
			node.setInfectedBikeIdList(infectedBikeIdList);
			node.setInfectedBikeNum();
			node.setInfected();
			nodeMap.put(initInfectedNode, node);
			
			//���濪ʼ
			while(time < endTime){
				//��ȡ��ǰʱ���е�������������/�е������������
				HashMap<String,Integer> minTimeMap = findMinTime(outBikeTimeMap,bikeArrivalTimeMap);
				//�޸ķ���ʱ��
				time = minTimeMap.values().iterator().next();
				if(time < time07){
					
				}else if(time < time11){
					changeLamda(1);
				}else if(time < time16){
					changeLamda(2);
				}else if(time < time20){
					changeLamda(3);
				}else if(time < time24){
					changeLamda(4);
				}
				for(Entry<String,Integer> e : minTimeMap.entrySet()){
					//����ǵ�������
					if("node".equals(e.getKey().split(",")[1])){
						String nd = e.getKey().split(",")[0];
						Node n = nodeMap.get(nd);
						if(n.getBikeNum() != 0 && ODMap.containsKey(nd)){//���������������е����Լ�����ת�Ƶ���������
							if(!bikeStartNodeMap.keySet().containsAll(n.getBikeIdList())){//������û�м�¼�Ѿ���ȥ�ĵ���
								String outBikeId = null;
								do{//��ȡû�г�ȥ�ĵ���
									outBikeId = n.getBikeIdList().get(rand.nextInt(n.getBikeNum()));
								}while(bikeStartNodeMap.keySet().contains(outBikeId));
								//��¼���������ڵ�
								bikeStartNodeMap.put(outBikeId, nd);
								bikeStartTimeMap.put(outBikeId, time);
								
								//����OD������������ƶ�����
								TreeMap<String,Double> Od = ODMap.get(nd);
								double randRatio = rand.nextDouble();
								double preRatio = 0.0;
								for(Entry<String,Double> en : Od.entrySet()){
									if(randRatio < en.getValue() && randRatio >=preRatio){
										//��¼��������ڵ�
										bikeArrivalNodeMap.put(outBikeId, en.getKey());
										//��¼��������ʱ��
										double distance = distanceMap.get(nd).get(en.getKey());
										bikeArrivalTimeMap.put(outBikeId, (int)(distance/speed + time));
										break;
									}
									preRatio = en.getValue();
								}
							}
						}
						
						//�޸ĵ�ǰ��ȥ�����Ľڵ��´γ�ȥ�����ļ��
						int nextInterval = randomInterval(lamdaMap.get(nd));
						ArrayList<Integer> addNextIntevalList = intervalSeqMap.get(nd);
						addNextIntevalList.add(nextInterval);
						intervalSeqMap.put(nd, addNextIntevalList);
						if(nextInterval == -1){
							//����ʱ�����޸ĵ�ǰ��ȥ�����Ľڵ��´γ�ȥ������ʱ��
							outBikeTimeMap.put(nd, nextInterval);
						}else {
							//����ʱ�����޸ĵ�ǰ��ȥ�����Ľڵ��´γ�ȥ������ʱ��
							outBikeTimeMap.put(nd, time + nextInterval);
						}
					}
					//����ǵ�������
					if("bike".equals(e.getKey().split(",")[1])){
						String bikeId = e.getKey().split(",")[0];
						
						//�ҵ�����������ʱ��
						int startTime = bikeStartTimeMap.get(bikeId);
						
						//�ҵ�һ����¼���������ı�ڵ����Ϣ
						Node nStart = nodeMap.get(bikeStartNodeMap.get(bikeId));
						nStart.getBikeIdList().remove(bikeId);
						nStart.setBikeNum();
						nStart.getInfectedBikeIdList().remove(bikeId);
						nStart.setInfectedBikeNum();
						nStart.setInfected();
						nodeMap.put(nStart.getId(), nStart);
						
						
						Node nArrival = nodeMap.get(bikeArrivalNodeMap.get(bikeId));
						Bike b = bikeMap.get(bikeId);
						if(b.isInfected()){//�жϵ����Ƿ񱻸�Ⱦ
							nArrival.getInfectedBikeIdList().add(bikeId);
							if(nArrival.getBikeNum() != 0){//����������������
								//�Զ��ĸ��ʸ�Ⱦ��һ����
								if(rand.nextDouble() <= infectProportion){
									String otherInfectedBikeId = nArrival.getBikeIdList().get(rand.nextInt(nArrival.getBikeNum()));
									if(!nArrival.getInfectedBikeIdList().contains(otherInfectedBikeId)){//���û�б���Ⱦ���ĵ���
										nArrival.getInfectedBikeIdList().add(otherInfectedBikeId);
									}
									//�޸ı���Ⱦ��������Ϣ
									Bike otherBike = bikeMap.get(otherInfectedBikeId);
									otherBike.setInfected(true);
									bikeMap.put(otherInfectedBikeId, otherBike);
									cw4.write(bikeId + "," + otherInfectedBikeId + "," + time);
									cw4.endRecord();
								}
							}
							nArrival.setInfectedBikeNum();
							nArrival.setInfected();
						}
						nArrival.getBikeIdList().add(bikeId);
						nArrival.setBikeNum();
						nodeMap.put(nArrival.getId(), nArrival);
						
						cw6.write(bikeId + "," + startTime + "," + nStart.getId() + "," + time + "," + nArrival.getId());
						cw6.endRecord();
						
						//ɾ����¼
						bikeArrivalNodeMap.remove(bikeId);
						bikeArrivalTimeMap.remove(bikeId);
						bikeStartNodeMap.remove(bikeId);
						bikeStartTimeMap.remove(bikeId);
					}
				}
			}
			cr1.close();
			cr2.close();
			cr3.close();
			cr4.close();
			cr5.close();
			cw4.close();
			cw5.close();
			cw6.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//��������Ϣ
		CsvWriter cw1 = new CsvWriter(file1.getAbsolutePath(),',',Charset.forName("utf-8"));
		CsvWriter cw2 = new CsvWriter(file2.getAbsolutePath(),',',Charset.forName("utf-8"));
		CsvWriter cw3 = new CsvWriter(file3.getAbsolutePath(),',',Charset.forName("utf-8"));
		for(Bike bk : bikeMap.values()){
			try {
				cw1.write(bk.toString());
				cw1.endRecord();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		for(Node nd : nodeMap.values()){
			try {
				String content1 = "";
				for(String str1 : nd.getBikeIdList()){
					content1 += str1 + ",";
				}
				String content2 = "";
				for(String str2 : nd.getInfectedBikeIdList()){
					content2 += str2 + ",";
				}
				cw2.write(nd.toString());
				cw2.endRecord();
				if(!content1.isEmpty()){
					cw2.write(content1.substring(0, content1.length()-1));
					cw2.endRecord();
				}
				if(!content2.isEmpty()){
					cw2.write(content2.substring(0, content2.length()-1));
					cw2.endRecord();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		for(Entry<String,ArrayList<Integer>> en : intervalSeqMap.entrySet()){
			try {
				String centerNum = en.getKey();
				String content = "";
				for(Integer in : en.getValue()){
					content += in + ",";
				}
				cw3.write(centerNum);
				if(!content.isEmpty()){
					cw3.write(content.substring(0, content.length()-1));
					cw3.endRecord();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		cw1.close();
		cw2.close();
		cw3.close();
		System.out.println(System.currentTimeMillis());
	}
	
	/**
	 * ����ÿ�������lamda���������ȡʱ��������lamdaΪ0���򷵻�-1
	 * @param lamda
	 * @return
	 */
	public static int randomInterval(double lamda){
		if(lamda != 0.0){
			double z = Math.random();
			double x = (-(1 / lamda) * Math.log(z));
			return (int)(x * 3600);
		}else {
			return -1;
		}
	}
	
	/**
	 * �ҵ����ʱ�̵ĵ�������/�����������Ϣ
	 * @param outBikeTime
	 * @param bikeArrivalTime
	 * @return
	 */
	public static HashMap<String,Integer> findMinTime(HashMap<String,Integer> outBikeTime,HashMap<String,Integer> bikeArrivalTime){
		HashMap<String,Integer> allMap = new HashMap<String,Integer>(outBikeTime);
		allMap.putAll(bikeArrivalTime);
		Collection<Integer> coll = allMap.values();
		ArrayList<Integer> al = new ArrayList<Integer>(coll);
		Iterator<Integer> it = al.iterator();
		while(it.hasNext()){//ȥ��lamdaΪ0���������ɵ�ʱ����Ϊ-1�ļ�¼
			Integer in = it.next();
			if(in == -1){
				it.remove();
			}
		}
		HashMap<String,Integer> minTimeMap = new HashMap<String,Integer>();
		Collections.sort(al);
		int minTime = al.get(0);//��ȡ���ʱ��
		for(Entry<String,Integer> e : outBikeTime.entrySet()){
			if(e.getValue() == minTime){
				minTimeMap.put(e.getKey() + "," + "node", e.getValue());
			}
		}
		if(!bikeArrivalTime.isEmpty()){
			for(Entry<String,Integer> e : bikeArrivalTime.entrySet()){
				if(e.getValue() == minTime){
					minTimeMap.put(e.getKey() + "," + "bike", e.getValue());
				}
			}
		}
		return minTimeMap;
	}
	
	public static void changeLamda(int i){
		if(flagMap.get(i)){
			for(Entry<String,ArrayList<Double>> e : differentLamdaMap.entrySet()){
				lamdaMap.put(e.getKey(), e.getValue().get(i));
				if(e.getValue().get(i-1) == 0.0){
//					�޸ĵ�ǰ��ȥ�����Ľڵ��´γ�ȥ�����ļ��
					int nextInterval = randomInterval(e.getValue().get(i));
					ArrayList<Integer> addNextIntevalList = intervalSeqMap.get(e.getKey());
					addNextIntevalList.add(nextInterval);
					intervalSeqMap.put(e.getKey(), addNextIntevalList);
					if(nextInterval == -1){
						//����ʱ�����޸ĵ�ǰ��ȥ�����Ľڵ��´γ�ȥ������ʱ��
						outBikeTimeMap.put(e.getKey(), nextInterval);
					}else {
						//����ʱ�����޸ĵ�ǰ��ȥ�����Ľڵ��´γ�ȥ������ʱ��
						outBikeTimeMap.put(e.getKey(), time + nextInterval);
					}
				}
			}
			flagMap.put(i, false);
		}
	}
}
