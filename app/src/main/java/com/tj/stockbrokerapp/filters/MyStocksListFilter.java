package com.tj.stockbrokerapp.filters;

import android.widget.Filter;

import com.tj.stockbrokerapp.adapters.MyStockAdapter;
import com.tj.stockbrokerapp.adapters.StocksListAdapter;
import com.tj.stockbrokerapp.models.MyStockModel;
import com.tj.stockbrokerapp.models.StockModel;

import java.util.ArrayList;
import java.util.List;

public class MyStocksListFilter extends Filter {
    List<MyStockModel> filterList;
    MyStockAdapter myStocksAdap;

    public MyStocksListFilter(List<MyStockModel> filterList, MyStockAdapter myStocksAdap) {
        this.filterList = filterList;
        this.myStocksAdap = myStocksAdap;
    }

    @Override
    protected FilterResults performFiltering(CharSequence charSequence) {
        FilterResults res = new FilterResults();
        if(charSequence != null && charSequence.length() > 0) {
            charSequence = charSequence.toString().toUpperCase();
            List<MyStockModel> filtMods = new ArrayList<>();
            for(int i = 0; i < filterList.size(); i++) {
                if(filterList.get(i).getStockName().toUpperCase().contains(charSequence)) {
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
        myStocksAdap.myStocksList = (List<MyStockModel>)filterResults.values;
        myStocksAdap.notifyDataSetChanged();
    }
}
