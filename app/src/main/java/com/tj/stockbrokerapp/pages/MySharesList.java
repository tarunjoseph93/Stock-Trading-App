package com.tj.stockbrokerapp.pages;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.tj.stockbrokerapp.adapters.MyStockAdapter;
import com.tj.stockbrokerapp.databinding.ActivityMySharesListBinding;
import com.tj.stockbrokerapp.models.MyStockModel;

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
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MySharesList extends AppCompatActivity {

    private ActivityMySharesListBinding bind;
    private List<MyStockModel> myStockModel;
    private MyStockAdapter myStockAdap;

    private static final String FILE_NAME = "MyStocks.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = ActivityMySharesListBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());

        try {
            loadMySharesList();
        } catch (IOException e) {
            e.printStackTrace();
        }

        bind.myStockSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                myStockAdap.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        bind.myStocksHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MySharesList.this, MainActivity.class);
                MySharesList.this.startActivity(intent);
            }
        });
    }

    private void loadMySharesList() throws IOException {
        myStockModel = new ArrayList<>();
        File file = new File(this.getFilesDir(), FILE_NAME);
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        String response = "";

        if(file.exists()) {
            StringBuffer output = new StringBuffer();
            fileReader = new FileReader(file.getAbsolutePath());
            bufferedReader = new BufferedReader(fileReader);
            String line = "";

            while ((line = bufferedReader.readLine()) != null) {
                output.append(line + "\n");
            }

            response = output.toString();
            bufferedReader.close();

            try {
                JSONObject jObjParent = new JSONObject(response);
                JSONArray jArrayParent = jObjParent.getJSONArray("Stocks List");
                for (int i = 0; i < jArrayParent.length(); i++) {
                    MyStockModel myStockMod = new MyStockModel();
                    JSONObject getMyStockItem = (JSONObject) jArrayParent.get(i);
                    myStockMod.setStockName(getMyStockItem.getString("name"));
                    myStockMod.setPrice(getMyStockItem.getDouble("price"));
                    myStockMod.setSymbol(getMyStockItem.getString("symbol"));
                    myStockMod.setCurrency(getMyStockItem.getString("currency"));
                    myStockMod.setAvailableShares(getMyStockItem.getInt("availableShares"));
                    myStockModel.add(myStockMod);
                }
            } catch(JSONException e) {
                e.printStackTrace();
            }
        } //add else load from assets over here

        myStockAdap = new MyStockAdapter(MySharesList.this, myStockModel);
        bind.myStockRecyclerView.setAdapter(myStockAdap);
    }

}