package com.amazon.Stock;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Stock implements Serializable{
	
	public String shareName;
	protected double sharePrice;
	public int numberOfShares;
    
    public Stock(){
    	shareName = "NA";
		sharePrice = 0.0;
		numberOfShares = 0;
    }
    
    public Stock(String shareName, double sharePrice, int numberOfShares) {
    	this.shareName = shareName;
		this.sharePrice = sharePrice;
		this.numberOfShares = numberOfShares;
    }
    
    public double getSharePrice() {
		return sharePrice;
	}
    
	void updateShares(int quantity){
          this.numberOfShares = quantity;
    }
    
    @Override
	public String toString() {
		
		StringBuffer buffer = new StringBuffer();
		buffer.append("\n...............................................");
		buffer.append("\n Share Name: "+shareName);
		buffer.append("\n Price of Share per unit: Rs."+Math.round(sharePrice * 100.0) / 100.0);
		buffer.append("\n Number of shares available: "+numberOfShares);
		buffer.append("\n...............................................");
		return buffer.toString();
	}

}
