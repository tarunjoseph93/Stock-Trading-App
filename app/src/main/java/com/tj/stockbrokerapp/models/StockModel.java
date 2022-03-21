package com.tj.stockbrokerapp.models;

import java.util.Comparator;

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

    public static Comparator<StockModel> myStocksLowToHigh = new Comparator<StockModel>() {
        @Override
        public int compare(StockModel t1, StockModel t2) {
            return (int) (t1.getPrice() - t2.getPrice());
        }
    };

    public static Comparator<StockModel> myStocksHighToLow = new Comparator<StockModel>() {
        @Override
        public int compare(StockModel t1, StockModel t2) {
            return (int) (t2.getPrice() - t1.getPrice());
        }
    };

    public static Comparator<StockModel> myStocksAToZ = new Comparator<StockModel>() {
        @Override
        public int compare(StockModel t1, StockModel t2) {
            return t1.getName().toLowerCase().compareTo(t2.getName().toLowerCase());
        }
    };

    public static Comparator<StockModel> myStocksZToA = new Comparator<StockModel>() {
        @Override
        public int compare(StockModel t1, StockModel t2) {
            return t2.getName().toLowerCase().compareTo(t1.getName().toLowerCase());
        }
    };

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
