package com.amazon.Transaction;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import com.amazon.Stock.Stock;
import com.amazon.Stock.StockHandler;
import com.amazon.User.User;

public class TransactionAPI {
	
//	StockHandler stocksDb = new StockHandler();
//	StockHandler BSE;
//	User user;
	static int transactionID = 1000;
	
//	TransactionAPI(){
//		Stock s1 = new Stock("Amazon",100,500);
//        Stock s2 = new Stock("Flipkart",90,500);
//        Stock s3 = new Stock("Walmart",80,500);
//        Stock s4 = new Stock("Jabong",70,500);
//        Stock s5 = new Stock("Myntra",60,500);
//        Stock s6 = new Stock("koovs",50,500);
//        Stock s7 = new Stock("nike",40,500);
//        
//        LinkedList<Stock> stockDBList = new LinkedList<Stock>();
//        stockDBList.add(s1);
//        stockDBList.add(s2);
//        stockDBList.add(s3);
//        stockDBList.add(s4);
//        stockDBList.add(s5);
//        stockDBList.add(s6);
//        stockDBList.add(s7);
        
//        BSE = new StockHandler(stockDBList);
//	}
	
	public static void depositMoney(User user, double amount) {
		double currentBalance = user.getMoney();
		currentBalance += amount;
		user.setMoney(currentBalance);
	}
	
	public static boolean withdrawMoney(User user, double amount) {
		
		double currentBalance = user.getMoney();
		if(currentBalance < amount) {
			System.out.println("Insufficient balance in the account");
			return false;
		}
		else {
			currentBalance -= amount;
			user.setMoney(currentBalance);
			System.out.println("Amount has been debitted");
			return true;
		}
		
	}
	
	
	static double transactionCharge(double amountToBeDebited) {
    
		final double transactionChargePercent = 0.005;
		final double securityTransferTaxPercent = 0.001;
        double transactionChargeAmount = transactionChargePercent*amountToBeDebited;
        
        if(transactionChargeAmount<100)
        	transactionChargeAmount=100;
        
        return (amountToBeDebited + transactionChargeAmount + (securityTransferTaxPercent*amountToBeDebited));
        
	}
  
  public static boolean buyShares(User user, StockHandler BSE, Scanner in) {
	
	  Stock bseStock =  null;
	  int sharesToBuy;
      double amountToBeDebited;
//      Scanner in = new Scanner(System.in);
      
      System.out.println("Enter the name of the company:");
      String shareName = in.next();
      if(BSE.checkShare(shareName))
    	  bseStock = BSE.fetchStocks(shareName);
      else
    	  return false;
//        mainMenu(); //it will go to main menu option to reselect buy option    	  
      System.out.println("Enter the number of shares you'd like to buy:");
      sharesToBuy = in.nextInt();
      
      if(sharesToBuy > bseStock.numberOfShares) {
             System.out.println("You cannot buy shares more than the available shares. Please re-try again");
             return false;
//             mainMenu(); //it will go to main menu option to reselect buy option
      }
      else {
             amountToBeDebited = bseStock.getSharePrice()*sharesToBuy;
             double finalAmount=transactionCharge(amountToBeDebited);
             System.out.println("Total amount to pay for buying "+sharesToBuy+" shares is \u20b9"+finalAmount);
             
             if(user.getMoney() > finalAmount) {
            	 if(BSE.updateSharesInMarket(shareName, "Remove", sharesToBuy)) {
	            		 withdrawMoney(user, finalAmount);
	        		 	 if(user.userHandler.checkShare(shareName)) {
	            			 if(user.userHandler.updateSharesInMarket(shareName, "Add", sharesToBuy))
	            				 System.out.println("Shares updated to your account");
	            		 }
	            		 else {
	            		 	user.userHandler.stockList.add(new Stock(shareName,bseStock.getSharePrice(),sharesToBuy));
	             		 	System.out.println("Shares added to your account");
	            		 }
	        		 	user.transactionReport.add(new Transaction(++transactionID, "Buy", LocalDate.now(), LocalTime.now(), shareName, bseStock.getSharePrice(), sharesToBuy, finalAmount));
	        		 	return true;
            	 }
            	 else
            		 return false; //it will go to main menu option to reselect buy option            	 
             }
             System.out.println("Insufficient balance in your Account.");
             return false;
      }
  }

