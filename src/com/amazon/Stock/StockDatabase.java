package com.amazon.Stock;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class StockDatabase{
       
       LinkedList<Stock> stockList = new LinkedList<Stock>();
       
       public StockDatabase(){
             
             Stock s1 = new Stock("Amazon",100,500);
             Stock s2 = new Stock("Flipkart",90,500);
             Stock s3 = new Stock("Walmart",80,500);
             Stock s4 = new Stock("Jabong",70,500);
             Stock s5 = new Stock("Myntra",60,500);
             Stock s6 = new Stock("Koovs",50,500);
             Stock s7 = new Stock("Nike",40,500);
             
             stockList.add(s1);
             stockList.add(s2);
             stockList.add(s3);
             stockList.add(s4);
             stockList.add(s5);
             stockList.add(s6);
             stockList.add(s7);
       }
       
      public boolean updateSharesInMarket(String Sharename, String TransactionType, int quantity){
             
             for(Stock stocks : stockList) {
                    if(stocks.Sharename==Sharename) {
                          if(TransactionType=="Buy") {
                                 if(quantity<=0) {
                                        System.out.println("**Please Enter Quantity as more than 1**\n");
                                 }
                                 else if(stocks.Availableshares>quantity) {
                                       stocks.Availableshares = stocks.Availableshares-quantity;
                                       return true;
                                 }
                                 else {
                                        System.out.println("**Requested Number of Shares Not available**\n");
                                       return false;
                                 }
                          }
                          if(TransactionType=="Sell") {
                                       stocks.Availableshares = stocks.Availableshares+quantity;
                                       return true;
                          }
                    }
             }
             return false;
       }
       
       void listShares() {
             for(Object o : stockList) {
                    ((Stock) o).showShareDetails();
             }
       }
       
       void listSpecificShares(String ShareName) {
             stockList.stream().filter(p-> p.Sharename==ShareName).forEach(share->System.out.println(share.showShareDetails()));
       }
       
       public Stock fetchStocks(String Name) {
             List<Stock> filteredProducts = stockList.stream().filter(p-> p.Sharename==Name).collect(Collectors.toList());
             System.out.println(filteredProducts);
             return filteredProducts.get(0);
       }
       
       public boolean checkShare(String Name) {
             List<Stock> filteredProducts = stockList.stream().filter(p-> p.Sharename==Name).collect(Collectors.toList());
             if(filteredProducts.size()==1) {
                    System.out.println("Stock Available");
                    return true;
             }else {
                    System.out.println("Stock Not Available");
                    return false;
             }
             
       }
       
       
}