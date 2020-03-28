package com.amazon.User;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

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
		buffer.append("\n Money in the account: Rs."+Math.round(money * 100.0) / 100.0);
		if(!userHandler.stockList.isEmpty()) {
			List<Stock> filteredStock = userHandler.stockList.stream().filter(s-> s.numberOfShares>0).collect(Collectors.toList());
			if(filteredStock.size() > 0) {
				buffer.append("\n *********** List of shares in possession ***********");
				for(Stock stock : filteredStock) {
						buffer.append(stock);
		        }
				buffer.append("\n Note: Stock price displayed is last traded price at which the stock was brought/sold");
			}
		}
		buffer.append("\n----------------------------------------");
		return buffer.toString();
	}
  
}
