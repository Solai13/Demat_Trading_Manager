package com.amazon.Stock;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@SuppressWarnings("serial")
public class StockHandler extends Stock implements Serializable{
       
       public LinkedList<Stock> stockList;
       
       public StockHandler(){
           this.stockList = new LinkedList<Stock>();
     }
       
       public StockHandler(LinkedList<Stock> stockList){
             this.stockList = stockList;
       }

       public void loadCurrentSharePrices() {
           for (Stock stocks : this.stockList) {
               Random r = new Random();
               double randomNumber = r.ints(1, -10, 10).findFirst().getAsInt();
//               System.out.println(randomNumber);
               double updatedPrice = stocks.sharePrice + stocks.sharePrice * (randomNumber / 100);
//               System.out.println(stocks.sharePrice);
               if (updatedPrice > 0.0)
               {
                   stocks.sharePrice = updatedPrice;
//                   System.out.println(stocks.sharePrice);
               }
           }
       }
       
       public boolean updateSharesInMarket(String shareName, String TransactionType, int quantity, double sharePrice){
             
             for(Stock stocks : stockList) {
                    if(stocks.shareName.equalsIgnoreCase(shareName)) {
                          if(TransactionType=="Remove") {
                                 if(quantity<=0) {
                                        System.out.println("**Please Enter Quantity as more than 1**\n");
                                 }
                                 else if(stocks.numberOfShares>=quantity) {
                                       stocks.numberOfShares -= quantity;
                                       stocks.sharePrice = sharePrice;
                                       return true;
                                 }
                                 else {
                                        System.out.println("**Requested Number of Shares Not available**\n");
                                       return false;
                                 }
                          }
                          if(TransactionType=="Add") {
                                       stocks.numberOfShares += quantity;
                                       stocks.sharePrice = sharePrice;
                                       return true;
                          }
                    }
             }
             return false;
       }
       
       public void listShares() {
             for(Stock stock : stockList) {
            	 System.out.println(stock);
             }
       }
       
       public void listSpecificShares(String ShareName) {
             stockList.stream().filter(p-> p.shareName==ShareName).forEach(share->System.out.println(share));
       }
       
       public Stock fetchStocks(String Name) {
             List<Stock> filteredProducts = stockList.stream().filter(p-> p.shareName.equalsIgnoreCase(Name)).collect(Collectors.toList());
             System.out.println(filteredProducts);
             return filteredProducts.get(0);
       }
       
       public boolean checkShare(String Name) {
             List<Stock> filteredProducts = stockList.stream().filter(p-> p.shareName.equalsIgnoreCase(Name)).collect(Collectors.toList());
             if(filteredProducts.size()==1) {
                    System.out.println("Stock Available");
                    return true;
             }else {
                    System.out.println("Stock Not Available with you.");
                    return false;
             }     
       }
       
}