package com.amazon.Transaction;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.LinkedList;
import java.util.Scanner;

import com.amazon.Stock.Stock;
import com.amazon.Stock.StockDatabase;
import com.amazon.Stock.StockHandler;
import com.amazon.User.User;

public class TransactionAPI {
	
	StockDatabase stocksDb = new StockDatabase();
//	StockHandler BSE;
	Stock bseStock, userStock;
	User user;
	
	TransactionAPI(){
		Stock s1 = new Stock("Amazon",100,500);
        Stock s2 = new Stock("Flipkart",90,500);
        Stock s3 = new Stock("Walmart",80,500);
        Stock s4 = new Stock("Jabong",70,500);
        Stock s5 = new Stock("Myntra",60,500);
        Stock s6 = new Stock("koovs",50,500);
        Stock s7 = new Stock("nike",40,500);
        
        LinkedList<Stock> stockDBList = new LinkedList<Stock>();
        stockDBList.add(s1);
        stockDBList.add(s2);
        stockDBList.add(s3);
        stockDBList.add(s4);
        stockDBList.add(s5);
        stockDBList.add(s6);
        stockDBList.add(s7);
        
//        BSE = new StockHandler(stockDBList);
	}
	
	void depositMoney(User user, double amount) {
		user.money += amount;
	}
	
	boolean withdrawMoney(User user, double amount) {
		if(user.money < amount) {
			System.out.println("Insufficient balance in the account");
			return false;
		}
		else {
			user.money -= amount;
			System.out.println("Amount has been debitted");
			return true;
		}
	}
	
	
	public double securityTransferTax(double amount) {
        final double tax = 0.001;
        return amount-(tax*amount);
  }
  
  public double transactionCharge(double amountToBeDebited) {
        final double tax = 0.005;
        double transactionFinal = amountToBeDebited-(tax*amountToBeDebited);
        
        if(transactionFinal<100)
               transactionFinal=100;
        
        return securityTransferTax(transactionFinal);
        
  }
  
  public void buyShares(User user, StockHandler BSE) {
	
      int sharesToBuy;
      double amountToBeDebited;
      Scanner in = new Scanner(System.in);
      
      System.out.println("Enter the name of the company:");
      String shareName = in.next();
      if(BSE.checkShare(shareName))
    	  bseStock = BSE.fetchStocks(shareName);
      else
//        mainMenu(); //it will go to main menu option to reselect buy option    	  
      System.out.println("Enter the number of shares you'd like to buy:");
      sharesToBuy = in.nextInt();
      
      if(sharesToBuy > bseStock.Availableshares) {
             System.out.println("You cannot buy shares more than the available shares. Please re-try again");
//             mainMenu(); //it will go to main menu option to reselect buy option
      }
      else {
             amountToBeDebited = bseStock.Shareprice*sharesToBuy;
             double finalAmount=transactionCharge(amountToBeDebited);
             System.out.println("Total amount to pay for buying "+sharesToBuy+" shares is \\u20b9"+finalAmount);
             
             if(user.money > finalAmount) {
            	 if(BSE.updateSharesInMarket(shareName, "Remove", sharesToBuy)) {
	            		 withdrawMoney(user, finalAmount);
	        		 	 if(user.userHandler.checkShare(shareName)) {
	            			 if(user.userHandler.updateSharesInMarket(shareName, "Add", sharesToBuy))
	            				 System.out.println("Shares updated to your account");
	            		 }
	            		 else {
	            		 	user.userHandler.stockList.add(new Stock(shareName,bseStock.Shareprice,sharesToBuy));
	             		 	System.out.println("Shares added to your account");
	             		 	user.transactionReport.add(new Transaction(101, "Buy", LocalDate.now(), LocalTime.now(), shareName, bseStock.Shareprice, sharesToBuy, finalAmount));
	//                      mainMenu(); //it will go to main menu option to reselect buy option 
	            		 }
            		 	
            	 }
            	 else
            		 System.out.println("Failed");
//               mainMenu(); //it will go to main menu option to reselect buy option            	 
             }
      }
  }

public void sellShares(User user, StockDatabase stocksDb) {
      
	  int sharesToSell;
      double amountToBeCredited;
      Scanner in = new Scanner(System.in);
      
      System.out.println("Enter the name of the company:");
      String shareName = in.next();
      if(user.userHandler.checkShare(shareName))
    	  userStock = user.userHandler.fetchStocks(shareName);
      else
    	  System.out.println("Share not available");
//    	  mainMenu(); //it will go to main menu option to reselect buy option
      System.out.println("Enter the number of shares you'd like to sell:");
      sharesToSell = in.nextInt();
      
      if(sharesToSell > userStock.Availableshares) {
             System.out.println("You cannot sell shares more than the available shares. Please re-try again");
//             mainMenu(); //it will go to main menu option to reselect buy option
      }
      else {
             amountToBeCredited = userStock.Shareprice*sharesToSell;
             double finalAmount=transactionCharge(amountToBeCredited);
             System.out.println("Total amount to be credited for selling "+sharesToSell+" shares is \\u20b9"+finalAmount);
             if(stocksDb.updateSharesInMarket(shareName, "Add", sharesToSell) && user.userHandler.updateSharesInMarket(shareName, "Remove", sharesToSell)) {
            	 depositMoney(user, finalAmount); //calls deposit function to deposit the money to user's wallet
            	 System.out.println("Shares sold successful");
            	 user.transactionReport.add(new Transaction(101, "Sell", LocalDate.now(), LocalTime.now(), shareName, bseStock.Shareprice, sharesToSell, finalAmount));
             }
                 
             
      }           
}


  
 public static void viewTransactionReportForDate(LinkedList<Transaction> transactionReport){
	 
	Scanner sc =  new Scanner(System.in);
	System.out.println("Enter the start date:");
	System.out.println("Enter the start year:");
	int startYear = sc.nextInt();
	 	System.out.println("---------------------------------------------");
		System.out.println("          Transaction Report");
		System.out.println("---------------------------------------------");
		for(Transaction t : transactionReport) {
			System.out.println(t);
		}
 }
 
