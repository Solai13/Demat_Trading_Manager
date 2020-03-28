package com.amazon.User;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Scanner;

import java.util.Random;

@SuppressWarnings("serial")
public class UserHandler implements Serializable{
	
	public HashMap<Integer, User> usersMap;
	
	public UserHandler(HashMap<Integer, User> usersMap) {
		this.usersMap = usersMap;
	}

	public User addUser(Scanner scan) {
		
		Double money=null;
		String userName, passWord;
		Integer accountNumber;
		User currentUser;
		
        System.out.print("\n>> Enter the New User Name: ");
        userName = scan.next();
        while(true) {
	        System.out.print(">> Enter a password: ");
	        passWord = scan.next();
	        if(passWord.length() > 6) {
		        break;	
	        }else {
	        	System.out.println(">> Enter a strong password of minimum 6 characters !");
	        	continue;
	        }
        }
        while(true) {
            System.out.print(">> Enter the Money to be stored in the account: ");
            String mon = scan.next();
            double moneyvalue = 0;
            try {
            	moneyvalue = Double.parseDouble(mon);
            	money = moneyvalue;
            	if(money > 0 && money < 999999999)
            		break;
            	else {
            		System.out.println(">> Invalid Amount !");
            		continue;
            	}
            }
            catch (NumberFormatException ne) {
        		System.out.println(">> Invalid Amount !");
            }
        }
        accountNumber = new Integer(new Random().nextInt(9999 + 1)  + 1000);
        if(!(usersMap.isEmpty())) {
	        while(usersMap.containsKey(accountNumber))
	        	accountNumber = new Integer(new Random().nextInt(9999 + 1)  + 1000);	
        }

		currentUser = new User(userName, passWord, money, accountNumber);
		System.out.println(currentUser);
		usersMap.put(currentUser.accountNumber, currentUser);

		System.out.println(">> Account has been created successfully !");
        return currentUser;
	}
    
	
	public User authenticateUser(Scanner scan) {
		
		String passWord;
		Integer accountNumber=null;
		User currentUser;
		
//		if(usersMap.isEmpty()) {
//			System.out.println("\n>> No user has been created yet. Please create a new User !");
//            return null;
//        }else {
        	while(true) {
                System.out.print("\n>> Enter your Account Number: ");
                String ac = scan.next();
                int inval = 0;
                try {
                  inval = Integer.parseInt(ac);
                  accountNumber = inval;
                  break;
                }
                catch (NumberFormatException ne) {
                                 System.out.print("\n>> Invalid account number !");
                }
            }
	        System.out.print(">> Enter your password: ");
            passWord = scan.next();
	        
	        if(usersMap.containsKey(accountNumber)) {
	        	currentUser = usersMap.get(accountNumber);
	            if(currentUser.passWord.equals(passWord)) {
	            	System.out.println(">> Authentication successful !");
	            	System.out.println(currentUser);
	            	return currentUser;
	            }
	            else {
	            	System.out.println(">> Authentication failure. Either account number/password is wrong. Please try again !");
	            	return null;
	            }
	        }else {
            	System.out.println(">> Account does not exist. Please create an account !");
            	return null;
            }
//        }
	}

}