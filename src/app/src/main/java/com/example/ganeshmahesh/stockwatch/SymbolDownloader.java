package com.example.ganeshmahesh.stockwatch;

import android.net.Uri;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class SymbolDownloader extends AsyncTask<String, Void, StockList> {

    private final String stockURL = "http://stocksearchapi.com";

    private final String myAPIKey = "ad52d419d6d1133ad89b8499a7098d4f722a6b11";
    private  MainActivity mainActivity;
    //this needs to have whatever user entered in dialogue
    private String userEnteredSymbol;

    public SymbolDownloader(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    protected StockList doInBackground(String... params) {
        userEnteredSymbol = params[0];

        Uri.Builder buildURL=Uri.parse(stockURL).buildUpon();

        buildURL.appendPath("api");
        buildURL.appendPath("");
        buildURL.appendQueryParameter("api_key",myAPIKey);

        buildURL.appendQueryParameter("search_text",userEnteredSymbol);
        String urlString=buildURL.build().toString();

        StringBuilder sb = new StringBuilder();
        try {
                URL url= new URL(urlString);

            HttpURLConnection connection=(HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            InputStream inputStream=connection.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(inputStream)));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return parseJSON(sb.toString());
    }

    @Override
    protected void onPostExecute(StockList tempStockList) {
        mainActivity.OnStockSymbolDownloadComplete(tempStockList,userEnteredSymbol);
    }

    private StockList parseJSON(String s) {
        StockList tempStockList = new StockList();
        try {
            JSONArray jsonArray=new JSONArray(s);
            int length=jsonArray.length();
            for (int i=0;i<length;i++)
            {
                JSONObject jObject=jsonArray.getJSONObject(i);
                String companyName = jObject.getString("company_name");
                String companySymbol=jObject.getString("company_symbol");
                tempStockList.stockAdd(companySymbol,companyName);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return tempStockList;
    }
}
