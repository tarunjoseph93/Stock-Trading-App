package com.tj.stockbrokerapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tj.stockbrokerapp.databinding.CustomMyStockRowBinding;
import com.tj.stockbrokerapp.filters.MyStocksListFilter;
import com.tj.stockbrokerapp.filters.StockListFilter;
import com.tj.stockbrokerapp.models.MyStockModel;
import com.tj.stockbrokerapp.models.StockModel;

import java.util.List;

public class MyStockAdapter extends RecyclerView.Adapter<MyStockAdapter.MyStockHolder> implements Filterable {
    private Context con;
    public List<MyStockModel> myStocksList, filtList;
    private CustomMyStockRowBinding bind;
    private MyStocksListFilter filter;

    public MyStockAdapter(Context con, List<MyStockModel> myStocksList) {
        this.con = con;
        this.myStocksList = myStocksList;
        this.filtList = myStocksList;
    }

    @NonNull
    @Override
    public MyStockHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        bind = CustomMyStockRowBinding.inflate(LayoutInflater.from(con), parent, false);
        return new MyStockHolder(bind.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull MyStockHolder holder, int position) {
        MyStockModel myStockMod = myStocksList.get(position);
        String stockSymbol = myStockMod.symbol;
        String stockName = myStockMod.stockName;
        String stockPrice = Double.toString(myStockMod.price);
        String stockCurrency = myStockMod.currency;
        String availableShares = String.valueOf(myStockMod.availableShares);

        holder.myStockName.setText(stockName);
        holder.myStockSymbol.setText(stockSymbol);
        holder.myStockPrice.setText(stockPrice);
        holder.myStockCurrency.setText(stockCurrency);
        holder.myStockAvailableShares.setText(availableShares);
    }

    @Override
    public int getItemCount() {
        return myStocksList.size();
    }

    @Override
    public Filter getFilter() {
        if(filter == null) {
            filter = new MyStocksListFilter(filtList, this);
        }
        return filter;
    }

    public class MyStockHolder extends RecyclerView.ViewHolder {

        TextView myStockName, myStockSymbol, myStockPrice, myStockCurrency, myStockAvailableShares;

        public MyStockHolder(@NonNull View itemView) {
            super(itemView);

            myStockName = bind.myStockName;
            myStockSymbol = bind.myStockSymbol;
            myStockPrice = bind.myStockPrice;
            myStockCurrency = bind.myStockCurrency;
            myStockAvailableShares = bind.myStockAvailableShares;
        }
    }
}
