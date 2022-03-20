package com.tj.stockbrokerapp.pages;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
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

        bind.stockSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                stockAdap.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        bind.stocksListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MySharesList.class);
                MainActivity.this.startActivity(intent);

            }
        });
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