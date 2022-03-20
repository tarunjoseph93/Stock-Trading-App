package com.tj.stockbrokerapp.models;

public class MyStockModel {
    public String stockName;
    public double price;
    public String currency;
    public String symbol;
    public int availableShares;

    public MyStockModel() {
    }

    public MyStockModel(String stockName, double price, String currency, String symbol, int availableShares) {
        this.stockName = stockName;
        this.price = price;
        this.currency = currency;
        this.symbol = symbol;
        this.availableShares = availableShares;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public int getAvailableShares() {
        return availableShares;
    }

    public void setAvailableShares(int availableShares) {
        this.availableShares = availableShares;
    }
}
