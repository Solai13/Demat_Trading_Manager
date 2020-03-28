package com.amazon.User;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Scanner;

import java.util.Random;

@SuppressWarnings("serial")
public class UserHandler implements Serializable{
	
	public HashMap<Integer, User> usersMap;
//	public Double money;
//	public String userName, passWord;
//	public Integer accountNumber;
//	public User currentUser;
	
//	public UserHandler(HashMap<Integer, User> usersMap) {
//		this.usersMap = usersMap;
//	}
	
	public UserHandler(HashMap<Integer, User> usersMap) {
		this.usersMap = usersMap;
//		money = null;
//		userName = null;
//		passWord = null;
//		accountNumber = null;
//		currentUser = null;
	}

	public User addUser(Scanner scan) {
		
		Double money=null;
		String userName, passWord;
		Integer accountNumber;
		User currentUser;
		
        System.out.print("\n>> Enter the New User Name: ");
        userName = scan.next();
        System.out.print(">> Enter a password: ");
        passWord = scan.next();
        while(true) {
            System.out.print(">> Enter the Money to be stored in the account: ");
            String mon = scan.next();
            int monval = 0;
            try {
            	monval = Integer.parseInt(mon);
            	money = (double) monval;
            	if(money > 0)
            		break;
            	else {
            		System.out.println("\n>> Invalid Amount !");
            		continue;
            	}
            }
            catch (NumberFormatException ne) {
        		System.out.println("\n>> Invalid Amount !");
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
	            	System.out.println(">> Authentication successfull !");
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

	public static void main(String[] args) {
		
		
//		System.out.print("************* Welcome ************");
//		Scanner in = new Scanner(System.in);
//		UserH userH = new UserH();
//		HashMap<Integer, User> usersMap = new HashMap<Integer, User>(); 
//		UserHandler userH = new UserHandler(usersMap);
//		User currentUser;
//		while(true) {
//		System.out.print("\n>> 1. Create Account");
//		System.out.print("\n>> 2. Login");
//		System.out.print("\n>> Enter your choice: ");
//		int choice = in.nextInt();
//		switch(choice) {
//		case 1:
//			currentUser = userH.addUser(in);
//			break;
//		case 2:
//			currentUser = userH.authenticateUser(in);
//			break;
//		default:
//			System.out.println(">> Invalid choice.");
//		}
//		}
//		in.close();
	}

}