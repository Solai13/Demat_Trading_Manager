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

//class User{
//	
//	String user;
//	String pass;
//	Double money;
//	String uniqueID;
//	public User(String user, String pass, Double money, String uniqueID) {
//		this.user = user;
//		this.pass = pass;
//		this.money = money;
//		this.uniqueID = uniqueID;
//	}
//	@Override
//	public String toString() {
//		return "User [user=" + user + ", pass=" + pass + ", money=" + money + ", uniqueID=" + uniqueID +"]";
//	}
//	
//}
//
//class BankTransaction{
//	
//	User user;
//	
//	void depositMoney(User user, double amount) {
//		user.money += amount;
//	}
//	
//	boolean withdrawMoney(User user, double amount) {
//		if(user.money < amount) {
//			System.out.println("Insufficient balance in the account");
//			return false;
//		}
//		else {
//			user.money -= amount;
//			System.out.println("Amount has been debitted");
//			return true;
//		}
//	}
//	
//	double getBalance(User user) {
//		return user.money;
//	}
//}

public class MainMenu {
	
	Scanner scan = new Scanner(System.in);
	char caseno;
	static StockHandler BSE;
	static UserHandler userH;
	static HashMap<Integer, User> usersMap;
	User currentUser;
	
	public MainMenu(){
//      Stock s1 = new Stock("Amazon",100,500);
//      Stock s2 = new Stock("Flipkart",90,500);
//      Stock s3 = new Stock("Walmart",80,500);
//      Stock s4 = new Stock("Jabong",70,500);
//      Stock s5 = new Stock("Myntra",60,500);
//      Stock s6 = new Stock("koovs",50,500);
//      Stock s7 = new Stock("nike",40,500);
//      
//      LinkedList<Stock> stockDBList = new LinkedList<Stock>();
//      stockDBList.add(s1);
//      stockDBList.add(s2);
//      stockDBList.add(s3);
//      stockDBList.add(s4);
//      stockDBList.add(s5);
//      stockDBList.add(s6);
//      stockDBList.add(s7);
//      
//      BSE = new StockHandler(stockDBList);
//      usersMap = new HashMap<Integer, User>();
      userH = new UserHandler(usersMap);
	}
	
//	public MainMenu(HashMap<Integer, User> usersMap, StockHandler BSE) {
//		this.BSE = BSE;
//		this.userH = new UserHandler(usersMap);
//	}
	
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
	        		double depMoney = 0;
		        	System.out.println("Enter the Money to be stored in the account: ");
	            	String mon = scan.next();
	            	int moneyvalue = 0;
	                try {
	                	moneyvalue = Integer.parseInt(mon);
	                	depMoney = moneyvalue;
	                	if(depMoney > 0){
			            	TransactionAPI.depositMoney(userdetail, depMoney);
			                System.out.println("Money Deposited succesfully. Updated Balance is Rs."+userdetail.getMoney());
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
	            	int moneyvalue = 0;
	                try {
	                	moneyvalue = Integer.parseInt(mon);
	                	withdrawMoney = moneyvalue;
	                	if(withdrawMoney > 0){
			            	TransactionAPI.withdrawMoney(userdetail, withdrawMoney);
			                System.out.println("Money Withdrawal succesful. Updated Balance is Rs."+userdetail.getMoney());
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
	               //Transaction t1 = new Transaction(101, "Buy", LocalDate.of(2019, 12, 12), LocalTime.of(03, 35));
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
		
//		User u1 = new User("solai", "abcd", 5000.0, "jashdashd");
//		
//		BankTransaction b1 = new BankTransaction();
//		System.out.println(b1.getBalance(u1));
//		b1.depositMoney(u1, 3000);
//		System.out.println(b1.getBalance(u1));
//		b1.depositMoney(u1, 7000);
//		System.out.println(b1.getBalance(u1));
//		b1.withdrawMoney(u1, 2000);
//		b1.withdrawMoney(u1, 1500);
//		System.out.println(b1.getBalance(u1));
//		b1.withdrawMoney(u1, 15000);
//		System.out.println(b1.getBalance(u1));
		
		System.out.println("^^^^^^^^^^^^^^^^^^^ DMAT TRADING ACCOUNT MANAGER ^^^^^^^^^^^^^^^^^^^");
//		BankRecordWriter writer = new BankRecordWriter(); 
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
			BSE.loadCurrentSharePrices();
		} catch(IOException e) {
			System.out.println("Loading your stocks !!");
			BSE = BankRecordWriter.writeDummyStockData();
		}catch(ClassNotFoundException e) {
			System.out.println("Stock Class not found !");
			System.out.println(e);
			System.exit(0);
		}
//		MainMenu mainApp = new MainMenu(usersMap, BSE);
		MainMenu mainApp = new MainMenu();
		mainApp.mainMenu();
		
	}

}
