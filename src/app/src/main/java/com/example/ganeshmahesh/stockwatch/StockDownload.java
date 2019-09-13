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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

class StockDownload extends AsyncTask<String, Void, Void> {
    private final String stockURL = "http://finance.google.com";
    private MainActivity mainActivity;
    private String userEnteredSymbol;
    private String stockCompanyName;
    Stock tempStock=null;

    public StockDownload(MainActivity mainActivity) {
        this.mainActivity = mainActivity;


    }

    private void parseJSON(String s) {
        try {
            s = s.replace("//", "");
            JSONArray jsonArray = new JSONArray(s);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            String lastTradePrice = jsonObject.getString("l");
            String priceChangeAmount = jsonObject.getString("c");
            String priceChangePercentage = jsonObject.getString("cp");
            String ticker=jsonObject.getString("t");
            if(ticker.equals(userEnteredSymbol)) {
                tempStock = new Stock(userEnteredSymbol, stockCompanyName, Double.parseDouble(lastTradePrice), Double.parseDouble(priceChangeAmount), Double.parseDouble(priceChangePercentage));
            }
            else
            {
                tempStock=null;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected Void doInBackground(String... params) {
        userEnteredSymbol = params[0];
        stockCompanyName = params[1];
        Uri.Builder builder = Uri.parse(stockURL).buildUpon();
        builder.appendPath("finance");
        builder.appendPath("info");
        builder.appendQueryParameter("client", "ig");

        builder.appendQueryParameter("q", userEnteredSymbol);
        String urlString = builder.build().toString();

        StringBuilder sb = new StringBuilder();

        try {
            URL url = new URL(urlString);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bufferReader.readLine()) != null) {
                sb.append(line).append('\n');
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        parseJSON(sb.toString());

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {

        mainActivity.OnStockDataDownloadComplete(tempStock);
    }
}
