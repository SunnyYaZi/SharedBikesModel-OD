package com.zf.ofobike.simulationwithmodel;

public class Bike {
	private String bikeId;//µ¥³µID
	private boolean isInfected;//¸ÐÈ¾×´Ì¬
	
	public String getBikeId() {
		return bikeId;
	}
	
	public void setBikeId(String bikeId) {
		this.bikeId = bikeId;
	}
	
	public boolean isInfected() {
		return isInfected;
	}
	
	public void setInfected(boolean isInfected) {
		this.isInfected = isInfected;
	}
	
	public String toString() {
		return bikeId + "," + isInfected;
	}
}
