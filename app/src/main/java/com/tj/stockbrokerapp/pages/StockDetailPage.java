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
    private String currencyCodeSelected, currencyRateSelected = "1";

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

        String updatedAvailableShares = null;
        try {
            updatedAvailableShares = updateAvailableShares();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        bind.stockDetailsAvailableShares.setText(updatedAvailableShares);

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

        bind.autoConvertCurrencyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                autoConvertCurrency();
            }
        });

        bind.listMySharesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(StockDetailPage.this, MySharesList.class);
                StockDetailPage.this.startActivity(intent1);
            }
        });

        bind.buyShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FileReader fileReader = null;
                BufferedReader bufferedReader = null;

                long timestamp = System.currentTimeMillis();
                String timestampModified = Long.toString(timestamp);
                String response = "";

                if (file.exists()) {

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
                        fileReader.close();

                        JSONObject jObjParent = new JSONObject(response);
                        JSONArray jArrayMyStocks = jObjParent.getJSONArray("Stocks List");

                        for (int i = 0; i < jArrayMyStocks.length(); i++) {

                            JSONObject jObj = jArrayMyStocks.getJSONObject(i);
                            String symb = jObj.getString("symbol");

                            if (symb.equals(stockSymbol)) {
                                int availableShares1 = jObj.getInt("availableShares");
                                int newAvailableShares = availableShares1 + 1;
                                jObj.put("availableShares", newAvailableShares);
                                jArrayMyStocks.put(i, jObj);
                            }
                        }
                        JSONObject jObjNew = new JSONObject();
                        jObjNew.put("Stocks List", jArrayMyStocks);

                        FileWriter fw = new FileWriter(file.getAbsoluteFile());
                        BufferedWriter bw = new BufferedWriter(fw);
                        bw.write(jObjNew.toString());
                        bw.close();
                        fw.close();
                        bind.stockDetailsAvailableShares.setText(updateAvailableShares());

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(StockDetailPage.this, "File DOES NOT exist!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void autoConvertCurrency() {
        double rateFrom = Double.parseDouble(stockPrice);
        double rateTo = Double.parseDouble(currencyRateSelected);
        double results = rateFrom/rateTo;
        String res = String.format("%.2f",results);
        bind.stockDetailsPrice.setText(res);
        bind.stockDetailsCurrency.setText(bind.stockDetailsCurrencyConvert.getText().toString());
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
                        currencyRateSelected = Double.toString(currencyModelList.get(i).currencyRate);
                        bind.stockDetailsCurrencyConvert.setText(currencyCodeSelected);
                        bind.stockDetailsCurrencyRate.setText(currencyRateSelected);
                    }
                }).show();
            }
        });
    }

    private String updateAvailableShares() throws JSONException {
        String result = "";

        File file = new File(this.getFilesDir(), FILE_NAME);
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        String response = "";

        if (file.exists()) {
            StringBuffer output = new StringBuffer();
            try {
                fileReader = new FileReader(file.getAbsolutePath());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            bufferedReader = new BufferedReader(fileReader);
            String line = "";

            while (true) {
                try {
                    if (!((line = bufferedReader.readLine()) != null)) break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                output.append(line + "\n");
            }

            response = output.toString();
            try {
                bufferedReader.close();
                fileReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            JSONObject jObjParent = new JSONObject(response);
            JSONArray jArrayParent = jObjParent.getJSONArray("Stocks List");
            for (int i = 0; i < jArrayParent.length(); i++) {
                JSONObject jObj = jArrayParent.getJSONObject(i);
                String symb = jObj.getString("symbol");

                if (symb.equals(stockSymbol)) {
                    int availableShares1 = jObj.getInt("availableShares");
                    result = String.valueOf(availableShares1);
                }
            }

        }
        return result;
    }
}