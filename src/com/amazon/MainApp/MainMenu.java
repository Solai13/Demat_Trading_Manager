package com.amazon.MainApp;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

import com.amazon.FileWriter.BankRecordWriter;
import com.amazon.Stock.StockHandler;
import com.amazon.Transaction.TransactionAPI;
import com.amazon.User.User;
import com.amazon.User.UserHandler;

public class MainMenu {
	
	Scanner scan = new Scanner(System.in);
	char caseno;
	static StockHandler BSE;
	static UserHandler userH;
	static HashMap<Integer, User> usersMap;
	User currentUser;
	
	public MainMenu(){
      userH = new UserHandler(usersMap);
	}
	
	
	public void mainMenu() {
	    
		System.out.println("^^^^^^^^^^^^^^^^^^^ LOGIN MENU ^^^^^^^^^^^^^^^^^^^");
		System.out.println("Enter 0 to Quit");
        System.out.println("Enter 1 to Create an Account");
        System.out.println("Enter 2 to Login to an Account");        
        
        caseno = scan.next().charAt(0);
        
        switch (caseno) {
        case '0':
        	scan.close();
        	System.exit(0);
            break;
        case '1':
        	currentUser = userH.addUser(scan);
        	try {
    			BankRecordWriter.writeUserDB(userH.usersMap);
    		} catch(IOException e) {
    			System.out.println("User Database not found !");
    			System.exit(0);
    		}
        	loginMenu(currentUser);
            break;
        case '2':
        	currentUser = userH.authenticateUser(scan);
        	if(currentUser != null)
        		loginMenu(currentUser);
        	else
        		mainMenu();
            break;
        default:
      	  	 System.out.println("Invalid Option");
      	  	 mainMenu();
        }
	        
	  }
	
