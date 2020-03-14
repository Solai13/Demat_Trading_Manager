package com.amazon.Stock;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class StockHandler{
       
       public LinkedList<Stock> stockList;
       
       public StockHandler(LinkedList<Stock> stockList){
             this.stockList = stockList;
       }
       
       public boolean updateSharesInMarket(String Sharename, String TransactionType, int quantity){
             
             for(Stock stocks : stockList) {
                    if(stocks.Sharename==Sharename) {
                          if(TransactionType=="Add") {
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
                          if(TransactionType=="Remove") {
                                       stocks.Availableshares = stocks.Availableshares+quantity;
                                       return true;
                          }
                    }
             }
             return false;
       }
       
       public void listShares() {
             for(Stock stock : stockList) {
            	 stock.showShareDetails();
             }
       }
       
       public void listSpecificShares(String ShareName) {
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
