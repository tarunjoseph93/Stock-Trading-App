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
import com.tj.stockbrokerapp.services.RealTimeStocksAPI;

import java.util.Collections;
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

        bind.aToZButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadStocksList();
            }
        });

        bind.zToAButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final RealTimeStocksAPI realTimeStocksAPI = new RealTimeStocksAPI(MainActivity.this);
                realTimeStocksAPI.getAllRealStocksData(new RealTimeStocksAPI.VolleyResponseListenerGetRealTimeStocksData() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(MainActivity.this, "Error: " + message.toString(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(List<StockModel> stockListMods) {
                        Collections.sort(stockListMods, StockModel.myStocksZToA);
                        setAdapter(stockListMods);
                    }
                });
            }
        });

        bind.lowToHighButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final RealTimeStocksAPI realTimeStocksAPI = new RealTimeStocksAPI(MainActivity.this);
                realTimeStocksAPI.getAllRealStocksData(new RealTimeStocksAPI.VolleyResponseListenerGetRealTimeStocksData() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(MainActivity.this, "Error: " + message.toString(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(List<StockModel> stockListMods) {
                        Collections.sort(stockListMods, StockModel.myStocksLowToHigh);
                        setAdapter(stockListMods);
                    }
                });
            }
        });

        bind.highToLowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final RealTimeStocksAPI realTimeStocksAPI = new RealTimeStocksAPI(MainActivity.this);
                realTimeStocksAPI.getAllRealStocksData(new RealTimeStocksAPI.VolleyResponseListenerGetRealTimeStocksData() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(MainActivity.this, "Error: " + message.toString(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(List<StockModel> stockListMods) {
                        Collections.sort(stockListMods, StockModel.myStocksHighToLow);
                        setAdapter(stockListMods);
                    }
                });
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
        final RealTimeStocksAPI realTimeStocksAPI = new RealTimeStocksAPI(MainActivity.this);
        realTimeStocksAPI.getAllRealStocksData(new RealTimeStocksAPI.VolleyResponseListenerGetRealTimeStocksData() {
            @Override
            public void onError(String message) {
                Toast.makeText(MainActivity.this, "Error: " + message.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(List<StockModel> stockListMods) {
                Collections.sort(stockListMods, StockModel.myStocksAToZ);
                setAdapter(stockListMods);
            }
        });
    }

    private void setAdapter(List<StockModel> stockListMods) {
        stockAdap = new StocksListAdapter(MainActivity.this, stockListMods);
        bind.stockRecyclerView.setAdapter(stockAdap);
        stockAdap.notifyDataSetChanged();
    }
}