package com.example.ganeshmahesh.stockwatch;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Adapter extends RecyclerView.Adapter<ViewHolder> {

    private static final String TAG = "StockAdapter";
    private StockList stockList;
    private MainActivity mainActivity;

    public Adapter(StockList stockList, MainActivity mainActivity) {
        this.stockList = stockList;
        this.mainActivity = mainActivity;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.stock_list_row, parent, false);
        itemView.setOnClickListener(mainActivity);
        itemView.setOnLongClickListener(mainActivity);

        return new ViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        boolean stockTrendStatus;
        Stock stock = stockList.getStockAt(position);

        holder.companySymbol.setText(stock.getMyStockSymbol());
        holder.companyName.setText(stock.getMyCompanyName());
        holder.price.setText(String.valueOf(stock.getMyPrice()));
        String stockTrendStatusText;

        stockTrendStatus = stock.getmyTrendStatus();
        if (stockTrendStatus) {
            stockTrendStatusText = "\u25B2";
            updateColor(holder, mainActivity.getColor(R.color.colorStockUp));
        } else {
            stockTrendStatusText = "\u25Bc";
            updateColor(holder, mainActivity.getColor(R.color.colorStockDown));
        }
        holder.stockTrend.setText(stockTrendStatusText + " " + stock.getMyStockChangeValue());
    }

    private void updateColor(ViewHolder holder, int color) {
        holder.companySymbol.setTextColor(color);
        holder.companyName.setTextColor(color);
        holder.price.setTextColor(color);
        holder.stockTrend.setTextColor(color);
    }


    @Override
    public int getItemCount() {
        return stockList.getSize();
    }
}
