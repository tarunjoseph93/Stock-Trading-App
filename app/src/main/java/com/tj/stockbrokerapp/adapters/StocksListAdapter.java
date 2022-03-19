package com.tj.stockbrokerapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tj.stockbrokerapp.databinding.CustomStockRowBinding;
import com.tj.stockbrokerapp.filters.StockListFilter;
import com.tj.stockbrokerapp.models.StockModel;
import com.tj.stockbrokerapp.pages.StockDetailPage;

import java.util.ArrayList;
import java.util.List;

public class StocksListAdapter extends RecyclerView.Adapter<StocksListAdapter.StocksListHolder> implements Filterable {
    private Context con;
    public List<StockModel> stocksList, filtList;
    private CustomStockRowBinding bind;
    private StockListFilter filter;

    public StocksListAdapter(Context con, List<StockModel> stocksList) {
        this.con = con;
        this.stocksList = stocksList;
        this.filtList = stocksList;
    }

    @NonNull
    @Override
    public StocksListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        bind = CustomStockRowBinding.inflate(LayoutInflater.from(con), parent, false);
        return new StocksListHolder(bind.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull StocksListHolder holder, int position) {
        StockModel stockMod = stocksList.get(position);
        String stockSymbol = stockMod.symbol;
        String stockName = stockMod.name;
        String stockPrice = Double.toString(stockMod.price);
        String stockCurrency = stockMod.currency;

        holder.stockName.setText(stockSymbol);
        holder.stockPrice.setText("£" + stockPrice);

holder.itemView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent intent = new Intent(con, StockDetailPage.class);
        intent.putExtra("stockSymbol", stockSymbol);
        intent.putExtra("stockName", stockName);
        intent.putExtra("stockPrice", stockPrice);
        intent.putExtra("stockCurrency", stockCurrency);
        con.startActivity(intent);
    }
});


    }

    @Override
    public int getItemCount() {
        return stocksList.size();
    }

    @Override
    public Filter getFilter() {
        if(filter == null) {
            filter = new StockListFilter(filtList, this);
        }
        return filter;
    }

    public class StocksListHolder extends RecyclerView.ViewHolder{
        TextView stockName, stockCurrency, stockPrice;
        public StocksListHolder(@NonNull View itemView) {
            super(itemView);
            stockName = bind.stockSymbol;
            stockPrice = bind.stockPrice;
        }
    }
}
