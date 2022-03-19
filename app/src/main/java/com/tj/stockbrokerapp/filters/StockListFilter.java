package com.tj.stockbrokerapp.filters;

import android.widget.Filter;

import com.tj.stockbrokerapp.adapters.StocksListAdapter;
import com.tj.stockbrokerapp.models.StockModel;

import java.util.ArrayList;
import java.util.List;

public class StockListFilter extends Filter {

    List<StockModel> filterList;
    StocksListAdapter stocksAdap;

    public StockListFilter(List<StockModel> filterList, StocksListAdapter stocksAdap) {
        this.filterList = filterList;
        this.stocksAdap = stocksAdap;
    }

    @Override
    protected FilterResults performFiltering(CharSequence charSequence) {
        FilterResults res = new FilterResults();
        if(charSequence != null && charSequence.length() > 0) {
            charSequence = charSequence.toString().toUpperCase();
            List<StockModel> filtMods = new ArrayList<>();
            for(int i = 0; i < filterList.size(); i++) {
                if(filterList.get(i).getSymbol().toUpperCase().contains(charSequence)) {
                    filtMods.add(filterList.get(i));
                }
            }
            res.count = filtMods.size();
            res.values = filtMods;
        }
        else {
            res.count = filterList.size();
            res.values = filterList;
        }
        return res;
    }

    @Override
    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
        stocksAdap.stocksList = (List<StockModel>)filterResults.values;
        stocksAdap.notifyDataSetChanged();
    }
}
