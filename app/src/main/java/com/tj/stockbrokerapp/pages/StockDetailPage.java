package com.tj.stockbrokerapp.pages;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.tj.stockbrokerapp.databinding.ActivityStockDetailPageBinding;
import com.tj.stockbrokerapp.models.CurrencyModel;
import com.tj.stockbrokerapp.services.CurrencyConversionAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class StockDetailPage extends AppCompatActivity {

    private ActivityStockDetailPageBinding bind;
    private String stockSymbol,stockName,stockPrice,stockCurrency;
    private String currencyCodeSelected, currencyRateSelected;

    private static final String FILE_NAME = "MyStocks.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = ActivityStockDetailPageBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());

        File file = new File(this.getFilesDir(), FILE_NAME);

        Intent intent = getIntent();
        stockSymbol = intent.getStringExtra("stockSymbol");
        stockName = intent.getStringExtra("stockName");
        stockPrice = intent.getStringExtra("stockPrice");
        stockCurrency = intent.getStringExtra("stockCurrency");

        bind.stockDetailsSymbol.setText(stockSymbol);
        bind.stockDetailsName.setText(stockName);
        bind.stockDetailsCurrency.setText(stockCurrency);
        bind.stockDetailsPrice.setText(stockPrice);

        bind.stockDetailsCurrencyConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickCurrencyCode();
            }
        });

        bind.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        bind.buyShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FileReader fileReader = null;
                FileWriter fileWriter = null;
                BufferedReader bufferedReader = null;
                BufferedWriter bufferedWriter = null;

                long timestamp = System.currentTimeMillis();
                String timestampModified = Long.toString(timestamp);
                String response = "";

                if(!file.exists()){
                    try {
                        file.createNewFile();
                        fileWriter = new FileWriter(file.getAbsoluteFile());
                        bufferedWriter = new BufferedWriter(fileWriter);

                        JSONObject jObjStockData = new JSONObject();
                        try {
                            jObjStockData.put("symbol", stockSymbol);
                            jObjStockData.put("name", stockName);
                            jObjStockData.put("price", stockPrice);
                            jObjStockData.put("availableShares", "1");
                            jObjStockData.put("timestamp", timestampModified);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        JSONArray jArrayMyStocks = new JSONArray();
                        jArrayMyStocks.put(jObjStockData);

                        JSONObject jObjParent = new JSONObject();
                        try {
                            jObjParent.put("My Stocks", jArrayMyStocks);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        bufferedWriter.write(jObjParent.toString());
                        bufferedWriter.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                //check if JSON Object for symbol exists in JSON Array
                StringBuffer output = new StringBuffer();
                try {
                    fileReader = new FileReader(file.getAbsolutePath());
                    bufferedReader = new BufferedReader(fileReader);
                    String line = "";

                    while ((line = bufferedReader.readLine()) != null) {
                        output.append(line + "\n");
                    }

                    response = output.toString();
                    bufferedReader.close();

                    JSONObject jObjParent = new JSONObject(response);
                    JSONArray jArrayMyStocks = jObjParent.getJSONArray("My Stocks");

                    for(int i = 0; i < jArrayMyStocks.length(); i++) {
                        fileWriter = new FileWriter(file.getAbsoluteFile());


                        JSONObject jObj = jArrayMyStocks.getJSONObject(i);
                        String symb = jObj.getString("symbol");
                        if(symb.equals(stockSymbol)) {

                            BufferedWriter bw = new BufferedWriter(fileWriter);

                            String availableShares1 = jObj.getString("availableShares");
                            int convertedShareString = Integer.parseInt(availableShares1);
                            int newAvailableShares = convertedShareString + 1;
                            String newAvailableSharesConverted = String.valueOf(newAvailableShares);
                            jObj.put("availableShares", newAvailableSharesConverted);

                            jArrayMyStocks.put(i, jObj);

                            jObjParent.put("My Stocks", jArrayMyStocks);
                            bw.write(jObjParent.toString());
                            bw.close();

                        }
                    }
                    BufferedWriter buffRed = new BufferedWriter(fileWriter);

                    JSONObject jObjStockDataNew = new JSONObject();
                    try {
                        jObjStockDataNew.put("symbol", stockSymbol);
                        jObjStockDataNew.put("name", stockName);
                        jObjStockDataNew.put("price", stockPrice);
                        jObjStockDataNew.put("availableShares", "1");
                        jObjStockDataNew.put("timestamp", timestampModified);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    jArrayMyStocks.put(jObjStockDataNew);
                    JSONObject jObjParentNew = new JSONObject();
                    jObjParentNew.put("My Stocks", jArrayMyStocks);
                    buffRed.write(jObjParentNew.toString());
                    buffRed.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void pickCurrencyCode() {
        final CurrencyConversionAPI currencyConversionAPI = new CurrencyConversionAPI(StockDetailPage.this);
        currencyConversionAPI.getAllCurrencyCodes(new CurrencyConversionAPI.VolleyResponseListenerGetCurrencyCodesList() {
            @Override
            public void onError(String message) {
                Toast.makeText(StockDetailPage.this, "Error: " + message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(List<CurrencyModel> currencyModelList) {
                String[] currencyArray = new String[currencyModelList.size()];
                for(int i = 0; i < currencyModelList.size(); i++) {
                    currencyArray[i] = currencyModelList.get(i).currencyName;
                }
                AlertDialog.Builder ad = new AlertDialog.Builder(StockDetailPage.this);
                ad.setTitle("Choose your currency: ").setItems(currencyArray, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        currencyCodeSelected = currencyModelList.get(i).currencyCode;
                        bind.stockDetailsCurrencyConvert.setText(currencyCodeSelected);
                    }
                }).show();
            }
        });

        
    }
}