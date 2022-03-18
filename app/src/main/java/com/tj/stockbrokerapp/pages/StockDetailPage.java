package com.tj.stockbrokerapp.pages;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.tj.stockbrokerapp.databinding.ActivityStockDetailPageBinding;

public class StockDetailPage extends AppCompatActivity {

    private ActivityStockDetailPageBinding bind;
    private String stockSymbol,stockName,stockPrice,stockCurrency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = ActivityStockDetailPageBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());

        Intent intent = getIntent();
        stockSymbol = intent.getStringExtra("stockSymbol");
        stockName = intent.getStringExtra("stockName");
        stockPrice = intent.getStringExtra("stockPrice");
        stockCurrency = intent.getStringExtra("stockCurrency");

        bind.stockDetailsSymbol.setText(stockSymbol);
        bind.stockDetailsName.setText(stockName);
        bind.stockDetailsCurrency.setText(stockCurrency);
        bind.stockDetailsPrice.setText(stockPrice);

        bind.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}