package com.amazon.User;

import java.io.Serializable;
import java.util.LinkedList;

import com.amazon.Stock.Stock;
import com.amazon.Stock.StockHandler;
import com.amazon.Transaction.Transaction;

@SuppressWarnings("serial")
public class User implements Serializable{
	
	public String userName;
	public String passWord;
	private Double money;
	public Integer accountNumber;
	public LinkedList<Transaction> transactionReport;
	public StockHandler userHandler;
	
    public User(String userName, String passWord, double money, Integer accountNumber) {
	this.userName = userName;
	this.passWord = passWord;
	this.money = money;
	this.accountNumber = accountNumber;
	transactionReport = new LinkedList<Transaction>();
	userHandler = new StockHandler();
    }

	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	@Override
	public String toString() {
		
		StringBuffer buffer = new StringBuffer();
		buffer.append("------------- DMAT Account details -------------");
		buffer.append("\n User Name: "+userName);
		buffer.append("\n Account Number: "+accountNumber);
		buffer.append("\n Money in the account: Rs."+money);
		if(!userHandler.stockList.isEmpty()) {
			buffer.append("\n *********** List of shares in posessions ***********\n");
			for(Stock stock : userHandler.stockList) {
				buffer.append(stock);
	        }	
		}
		buffer.append("\n----------------------------------------");
		return buffer.toString();
	}
  
}
