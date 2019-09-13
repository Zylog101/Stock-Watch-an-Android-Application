package com.example.ganeshmahesh.stockwatch;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {
    SwipeRefreshLayout swiper;
    StockList stockList=new StockList();
    RecyclerView recyclerView;
    Adapter recyclerViewAdapter;
    DatabaseHandler databaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        recyclerViewAdapter=new Adapter(stockList,this);

        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        swiper = (SwipeRefreshLayout)findViewById(R.id.swiperefresh);
        databaseHandler=new DatabaseHandler(this);
        swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(!IsConnectionAvailable())
                {
                    swiper.setRefreshing(false);
                    ShowNoNetworkAvailableDialogue();

                    return;
                }

                stockList.clear();
                StockList tempStockList=databaseHandler.loadStocks();
                if(tempStockList.getSize()==0)
                {
                    //stockList=new StockList();
                }
                else
                {

                    for(int i=0;i<tempStockList.getSize();i++)
                    {
                        StockDownload stockDownload = new StockDownload(MainActivity.this);
                        String symbol=tempStockList.getStockAt(i).getMyStockSymbol();
                        String companyName=tempStockList.getStockAt(i).getMyCompanyName();
                        stockDownload.execute(symbol,companyName);
                    }

                }
                swiper.setRefreshing(false);
            }
        });
        if(!IsConnectionAvailable())
        {
            ShowNoNetworkAvailableDialogue();
            return;
        }

        StockList tempStockList=databaseHandler.loadStocks();
        if(tempStockList.getSize()==0)
        {
            //stockList=new StockList();
        }
        else
        {

            for(int i=0;i<tempStockList.getSize();i++)
            {
                StockDownload stockDownload = new StockDownload(this);
                String symbol=tempStockList.getStockAt(i).getMyStockSymbol();
                String companyName=tempStockList.getStockAt(i).getMyCompanyName();
                stockDownload.execute(symbol,companyName);
            }

        }

    }
    boolean IsConnectionAvailable()
    {
        ConnectivityManager connectivityManager =(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo currentNetwork=connectivityManager.getActiveNetworkInfo();
        return currentNetwork!=null&&currentNetwork.isConnectedOrConnecting();
    }
    void OnStockSymbolDownloadComplete(final StockList tempStockList, String userEnteredSymbol)
    {
        int tempStockListLength=tempStockList.getSize();
        if(tempStockListLength==0)
        {
            AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("Data for stock symbol");
            builder.setTitle("Symbol Not Found: "+userEnteredSymbol);
            AlertDialog dialog = builder.create();
            dialog.show();

        }
        else if(tempStockListLength==1) {
            String symbol=tempStockList.getStockAt(0).getMyStockSymbol();
            String companyName=tempStockList.getStockAt(0).getMyCompanyName();

            if(stockList.isThisSymbolAlreadyPresent(symbol))
            {
                AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                builder.setIcon(R.drawable.ic_warning_dialogue);
                builder.setTitle("Duplicate Stock");
                builder.setMessage("Stock Symbol: "+symbol+" is already displayed");
                builder.create().show();
                return;
            }
            StockDownload stockDownload = new StockDownload(this);
            stockDownload.execute(symbol,companyName);
            return;
        }
        else {

            //case where multiple items are found for the symbol
            final CharSequence[] arrayOfStockListToDislplay = new CharSequence[tempStockListLength];
            for (int i = 0; i < tempStockListLength; i++) {
                arrayOfStockListToDislplay[i] = tempStockList.getStockAt(i).getMyStockSymbol() + " " + tempStockList.getStockAt(i).getMyCompanyName();
            }
            //Dialog is created for selection of stock
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Make A Selection");
            builder.setItems(arrayOfStockListToDislplay, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String symbol = tempStockList.getStockAt(which).getMyStockSymbol();
                    String companyName = tempStockList.getStockAt(which).getMyCompanyName();
                    if(stockList.isThisSymbolAlreadyPresent(symbol))
                    {
                        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("Duplicate Stock");
                        builder.setMessage("Stock Symbol: "+symbol+" is already displayed");
                        builder.create().show();
                        return;
                    }

                    //Stock data is fetched for the perticular stock Data
                    StockDownload stockDownload = new StockDownload(MainActivity.this);
                    stockDownload.execute(symbol, companyName);
                    return;
                }
            });
            builder.setNegativeButton("NEVERMIND", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //nothing to do here
                }
            });
            AlertDialog dialog = builder.create();

            dialog.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(!IsConnectionAvailable())
        {
            ShowNoNetworkAvailableDialogue();
            return true;
        }
        //pop up dialogue asking for the name of the stock symbol
        final AlertDialog.Builder builder=new AlertDialog.Builder(this);
        final EditText symbolEditTextView=new EditText(this);
        symbolEditTextView.setInputType(InputType.TYPE_CLASS_TEXT);
        symbolEditTextView.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        symbolEditTextView.setGravity(Gravity.CENTER_HORIZONTAL);
        builder.setView(symbolEditTextView);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //async task code to find symbol goes here
                //following code for handle case of not found must be placed in async task which will download the symbol
                SymbolDownloader symbolDownloader=new SymbolDownloader(MainActivity.this);
                symbolDownloader.execute(symbolEditTextView.getText().toString());
            }
        });
        builder.setNegativeButton("CANCEL",new DialogInterface.OnClickListener(){


            @Override
            public void onClick(DialogInterface dialog, int which) {
                //User Didnt want to add stock
            }
        });
        builder.setTitle("StockSelection");
        builder.setMessage("Please enter a Stock Symbol:");
        AlertDialog dialog=builder.create();
        dialog.show();
        return true;

    }

    private void ShowNoNetworkAvailableDialogue() {
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setTitle("No Network Connection");
        builder.setMessage("Stock Connot Be Updated Without Network Connection");
        builder.create().show();
    }

    @Override
    public void onClick(View v) {
        String url="http://www.marketwatch.com/investing/stock/";
        url+=stockList.getStockAt(recyclerView.getChildLayoutPosition(v)).getMyStockSymbol();

        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        //Intent intent
    }

    @Override
    public boolean onLongClick(View view) {
        final int position = recyclerView.getChildLayoutPosition(view);
        final String stockSymbol= stockList.getStockAt(position).getMyStockSymbol();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                databaseHandler.deleteStock(stockSymbol);
                stockList.removeStockAt(position);
                recyclerViewAdapter.notifyDataSetChanged();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });

        builder.setMessage("Delete Stock Symbol " + stockList.getStockAt(position).getMyStockSymbol() + "?");
        builder.setTitle("Delete Stock");

        AlertDialog dialog = builder.create();
        dialog.show();
        return true;
    }

    public void OnStockDataDownloadComplete(Stock stock) {
        if(stock==null)
        {
            AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("NO stock (no match found)");
            //builder.setTitle("Symbol Not Found: "+symbol);
            AlertDialog dialog = builder.create();
            dialog.show();

        }
        stockList.stockAdd(stock);
        databaseHandler.addStock(stock);
        recyclerViewAdapter.notifyDataSetChanged();

    }
}
