package com.tj.stockbrokerapp.pages;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.tj.stockbrokerapp.adapters.StocksListAdapter;
import com.tj.stockbrokerapp.databinding.ActivityMainBinding;
import com.tj.stockbrokerapp.models.StockModel;
import com.tj.stockbrokerapp.services.StocksDataAPI;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding bind;
    private StocksListAdapter stockAdap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());

        loadStocksList();

   }

    private void loadStocksList() {
        final StocksDataAPI stocksAPI = new StocksDataAPI(MainActivity.this);
        stocksAPI.getAllStocksList(new StocksDataAPI.VolleyResponseListenerGetStocksList() {
            @Override
            public void onError(String message) {
                Toast.makeText(MainActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(List<StockModel> stockListMods) {
                stockAdap = new StocksListAdapter(MainActivity.this, stockListMods);
                bind.stockRecyclerView.setAdapter(stockAdap);
            }
        });
    }
}