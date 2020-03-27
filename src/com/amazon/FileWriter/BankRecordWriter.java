package com.amazon.FileWriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.LinkedList;

import com.amazon.Stock.Stock;
import com.amazon.Stock.StockHandler;
import com.amazon.User.User;


public class BankRecordWriter {
	
//	public User user;
//	public StockHandler BSE;
//	public HashMap<Integer, User> usersMap;
	
//	public static void writeUserInformation(User user){
//
//        try (
//            ObjectOutputStream objectOutput
//                = new ObjectOutputStream(new FileOutputStream(user.userName + "_account.db"))
//            ) {
// 
//                objectOutput.writeObject(user);
//
//        } catch (IOException ex) {
//            ex.printStackTrace();
//
//        }
//    }
//
//    public static User readUserInformation(String username)
//
//    {
//        try {
//
//            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(username + "_account.db"));
//            return (User) objectInputStream.readObject();
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        return null;
//
//    }


	public static void writeStockInformation(String path, StockHandler BSE) throws IOException {
		
	    File file =  new File(path);
	    ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
	    objectOutputStream.writeObject(BSE);
	    objectOutputStream.close();
	    
	}
	
	public static StockHandler readStockInformation(String path) throws IOException, ClassNotFoundException {
	    
		File fileToRead = new File(path);
	    ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(fileToRead));
	    StockHandler BSE = (StockHandler) objectInputStream.readObject();
	    objectInputStream.close();
	    return BSE;
	    
	}
	
	public static void writeUserDB(String path, HashMap<Integer, User> usersMap) throws IOException {
		
//		 try (
//				 ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(new File(path)))
//		            ) {
//		 
//		            	objectOutputStream.writeObject(usersMap);
//		        	    objectOutputStream.close();
//
//		        } catch (IOException ex) {
//		            ex.printStackTrace();
//
//		        }
		 
		File file =  new File(path);
	    ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
	    objectOutputStream.writeObject(usersMap);
	    objectOutputStream.close();
	    
	}
	
	@SuppressWarnings("unchecked")
	public static HashMap<Integer, User> readUserDB(String path) throws IOException, ClassNotFoundException {
		
//		try {
//
//            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(new File(path)));
//            return (HashMap<Integer, User>) objectInputStream.readObject();
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        return null;
	    
		File fileToRead = new File(path);
	    ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(fileToRead));
	    HashMap<Integer, User> usersMap = (HashMap<Integer, User>) objectInputStream.readObject();
	    objectInputStream.close();
	    return usersMap;
	    
	}
	
	public static void main(String[] args) {
		
      Stock s1 = new Stock("Amazon",100,500);
      Stock s2 = new Stock("Flipkart",90,500);
      Stock s3 = new Stock("Walmart",80,500);
      Stock s4 = new Stock("Jabong",70,500);
      Stock s5 = new Stock("Myntra",60,500);
      Stock s6 = new Stock("Puma",50,500);
      Stock s7 = new Stock("Nike",40,500);
      
      LinkedList<Stock> stockDBList = new LinkedList<Stock>();
      stockDBList.add(s1);
      stockDBList.add(s2);
      stockDBList.add(s3);
      stockDBList.add(s4);
      stockDBList.add(s5);
      stockDBList.add(s6);
      stockDBList.add(s7);
      
      StockHandler BSE = new StockHandler(stockDBList);
      StockHandler newBSE = null;
      
      BSE.listShares();
      
      try {
			BankRecordWriter.writeStockInformation("stocks.db", BSE);
			System.out.println(" Write Successful !");
		} catch(IOException e) {
			System.out.println("User Database not found !");
			System.out.println(e);
			System.exit(0);
		}
      
      try {
    	  newBSE = BankRecordWriter.readStockInformation("stocks.db");
    	  System.out.println(" Read Successful !");
		} catch(IOException e) {
			System.out.println("User Database not found !");
			System.exit(0);
		}catch(ClassNotFoundException e) {
			System.out.println("User Class not found !");
			System.exit(0);
		}
      
      newBSE.listShares();
      
	}

}
