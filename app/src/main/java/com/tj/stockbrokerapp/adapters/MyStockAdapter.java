package com.tj.stockbrokerapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tj.stockbrokerapp.databinding.CustomMyStockRowBinding;
import com.tj.stockbrokerapp.filters.MyStocksListFilter;
import com.tj.stockbrokerapp.filters.StockListFilter;
import com.tj.stockbrokerapp.models.MyStockModel;
import com.tj.stockbrokerapp.models.StockModel;
import com.tj.stockbrokerapp.pages.StockDetailPage;

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

public class MyStockAdapter extends RecyclerView.Adapter<MyStockAdapter.MyStockHolder> implements Filterable {
    private Context con;
    public List<MyStockModel> myStocksList, filtList;
    private CustomMyStockRowBinding bind;
    private MyStocksListFilter filter;

    private static final String FILE_NAME = "MyStocks.json";

    public MyStockAdapter(Context con, List<MyStockModel> myStocksList) {
        this.con = con;
        this.myStocksList = myStocksList;
        this.filtList = myStocksList;
    }

    @NonNull
    @Override
    public MyStockHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        bind = CustomMyStockRowBinding.inflate(LayoutInflater.from(con), parent, false);
        return new MyStockHolder(bind.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull MyStockHolder holder, int position) {
        MyStockModel myStockMod = myStocksList.get(position);
        String stockSymbol = myStockMod.symbol;
        String stockName = myStockMod.stockName;
        String stockPrice = Double.toString(myStockMod.price);
        String stockCurrency = myStockMod.currency;
        String availableShares = String.valueOf(myStockMod.availableShares);

        File file = new File(con.getFilesDir(), FILE_NAME);

        holder.myStockName.setText(stockName);
        holder.myStockSymbol.setText(stockSymbol);
        holder.myStockPrice.setText(stockPrice);
        holder.myStockCurrency.setText(stockCurrency);
        holder.myStockAvailableShares.setText(availableShares);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Integer.parseInt(availableShares) <= 0) {
                    Toast.makeText(con, "Naaah fam, you dont have enough shares to sell!", Toast.LENGTH_SHORT).show();
                } else {
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
                                    if (availableShares1 <= 0) {
                                        Toast.makeText(con, "Naaah fam, you dont have enough shares to sell!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        jObj.put("availableShares", availableShares1 - 1);
                                        jArrayMyStocks.put(i, jObj);
                                    }
                                }
                            }
                            JSONObject jObjNew = new JSONObject();
                            jObjNew.put("Stocks List", jArrayMyStocks);

                            FileWriter fw = new FileWriter(file.getAbsoluteFile());
                            BufferedWriter bw = new BufferedWriter(fw);
                            bw.write(jObjNew.toString());
                            bw.close();
                            fw.close();

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return myStocksList.size();
    }

    @Override
    public Filter getFilter() {
        if(filter == null) {
            filter = new MyStocksListFilter(filtList, this);
        }
        return filter;
    }

    public class MyStockHolder extends RecyclerView.ViewHolder {

        TextView myStockName, myStockSymbol, myStockPrice, myStockCurrency, myStockAvailableShares;

        public MyStockHolder(@NonNull View itemView) {
            super(itemView);

            myStockName = bind.myStockName;
            myStockSymbol = bind.myStockSymbol;
            myStockPrice = bind.myStockPrice;
            myStockCurrency = bind.myStockCurrency;
            myStockAvailableShares = bind.myStockAvailableShares;
        }
    }
}
