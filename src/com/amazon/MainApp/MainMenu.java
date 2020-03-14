package com.amazon.MainApp;

class User{
	
	String user;
	String pass;
	Double money;
	String uniqueID;
	public User(String user, String pass, Double money, String uniqueID) {
		this.user = user;
		this.pass = pass;
		this.money = money;
		this.uniqueID = uniqueID;
	}
	@Override
	public String toString() {
		return "User [user=" + user + ", pass=" + pass + ", money=" + money + ", uniqueID=" + uniqueID +"]";
	}
	
}

class BankTransaction{
	
	User user;
	
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
	
	double getBalance(User user) {
		return user.money;
	}
}

public class MainMenu {

	public static void main(String[] args) {
		
		User u1 = new User("solai", "abcd", 5000.0, "jashdashd");
		
		BankTransaction b1 = new BankTransaction();
		System.out.println(b1.getBalance(u1));
		b1.depositMoney(u1, 3000);
		System.out.println(b1.getBalance(u1));
		b1.depositMoney(u1, 7000);
		System.out.println(b1.getBalance(u1));
		b1.withdrawMoney(u1, 2000);
		b1.withdrawMoney(u1, 1500);
		System.out.println(b1.getBalance(u1));
		b1.withdrawMoney(u1, 15000);
		System.out.println(b1.getBalance(u1));
		
		

	}

}
