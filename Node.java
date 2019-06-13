package com.zf.ofobike.simulationwithmodel;

import java.util.ArrayList;

public class Node {
	private String id;//�ڵ�ID
	private int bikeNum;//������������
	private int infectedBikeNum;//��������Ⱦ��������
	private boolean isInfected;//��Ⱦ״̬
	private ArrayList<String> bikeIdList = new ArrayList<String>();//��������ID
	private ArrayList<String> infectedBikeIdList = new ArrayList<String>();//��������Ⱦ����ID
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public int getBikeNum() {
		return bikeNum;
	}
	
	public void setBikeNum() {
		this.bikeNum = bikeIdList.size();
	}
	
	public int getInfectedBikeNum() {
		return infectedBikeNum;
	}
	
	public void setInfectedBikeNum() {
		this.infectedBikeNum = infectedBikeIdList.size();
	}
	
	public boolean isInfected() {
		return isInfected;
	}
	
	public void setInfected() {
		if(infectedBikeNum != 0){
			isInfected = true;
		}else {
			isInfected = false;
		}
	}
	
	public ArrayList<String> getBikeIdList() {
		return bikeIdList;
	}
	
	public void setBikeIdList(ArrayList<String> bikeIdList) {
		this.bikeIdList = bikeIdList;
	}
	
	public ArrayList<String> getInfectedBikeIdList() {
		return infectedBikeIdList;
	}
	
	public void setInfectedBikeIdList(ArrayList<String> infectedBikeIdList) {
		this.infectedBikeIdList = infectedBikeIdList;
	}
	
	public String toString() {
		return id + "," + bikeNum + "," + isInfected + "," + infectedBikeNum;
	}
}
