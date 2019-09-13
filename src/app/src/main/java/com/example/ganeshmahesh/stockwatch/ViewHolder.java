package com.example.ganeshmahesh.stockwatch;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class ViewHolder extends RecyclerView.ViewHolder {
    TextView companySymbol;
    TextView companyName;
    TextView price;
    TextView stockTrend;

    public ViewHolder(View itemView) {
        super(itemView);
        companySymbol = (TextView) itemView.findViewById(R.id.companySymbol);
        companyName = (TextView) itemView.findViewById(R.id.companyName);
        price = (TextView) itemView.findViewById(R.id.price);
        stockTrend = (TextView) itemView.findViewById(R.id.stockTrend);
    }

}