  public static boolean sellShares(User user, StockHandler BSE, Scanner in) {
      
	  Stock bseStock =  null, userStock = null;  
	  int sharesToSell;
      double amountToBeCredited;
//      Scanner in = new Scanner(System.in);
      
      System.out.println("Enter the name of the company:");
      String shareName = in.next();
      if(user.userHandler.checkShare(shareName))
    	  userStock = user.userHandler.fetchStocks(shareName);
      else {
    	  System.out.println("Share not available");
    	  return false;
      }
      System.out.println("Enter the number of shares you'd like to sell:");
      sharesToSell = in.nextInt();
      
      if(sharesToSell > userStock.numberOfShares) {
             System.out.println("You cannot sell shares more than the available shares. Please re-try again");
             return false; //it will go to main menu option to reselect buy option
      }
      else {
    	  	 bseStock = BSE.fetchStocks(shareName);
             amountToBeCredited = userStock.getSharePrice()*sharesToSell;
             double finalAmount=transactionCharge(amountToBeCredited);
             System.out.println("Total amount to be credited for selling "+sharesToSell+" shares is \\u20b9"+finalAmount);
             if(BSE.updateSharesInMarket(shareName, "Add", sharesToSell) && user.userHandler.updateSharesInMarket(shareName, "Remove", sharesToSell)) {
            	 depositMoney(user, finalAmount); //calls deposit function to deposit the money to user's wallet
            	 System.out.println("Shares sold successful");
            	 user.transactionReport.add(new Transaction(++transactionID, "Sell", LocalDate.now(), LocalTime.now(), shareName, bseStock.getSharePrice(), sharesToSell, finalAmount));
             }
             return true;
      }       
      
  }

  
 public static void viewTransactionReportForDate(User user, Scanner scan){
	 
	 String date;
	 LocalDate tempDate = null, startDate, endDate;
	 DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("uuuu-MM-dd");
	 System.out.print(">> Enter the start date in (yyyy-mm-dd) format: ");
	 while(tempDate==null) {
		 date = scan.nextLine();
		 try {
			 tempDate = LocalDate.parse(date, dateFormat);
		 }catch (DateTimeParseException e) {
			 System.out.print("\n>> Sorry, that's not a valid date. Please enter date in proper format: ");
		 }
	 }
	 startDate = tempDate;
	 tempDate = null;
	 System.out.print(">> Enter the end date in (yyyy-mm-dd) format: ");
	 while(tempDate==null) {
		 date = scan.nextLine();
		 try {
			 tempDate = LocalDate.parse(date, dateFormat);	 
		 }catch (DateTimeParseException e) {
			 System.out.print("\n>> Sorry, that's not a valid date. Please enter date in proper format: ");
		 }
	 }
	 endDate = tempDate;
	 
	 List<Transaction> filteredTransactions = user.transactionReport.stream().filter(t-> t.transactionDate.isAfter(startDate)  && t.transactionDate.isBefore(endDate)).collect(Collectors.toList());
	 if(filteredTransactions.size()>=1) {
		System.out.println("*********************************************");
		System.out.println("          Transaction Report");
		System.out.println("*********************************************");
		for(Transaction t : filteredTransactions)
				System.out.println(t);	
	}else {
        System.out.println("No Transactions available in the given Date range !");
	} 
	 
 }
 
 public static void viewTransactionReportForShare(User user, Scanner scan){
	 
	System.out.print(">>Enter the share name: ");
	String shareName = scan.nextLine();
	
	List<Transaction> filteredTransactions = user.transactionReport.stream().filter(t-> t.shareName.equals(shareName)).collect(Collectors.toList());
    if(filteredTransactions.size()>=1) {
    	System.out.println("*********************************************");
    	System.out.println("          Transaction Report");
    	System.out.println("*********************************************");
    	for(Transaction t : filteredTransactions)
    			System.out.println(t);	
    }else {
        System.out.println("No Transactions available for the given share !");
    }
    
 }
 
 public static void viewAllTransactions(User user){
	 
		System.out.println("*********************************************");
		System.out.println("          Transaction Report");
		System.out.println("*********************************************");
		for(Transaction t : user.transactionReport) {
			System.out.println(t);
		}
 }

	public static void main(String[] args)throws Exception {
		
		LinkedList<Transaction> transactionReport = new LinkedList<Transaction>();
		
		
		
		Transaction t1 = new Transaction(101, "Buy", LocalDate.of(2019, 12, 12), LocalTime.of(03, 35), "Amazon", 1500.00, 3, 4500.00);
		Transaction t2 = new Transaction(102, "Buy", LocalDate.of(2019, 12, 20), LocalTime.of(04, 20), "Walmart", 1200.00, 2, 2400.00);
		Transaction t3 = new Transaction(103, "Sell", LocalDate.of(2020, 01, 15), LocalTime.of(9, 40), "Amazon", 1500.00, 1, 1500.00);
		Transaction t4 = new Transaction(104, "Buy", LocalDate.of(2020, 02, 12), LocalTime.of(02, 15), "Target", 1000.00, 5, 5000.00);
		transactionReport.add(t1);
		transactionReport.add(t2);
		transactionReport.add(t3);
		transactionReport.add(t4);

//		TransactionAPI.viewTransactionReportForDate(transactionReport);
//		TransactionAPI.viewTransactionReportForShare(transactionReport);
//		TransactionAPI.viewAllTransactions(transactionReport);
		
//        Stock s1 = new Stock("Amazon",100,500);
//        Stock s2 = new Stock("Flipkart",90,500);
//        Stock s3 = new Stock("Walmart",80,500);
//        Stock s4 = new Stock("Jabong",70,500);
//        Stock s5 = new Stock("Myntra",60,500);
//        Stock s6 = new Stock("koovs",50,500);
//        Stock s7 = new Stock("nike",40,500);
//        
//        LinkedList<Stock> stockDBList = new LinkedList<Stock>();
//        stockDBList.add(s1);
//        stockDBList.add(s2);
//        stockDBList.add(s3);
//        stockDBList.add(s4);
//        stockDBList.add(s5);
//        stockDBList.add(s6);
//        stockDBList.add(s7);
//        
//        StockHandler BSE = new StockHandler(stockDBList);
	}

}
