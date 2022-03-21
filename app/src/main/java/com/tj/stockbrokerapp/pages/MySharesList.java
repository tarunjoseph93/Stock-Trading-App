package com.tj.stockbrokerapp.pages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import java.util.Collections;
import java.util.List;

public class MySharesList extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private ActivityMySharesListBinding bind;
    private List<MyStockModel> myStockModel;
    private MyStockAdapter myStockAdap;
    SwipeRefreshLayout mSwipeRefreshLayout;

    private static final String FILE_NAME = "MyStocks.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = ActivityMySharesListBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());

        mSwipeRefreshLayout = bind.swipeRefreshRecyclerView;
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_purple,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
        mSwipeRefreshLayout.post(new Runnable() {

            @Override
            public void run() {

                mSwipeRefreshLayout.setRefreshing(true);

                try {
                    loadMySharesList();
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        bind.aToZButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    loadMySharesList();
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        bind.zToAButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSwipeRefreshLayout.setRefreshing(true);

                myStockModel = new ArrayList<>();

                File file = new File(MySharesList.this.getFilesDir(), FILE_NAME);
                FileReader fileReader = null;
                BufferedReader bufferedReader = null;

                String response = "";

                if(file.exists()) {
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
                }
                Collections.sort(myStockModel, MyStockModel.myStocksZToA);
                setAdapter(myStockModel);
            }
        });

        bind.lowToHighButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSwipeRefreshLayout.setRefreshing(true);

                myStockModel = new ArrayList<>();

                File file = new File(MySharesList.this.getFilesDir(), FILE_NAME);
                FileReader fileReader = null;
                BufferedReader bufferedReader = null;

                String response = "";

                if(file.exists()) {
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
                }
                Collections.sort(myStockModel, MyStockModel.myStocksLowToHigh);
                setAdapter(myStockModel);
            }
        });

        bind.highToLowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSwipeRefreshLayout.setRefreshing(true);

                myStockModel = new ArrayList<>();

                File file = new File(MySharesList.this.getFilesDir(), FILE_NAME);
                FileReader fileReader = null;
                BufferedReader bufferedReader = null;

                String response = "";

                if(file.exists()) {
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
                }
                Collections.sort(myStockModel, MyStockModel.myStocksHighToLow);
                setAdapter(myStockModel);
            }
        });

        bind.sharesLowToHighButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSwipeRefreshLayout.setRefreshing(true);

                myStockModel = new ArrayList<>();

                File file = new File(MySharesList.this.getFilesDir(), FILE_NAME);
                FileReader fileReader = null;
                BufferedReader bufferedReader = null;

                String response = "";

                if(file.exists()) {
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
                }
                Collections.sort(myStockModel, MyStockModel.myStocksSharesLowToHigh);
                setAdapter(myStockModel);
            }
        });

        bind.sharesHighToLowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSwipeRefreshLayout.setRefreshing(true);

                myStockModel = new ArrayList<>();

                File file = new File(MySharesList.this.getFilesDir(), FILE_NAME);
                FileReader fileReader = null;
                BufferedReader bufferedReader = null;

                String response = "";

                if(file.exists()) {
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
                }
                Collections.sort(myStockModel, MyStockModel.myStocksSharesHighToLow);
                setAdapter(myStockModel);
            }
        });

        bind.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

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

    private void loadMySharesList() throws IOException, JSONException {

        mSwipeRefreshLayout.setRefreshing(true);

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
            fileReader.close();

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
        }
        else {
            String assetsString = loadJSONFromAsset();
            JSONObject jsonObjectParent = new JSONObject(assetsString);

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(jsonObjectParent.toString());
            bw.close();
            fw.close();

            StringBuffer output = new StringBuffer();
            fileReader = new FileReader(file.getAbsolutePath());
            bufferedReader = new BufferedReader(fileReader);
            String line = "";

            while ((line = bufferedReader.readLine()) != null) {
                output.append(line + "\n");
            }

            response = output.toString();
            bufferedReader.close();
            fileReader.close();

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
        }
        Collections.sort(myStockModel, MyStockModel.myStocksAToZ);
        setAdapter(myStockModel);

    }

    private void setAdapter(List<MyStockModel> myStocksList) {
        myStockAdap = new MyStockAdapter(MySharesList.this, myStocksList);
        bind.myStockRecyclerView.setAdapter(myStockAdap);
        mSwipeRefreshLayout.setRefreshing(false);
        myStockAdap.notifyDataSetChanged();
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("MyStocks.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    @Override
    public void onRefresh() {
        try {
            loadMySharesList();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }
}