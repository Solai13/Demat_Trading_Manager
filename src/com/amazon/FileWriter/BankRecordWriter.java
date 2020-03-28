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
import com.amazon.Utils.Constants;


public class BankRecordWriter {


	public static void writeStockInformation(StockHandler BSE) throws IOException {
		
	    File file =  new File(Constants.STOCKS_DB);
	    ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
	    objectOutputStream.writeObject(BSE);
	    objectOutputStream.close();
	    
	}
	
	public static StockHandler readStockInformation() throws IOException, ClassNotFoundException {
	    
		File fileToRead = new File(Constants.STOCKS_DB);
	    ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(fileToRead));
	    StockHandler BSE = (StockHandler) objectInputStream.readObject();
	    objectInputStream.close();
	    return BSE;
	    
	}
	
	public static void writeUserDB(HashMap<Integer, User> usersMap) throws IOException {


		File fileToRead = new File(Constants.USER_DB);
	    ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(fileToRead));
	    objectOutputStream.writeObject(usersMap);
	    objectOutputStream.close();
	    
	}
	
	@SuppressWarnings("unchecked")
	public static HashMap<Integer, User> readUserDB() throws IOException, ClassNotFoundException {

	    
		File fileToRead = new File(Constants.USER_DB);
	    ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(fileToRead));
	    HashMap<Integer, User> usersMap = (HashMap<Integer, User>) objectInputStream.readObject();
	    objectInputStream.close();
	    return usersMap;
	    
	}
	
	public static StockHandler writeDummyStockData() {
		
      Stock s1 = new Stock("Amazon",1900.10,100);
      Stock s2 = new Stock("Flipkart",890.75,100);
      Stock s3 = new Stock("Walmart",1109.58,100);
      Stock s4 = new Stock("Jabong",305.40,500);
      Stock s5 = new Stock("Myntra",430.50,500);
      Stock s6 = new Stock("Puma",55.95,1000);
      Stock s7 = new Stock("Nike",83.23,1000);
      
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

      try {
			BankRecordWriter.writeStockInformation(BSE);
		} catch(IOException e) {
			System.out.println(e);
			System.exit(0);
		}
      
      try {
    	  newBSE = BankRecordWriter.readStockInformation();
		} catch(IOException e) {
			System.exit(0);
		}catch(ClassNotFoundException e) {
			System.out.println("User Class not found !");
			System.exit(0);
		}

      return newBSE;
	}
	
	public static void writeTransactionNumber(int uniqueID)
    {
        try (
            ObjectOutputStream objectOutput = new ObjectOutputStream(new FileOutputStream(new File(Constants.TRANSACTION_NUMBER_DB))))
        {
                objectOutput.writeObject(uniqueID);
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static int readTransactionNumber () throws ClassNotFoundException, IOException
    {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(new File(Constants.TRANSACTION_NUMBER_DB)));
            int uniqueID = (int) objectInputStream.readObject();
            objectInputStream.close();
            return uniqueID;
}   


}
