package com.tj.stockbrokerapp.models;

import java.util.Comparator;
import java.util.Locale;

public class MyStockModel{
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


    public static Comparator<MyStockModel> myStocksLowToHigh = new Comparator<MyStockModel>() {
        @Override
        public int compare(MyStockModel t1, MyStockModel t2) {
            return (int) (t1.getPrice() - t2.getPrice());
        }
    };

    public static Comparator<MyStockModel> myStocksHighToLow = new Comparator<MyStockModel>() {
        @Override
        public int compare(MyStockModel t1, MyStockModel t2) {
            return (int) (t2.getPrice() - t1.getPrice());
        }
    };

    public static Comparator<MyStockModel> myStocksSharesLowToHigh = new Comparator<MyStockModel>() {
        @Override
        public int compare(MyStockModel t1, MyStockModel t2) {
            return t1.getAvailableShares() - t2.getAvailableShares();
        }
    };

    public static Comparator<MyStockModel> myStocksSharesHighToLow = new Comparator<MyStockModel>() {
        @Override
        public int compare(MyStockModel t1, MyStockModel t2) {
            return t2.getAvailableShares() - t1.getAvailableShares();
        }
    };

    public static Comparator<MyStockModel> myStocksAToZ = new Comparator<MyStockModel>() {
        @Override
        public int compare(MyStockModel t1, MyStockModel t2) {
            return t1.getStockName().toLowerCase().compareTo(t2.getStockName().toLowerCase());
        }
    };

    public static Comparator<MyStockModel> myStocksZToA = new Comparator<MyStockModel>() {
        @Override
        public int compare(MyStockModel t1, MyStockModel t2) {
            return t2.getStockName().toLowerCase().compareTo(t1.getStockName().toLowerCase());
        }
    };


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
