package com.tj.stockbrokerapp.services;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.tj.stockbrokerapp.models.StockModel;
import com.tj.stockbrokerapp.singletons.StocksApiSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RealTimeStocksAPI {
    Context con;

    public interface VolleyResponseListenerGetRealTimeStocksData {
        void onError(String message);

        void onResponse(List<StockModel> stockListMods);
    }

    public RealTimeStocksAPI(Context con) {
        this.con = con;
    }

    public static final String REAL_STOCKS_LIST_API = "https://yfapi.net/v6/finance/quote?region=US&lang=en&symbols=UWMC%2CX%2CQRTEA%2CEBAY%2CSAGE%2CEQT%2CUPST%2CBXP%2CDLR%2CTRGP";

    public void getAllRealStocksData(VolleyResponseListenerGetRealTimeStocksData getRealStocksVRL) {
        List<StockModel> stockModel = new ArrayList<>();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, REAL_STOCKS_LIST_API, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONObject jObjParent = response.getJSONObject("quoteResponse");
                    JSONArray jArrayParent = jObjParent.getJSONArray("result");
                    for(int i = 0; i < jArrayParent.length(); i++) {
                        StockModel stockModel1 = new StockModel();
                        JSONObject getStockItem =(JSONObject) jArrayParent.get(i);
                        stockModel1.setName(getStockItem.getString("longName"));
                        stockModel1.setSymbol(getStockItem.getString("symbol"));
                        stockModel1.setPrice(getStockItem.getDouble("regularMarketPrice"));
                        stockModel1.setCurrency(getStockItem.getString("currency"));
                        stockModel.add(stockModel1);
                    }
                    getRealStocksVRL.onResponse(stockModel);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error is ", "" + error);
                Toast.makeText(con, "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
                getRealStocksVRL.onError(error.toString());
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("accept", "application/json");
                params.put("X-API-KEY", "wwX4lnCvMB5Q3OnszZqhqXxO9LhOH6b7n6fX7Sbg");
                return params;
            }
        };
        StocksApiSingleton.getInstance(con).addToRequestQueue(request);
    }
}
