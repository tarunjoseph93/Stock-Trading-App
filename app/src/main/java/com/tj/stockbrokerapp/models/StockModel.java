package com.tj.stockbrokerapp.models;

public class StockModel {
    public String name;
    public double price;
    public String currency;
    public String symbol;

    public StockModel() {
    }

    public StockModel(String name, double price, String currency, String symbol) {
        this.name = name;
        this.price = price;
        this.currency = currency;
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
