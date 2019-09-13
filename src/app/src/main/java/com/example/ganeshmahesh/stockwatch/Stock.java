package com.example.ganeshmahesh.stockwatch;


import android.support.annotation.NonNull;

class Stock implements Comparable {
    private String myStockSymbol;
    private String myCompanyName;
    private double myPrice;
    private double myPriceChange;
    private boolean myTrendStat;
    private double myChangePercentage;

    public Stock(String myStockSymbol, String myCompanyName) {
        this.myStockSymbol = myStockSymbol;
        this.myCompanyName = myCompanyName;
    }

    //dummydata insertion perposses
    public Stock(String stockSymbol, String companyName, double price, double priceChange, double changePercentage) {

        this.myStockSymbol = stockSymbol;
        this.myCompanyName = companyName;
        this.myPrice = price;
        this.myPriceChange = priceChange;
        this.myChangePercentage = changePercentage;
    }

    public String getMyStockSymbol() {
        return myStockSymbol;
    }

    public void setMyStockSymbol(String myStockSymbol) {
        this.myStockSymbol = myStockSymbol;
    }

    public String getMyCompanyName() {
        return myCompanyName;
    }

    public void setMyCompanyName(String myCompanyName) {
        this.myCompanyName = myCompanyName;
    }

    public double getMyPrice() {
        return myPrice;
    }

    public void setMyPrice(double myPrice) {
        this.myPrice = myPrice;
    }

    public double getMyPriceChange() {
        return myPriceChange;
    }

    public void setMyPriceChange(double myPriceChange) {
        this.myPriceChange = myPriceChange;
    }

    public boolean getmyTrendStatus() {
        myTrendStat = myPriceChange >= 0;
        return myTrendStat;
    }

    public void setmyTrendStatus(double myPercentage) {
        this.myTrendStat = myTrendStat;
    }

    public double getMyChangePercentage() {
        return myChangePercentage;
    }

    public void setMyChangePercentage(double myChangePercentage) {
        this.myChangePercentage = myChangePercentage;
    }
    public String getMyStockChangeValue()
    {
        return myPriceChange+"("+myChangePercentage+")";
    }

    @Override
    public int compareTo(@NonNull Object stock) {

        return this.myStockSymbol.compareTo(((Stock)stock).getMyStockSymbol());
    }
}
