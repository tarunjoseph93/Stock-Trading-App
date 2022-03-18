package com.tj.stockbrokerapp.services;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.tj.stockbrokerapp.models.StockModel;
import com.tj.stockbrokerapp.singletons.StocksApiSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StocksDataAPI {
    Context con;

    public interface VolleyResponseListenerGetStocksList {
        void onError(String message);

        void onResponse(List<StockModel> stockListMods);
    }

    public static final String STOCKS_LIST_API = "http://10.0.2.2:8080/StocksListTest/webresources/stocksList";

    public StocksDataAPI(Context con) {
        this.con = con;
    }

    public void getAllStocksList(VolleyResponseListenerGetStocksList stockListRL) {
        List<StockModel> stockModel = new ArrayList<>();
        JsonObjectRequest jObjReq = new JsonObjectRequest(Request.Method.GET, STOCKS_LIST_API, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray stocksList = response.getJSONArray("Stocks List");
                    for(int i = 0; i < stocksList.length(); i++) {
                        StockModel stockModel1 = new StockModel();
                        JSONObject getStockItem =(JSONObject) stocksList.get(i);
                        stockModel1.setName(getStockItem.getString("name"));
                        stockModel1.setPrice(getStockItem.getDouble("price"));
                        stockModel1.setCurrency(getStockItem.getString("currency"));
                        stockModel1.setSymbol(getStockItem.getString("symbol"));
                        stockModel.add(stockModel1);
                    }
                    stockListRL.onResponse(stockModel);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(con, "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
                stockListRL.onError(error.toString());
            }
        });
        StocksApiSingleton.getInstance(con).addToRequestQueue(jObjReq);
    }
}
