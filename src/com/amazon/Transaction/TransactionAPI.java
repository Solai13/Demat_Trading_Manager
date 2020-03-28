package com.amazon.Transaction;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import com.amazon.FileWriter.BankRecordWriter;
import com.amazon.Stock.Stock;
import com.amazon.Stock.StockHandler;
import com.amazon.User.User;
import com.amazon.Utils.Constants;

public class TransactionAPI {
	
	static int transactionID = 0;
	
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
	
	
	static double transactionCharge(double transactionAmount,String transactionType) {
        
        double finalAmount=0.0;
        
        double taxFinal = Constants.TRANSACTION_CHARGE * transactionAmount;
        
        if(taxFinal<100)
               taxFinal=100;
        
        double stt = Constants.SECURITY_TRANSFER_TAX * transactionAmount;
        
        if(transactionType.equals("buy"))
             finalAmount = transactionAmount+stt+taxFinal; 

       else if (transactionType.equals("sell"))
          finalAmount=transactionAmount-stt-taxFinal;

        return Math.round(finalAmount * 100.0) / 100.0;
	}

  
  public static boolean buyShares(User user, StockHandler BSE, Scanner in) {
	
	  Stock bseStock =  null;
	  int sharesToBuy;
      double amountToBeDebited;
      
      System.out.println("Enter the name of the company:");
      String shareName = in.next();
      if(BSE.checkShare(shareName))
    	  bseStock = BSE.fetchStocks(shareName);
      else
    	  return false; //it will go to main menu option to reselect buy option    	  
      System.out.println("Enter the number of shares you'd like to buy:");
      sharesToBuy = in.nextInt();
      
      if(sharesToBuy > bseStock.numberOfShares) {
             System.out.println("You cannot buy shares more than the available shares. Please re-try again");
             return false; //it will go to main menu option to reselect buy option
      }
      else {
             amountToBeDebited = bseStock.getSharePrice()*sharesToBuy;
             double finalAmount=transactionCharge(amountToBeDebited, "buy");
             System.out.println("Total amount to pay for buying "+sharesToBuy+" shares is Rs."+finalAmount);
             
             if(user.getMoney() > finalAmount) {
            	 if(BSE.updateSharesInMarket(shareName, "Remove", sharesToBuy, bseStock.getSharePrice())) {
	            		 withdrawMoney(user, finalAmount);
	        		 	 if(user.userHandler.checkShare(shareName)) {
	            			 if(user.userHandler.updateSharesInMarket(shareName, "Add", sharesToBuy, bseStock.getSharePrice()))
	            				 System.out.println("Shares updated to your account");
	            		 }
	            		 else {
	            		 	user.userHandler.stockList.add(new Stock(shareName,bseStock.getSharePrice(),sharesToBuy));
	             		 	System.out.println("Shares added to your account");
	            		 }
                         try {
                        	 transactionID = (int) BankRecordWriter.readTransactionNumber();
                        	 transactionID += 1;
			               } catch (ClassNotFoundException e) {
	                             System.out.println("error1");
	                             e.printStackTrace();
			               } catch (IOException e) {
				            	   transactionID = 1000;
				                   BankRecordWriter.writeTransactionNumber(transactionID);
			               }

	        		 	user.transactionReport.add(new Transaction(transactionID, "Buy", LocalDate.now(), LocalTime.now(), shareName, bseStock.getSharePrice(), sharesToBuy, finalAmount));
	        		 	BankRecordWriter.writeTransactionNumber(transactionID);
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
             double finalAmount=transactionCharge(amountToBeCredited, "sell");
             System.out.println("Total amount to be credited for selling "+sharesToSell+" shares is Rs."+finalAmount);
             if(BSE.updateSharesInMarket(shareName, "Add", sharesToSell, bseStock.getSharePrice()) && user.userHandler.updateSharesInMarket(shareName, "Remove", sharesToSell, bseStock.getSharePrice())) {
            	 depositMoney(user, finalAmount); //calls deposit function to deposit the money to user's wallet
            	 System.out.println("Shares sold successful");
            	 try {
                	 transactionID = (int) BankRecordWriter.readTransactionNumber();
                	 transactionID += 1;
	               } catch (ClassNotFoundException e) {
                         System.out.println("error1");
                         e.printStackTrace();
	               } catch (IOException e) {
		            	   transactionID = 1000;
		                   BankRecordWriter.writeTransactionNumber(transactionID);
	               }
            	 BankRecordWriter.writeTransactionNumber(transactionID);
            	 user.transactionReport.add(new Transaction(transactionID, "Sell", LocalDate.now(), LocalTime.now(), shareName, bseStock.getSharePrice(), sharesToSell, finalAmount));
             }
             return true;
      }       
      
  }
  
  public static void viewTransactionReport(User user, Scanner scan) {
	    
		System.out.println("^^^^^^^^^^^^^^^^^^^ TRANSACTIONS MENU ^^^^^^^^^^^^^^^^^^^");
		System.out.println("Enter 1 to view all transactions");
		System.out.println("Enter 2 to view transactions by date range");
		System.out.println("Enter 3 to view transactions for a stock");   
		System.out.println("Enter 4 to go back to Main Menu");   
    
		char caseno = scan.next().charAt(0);
    
		switch (caseno) {
		case '1':
			viewAllTransactions(user);
			break;
		case '2':
			viewTransactionReportForDate(user, scan);
			break;
		case '3':
			viewTransactionReportForShare(user, scan);
			break;
		case '4':
			break;
		default:
			System.out.println("Invalid Option");
			viewTransactionReport(user, scan);
    }
	        
	  }

  
 public static void viewTransactionReportForDate(User user, Scanner scan){
	 
	 String date;
	 LocalDate tempDate = null, startDate, endDate;
	 DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("uuuu-MM-dd");
	 System.out.print(">> Enter the start date in (yyyy-mm-dd) format: ");
	 while(tempDate == null) {
		 date = scan.next();
		 try {
			 tempDate = LocalDate.parse(date, dateFormat);
			 break;
		 }catch (DateTimeParseException e) {
			 System.out.print("\n>> Sorry, that's not a valid date. Please enter date in proper format: ");
		 }
	 }
	 startDate = tempDate;
	 tempDate = null;
	 System.out.print(">> Enter the end date in (yyyy-mm-dd) format: ");
	 while(tempDate==null) {
		 date = scan.next();
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
	String shareName = scan.next();
	
	List<Transaction> filteredTransactions = user.transactionReport.stream().filter(t-> t.shareName.equalsIgnoreCase(shareName)).collect(Collectors.toList());
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

}
