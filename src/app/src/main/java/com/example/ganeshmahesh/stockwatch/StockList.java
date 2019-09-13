package com.example.ganeshmahesh.stockwatch;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

class StockList{
    List<Stock> stockList=new ArrayList<Stock>();

    void stockAdd(String stockSymbol, String companyName){
        stockList.add(new Stock(stockSymbol,companyName));
    }

    void stockAdd(String stockSymbol, String companyName,double price, double priceChange, double changePercentage){
    Stock stock=new Stock(stockSymbol,companyName,price,priceChange,changePercentage);
        stockList.add(stock);
    }
    void removeStockAt(int position){
        stockList.remove(position);
    }
    Stock getStockAt(int position){
        return stockList.get(position);
    }

    public int getSize() {
        return stockList.size();
    }

    public void copyAllFrom(StockList tempStockList) {
        for (int i=0;i<tempStockList.getSize();i++) {
            stockList.add(tempStockList.getStockAt(i));
        }
    }

    public void stockAdd(Stock stock) {
        stockList.add(stock);
        Collections.sort(stockList);


    }

    public boolean isThisSymbolAlreadyPresent(String symbol) {
        for(int i=0;i<stockList.size();i++)
        {
            if(symbol.equals(stockList.get(i).getMyStockSymbol()))
            {
                return true;
            }
        }
        return false;
    }

    public void clear() {
        stockList.clear();
    }
}