	public void loginMenu(User userdetail) {
	        
	        System.out.println("^^^^^^^^^^^^^^^^^^^ MAIN MENU ^^^^^^^^^^^^^^^^^^^");
	        System.out.println("Select the below option to proceed with your transaction");
	        System.out.println("0 - Quit");
	        System.out.println("1 - Display Demat account details");
	        System.out.println("2 - Deposit Money");
	        System.out.println("3 - Withdraw Money");
	        System.out.println("4 - Buy transaction");
	        System.out.println("5 - Sell transaction");
	        System.out.println("6 - View Transactions report");
	        System.out.println("7 - LogOut");
	
	        char maincaseno = scan.next().charAt(0);
	        
	        switch (maincaseno) {
	        case '0':
	        	try {
   	       			BankRecordWriter.writeUserDB(userH.usersMap);
   	       			BankRecordWriter.writeStockInformation(BSE);
   	       		} catch(IOException e) {
   	       			System.out.println("User Database not found !");
   	       			System.exit(0);
   	       		}
	        	scan.close();
	        	System.exit(0);
	            break;
	        case '1':
	            System.out.println("Below are your account details \n");
	            System.out.println(userdetail);
	            loginMenu(userdetail);
	            break;
	        case '2':	
	        	while(true) {
	        		double depositMoney = 0;
		        	System.out.println("Enter the Money to be stored in the account: ");
	            	String mon = scan.next();
	            	double moneyvalue = 0;
	                try {
	                	moneyvalue = Double.parseDouble(mon);
	                	depositMoney = moneyvalue;
	                	if(depositMoney > 0 && depositMoney < 999999999){
			            	TransactionAPI.depositMoney(userdetail, depositMoney);
			                System.out.println("Money Deposited succesfully. Updated Balance is Rs."+Math.round(userdetail.getMoney() * 100.0) / 100.0);
			                try {
			 	       			BankRecordWriter.writeUserDB(userH.usersMap);	
			 	       		} catch(IOException e) {
			 	       			System.out.println("User Database not found !");
			 	       			System.exit(0);
			 	       		}
			                break;
	                	} else {
	                		System.out.println("\n>> Invalid Amount !");
	                		continue;
	                	}
	                }
	                catch (NumberFormatException ne) {
		                   System.out.println("Invalid value has been given. Please enter a valid amount !");
	                }
	            }
	        	loginMenu(userdetail);
	        	break;
	        case '3':
	        	while(true) {
	        		double withdrawMoney = 0;
	        		System.out.println("Enter the Money you would like to withdraw");
	            	String mon = scan.next();
	            	double moneyvalue = 0;
	                try {
	                	moneyvalue = Double.parseDouble(mon);
	                	withdrawMoney = moneyvalue;
	                	if(withdrawMoney > 0 && withdrawMoney < 999999999){
			            	if(TransactionAPI.withdrawMoney(userdetail, withdrawMoney)) {
			            		System.out.println("Money Withdrawal succesful. Updated Balance is Rs."+Math.round(userdetail.getMoney() * 100.0) / 100.0);
				                try {
				 	       			BankRecordWriter.writeUserDB(userH.usersMap);	
				 	       		} catch(IOException e) {
				 	       			System.out.println("User Database not found !");
				 	       			System.exit(0);
				 	       		}
			                	break;
			            	} else {
		                		continue;
		                	}
	                	} else {
	                		System.out.println("\n>> Invalid Amount !");
	                		continue;
	                	}
	                }
	                catch (NumberFormatException ne) {
		                   System.out.println("Invalid value has been given. Please enter a valid amount ! ");
	                }
	            }
		        loginMenu(userdetail);
	            break;
	        case '4':
	               System.out.println("--** Stock List **--\n");
	               BSE.listShares();
	               if(TransactionAPI.buyShares(userdetail,BSE, scan)) {
	            	   try {
		   	       			BankRecordWriter.writeUserDB(userH.usersMap);
		   	       			BankRecordWriter.writeStockInformation(BSE);
		   	       		} catch(IOException e) {
		   	       			System.out.println("User Database not found !");
		   	       			System.out.println(e);
		   	       			System.exit(0);
		   	       		}
	            	   System.out.println("Transaction Successful !!!");
	               }   
	               else
	            	   System.out.println("Transaction Failed !");
	               loginMenu(userdetail);
	               
	               break;
	        case '5':
	               System.out.println("Sell transaction");
	               userdetail.userHandler.listShares();
	               if(TransactionAPI.sellShares(userdetail,BSE, scan)) {
	            	   try {
		   	       			BankRecordWriter.writeUserDB(userH.usersMap);
		   	       			BankRecordWriter.writeStockInformation(BSE);
		   	       		} catch(IOException e) {
		   	       			System.out.println("User Database not found !");
		   	       			System.exit(0);
		   	       		}
	            	   System.out.println("Transaction Successful !!!");
	               }
	               else
	            	   System.out.println("Transaction Failed !");
	               loginMenu(userdetail);
	               break;
	        case '6':
	        		TransactionAPI.viewTransactionReport(userdetail, scan);
	        		loginMenu(userdetail);
	               break;
	        case '7':
	        	try {
   	       			BankRecordWriter.writeUserDB(userH.usersMap);
   	       			BankRecordWriter.writeStockInformation(BSE);
   	       		} catch(IOException e) {
   	       			System.out.println("User Database not found !");
   	       			System.exit(0);
   	       		}
	            System.out.println("Succesfully Logged Out");
	            mainMenu();
	            break;
	            
	        default:
	               System.out.println("Invalid Option");
	               loginMenu(userdetail);
	               break;
	        }
	        
	  }

	public static void main(String[] args) {
	
		System.out.println("^^^^^^^^^^^^^^^^^^^ DMAT TRADING ACCOUNT MANAGER ^^^^^^^^^^^^^^^^^^^");
		try {
			usersMap = BankRecordWriter.readUserDB();
		} catch(FileNotFoundException e) {
			usersMap = new HashMap<Integer, User>();
		}catch(IOException e) {
			e.printStackTrace();
			System.out.println("User Database not found !");
			System.exit(0);
		}catch(ClassNotFoundException e) {
			System.out.println("User Class not found !");
			System.out.println(e);
			System.exit(0);
		}
		try {
			BSE = BankRecordWriter.readStockInformation();
			System.out.println("Loading and updating stock informations and prices !!");
			BSE.loadCurrentSharePrices(usersMap);
		} catch(IOException e) {
			System.out.println("Loading your stocks !!");
			BSE = BankRecordWriter.writeDummyStockData();
		}catch(ClassNotFoundException e) {
			System.out.println("Stock Class not found !");
			System.out.println(e);
			System.exit(0);
		}
		MainMenu mainApp = new MainMenu();
		mainApp.mainMenu();
		
	}

}