 public static void viewTransactionReportForShare(LinkedList<Transaction> transactionReport){
	 
	Scanner sc =  new Scanner(System.in);
	System.out.println("Enter the start date:");
	System.out.println("Enter the start year:");
	int startYear = sc.nextInt();
	 	System.out.println("---------------------------------------------");
		System.out.println("          Transaction Report");
		System.out.println("---------------------------------------------");
		for(Transaction t : transactionReport) {
			System.out.println(t);
		}
 }
 
 public static void viewTransactionReport(LinkedList<Transaction> transactionReport){
	 
	Scanner sc =  new Scanner(System.in);
	System.out.println("Enter the start date:");
	System.out.println("Enter the start year:");
	int startYear = sc.nextInt();
	 	System.out.println("---------------------------------------------");
		System.out.println("          Transaction Report");
		System.out.println("---------------------------------------------");
		for(Transaction t : transactionReport) {
			System.out.println(t);
		}
 }

	public static void main(String[] args)throws Exception {
		
		LinkedList<Transaction> transactionReport = new LinkedList<Transaction>();
		
		
		
//		Transaction t1 = new Transaction(101, "Buy", LocalDate.of(2019, 12, 12), LocalTime.of(03, 35));
//		Transaction t2 = new Transaction(102, "Buy", LocalDate.of(2019, 12, 20), LocalTime.of(04, 20));
//		Transaction t3 = new Transaction(103, "Sell", LocalDate.of(2020, 01, 15), LocalTime.of(9, 40));
//		Transaction t4 = new Transaction(104, "Buy", LocalDate.of(2020, 02, 12), LocalTime.of(02, 15));
//		
//		transactionReport.add(t1);
//		transactionReport.add(t2);
//		transactionReport.add(t3);
//		transactionReport.add(t4);
//		
//		TransactionAPI.viewTransactionReport(transactionReport);
		
        Stock s1 = new Stock("Amazon",100,500);
        Stock s2 = new Stock("Flipkart",90,500);
        Stock s3 = new Stock("Walmart",80,500);
        Stock s4 = new Stock("Jabong",70,500);
        Stock s5 = new Stock("Myntra",60,500);
        Stock s6 = new Stock("koovs",50,500);
        Stock s7 = new Stock("nike",40,500);
        
        LinkedList<Stock> stockDBList = new LinkedList<Stock>();
        stockDBList.add(s1);
        stockDBList.add(s2);
        stockDBList.add(s3);
        stockDBList.add(s4);
        stockDBList.add(s5);
        stockDBList.add(s6);
        stockDBList.add(s7);
        
        StockHandler BSE = new StockHandler(stockDBList);

        
        

		
		    String sDate1="31/12/1998";  
		    Date date1=new SimpleDateFormat("dd/MM/yyyy").parse(sDate1);  
		    System.out.println(sDate1+"\t"+date1);
	}
	

}
