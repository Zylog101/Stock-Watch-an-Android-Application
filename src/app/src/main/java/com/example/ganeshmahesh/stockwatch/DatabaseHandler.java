package com.example.ganeshmahesh.stockwatch;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "StockTable";
    private static final String DATABASE_NAME = "StockWatchAppDB";
    private static final String COMPANY_SYMBOL = "CompanySymbol";
    private static final String COMPANY_NAME = "CompanyName";

    private SQLiteDatabase database;
    //SQL Query to create Table
    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COMPANY_SYMBOL + " TEXT not null unique," +
                    COMPANY_NAME + " TEXT not null)";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        database = getWritableDatabase();

    }

    public StockList loadStocks() {
        StockList stockList = new StockList();
        Cursor cursor = database.query(
                TABLE_NAME,// The table to query
                new String[]{COMPANY_SYMBOL, COMPANY_NAME},// The columns to return
                null, // The columns for the WHERE clause
                null, // The values for the WHERE clause
                null, // don't group the rows
                null, // don't filter by row groups
                null); // The sort order

        if (cursor != null) {
            cursor.moveToFirst();
        }

        for (int i = 0; i < cursor.getCount(); i++) {
            String companySymbol = cursor.getString(0);
            String companyName = cursor.getString(1);
            stockList.stockAdd(companySymbol, companyName);
            cursor.moveToNext();
        }
        cursor.close();
        return stockList;
    }

    public void addStock(Stock stock) {
        ContentValues values = new ContentValues();
        values.put(COMPANY_SYMBOL, stock.getMyStockSymbol());
        values.put(COMPANY_NAME, stock.getMyCompanyName());

        deleteStock(stock.getMyStockSymbol());
        database.insert(TABLE_NAME, null, values);
    }

    public void deleteStock(String name) {
        database.delete(TABLE_NAME, COMPANY_SYMBOL + " = ?", new String[]{name});
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //creation of table and initial population of table should happen here
        db.execSQL(SQL_CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //called when database needs to be upgraded
    }
}
