package com.tj.stockbrokerapp.services;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.tj.stockbrokerapp.models.CurrencyModel;
import com.tj.stockbrokerapp.models.StockModel;
import com.tj.stockbrokerapp.singletons.StocksApiSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CurrencyConversionAPI {
    Context con;

    public static final String CURRENCY_CONV_API = "http://10.0.2.2:8080/CurConvRS/webresources/curCodes";

    public interface VolleyResponseListenerGetCurrencyCodesList {
        void onError(String message);

        void onResponse(List<CurrencyModel> currencyModelList);
    }

    public CurrencyConversionAPI(Context con) {
        this.con = con;
    }

    public void getAllCurrencyCodes(VolleyResponseListenerGetCurrencyCodesList currencyCodesVRL) {
        List<CurrencyModel> currencyModel = new ArrayList<>();
        JsonObjectRequest jObjReq = new JsonObjectRequest(Request.Method.GET, CURRENCY_CONV_API, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray currencyList = response.getJSONArray("CurrConv");
                    for(int i = 0; i < currencyList.length(); i++) {
                        CurrencyModel currencyModel1 = new CurrencyModel();
                        JSONObject getCurrencyItem =(JSONObject) currencyList.get(i);
                        currencyModel1.setCurrencyName(getCurrencyItem.getString("name"));
                        currencyModel1.setCurrencyCode(getCurrencyItem.getString("code"));
                        currencyModel1.setCurrencyRate(getCurrencyItem.getDouble("rate"));
                        currencyModel.add(currencyModel1);
                    }
                    currencyCodesVRL.onResponse(currencyModel);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(con, "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
                currencyCodesVRL.onError(error.toString());
            }
        });
        StocksApiSingleton.getInstance(con).addToRequestQueue(jObjReq);
    }
}
