package com.example.assignment6;

import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import android.content.ContentValues;

public class DBManagement extends SQLiteOpenHelper{
    public static final String DATABASE_NAME = "Store.db";
    public static final String TABLE_NAME = "SpendingTable";
    public static final String COL_NAME_ENUMERATION = "Count";
    public static final String COL_NAME_ITEM = "Item";
    public static final String COL_NAME_DAY = "Day";
    public static final String COL_NAME_MONTH = "Month";
    public static final String COL_NAME_YEAR = "Year";
    public static final String COL_NAME_PRICE = "Price";

    public DBManagement(Context context){
        super(context, DATABASE_NAME, null, 5);
    }

    public void onCreate(SQLiteDatabase sqlDB){
        sqlDB.execSQL("CREATE TABLE " + TABLE_NAME +
                " (Count INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Item varchar(100), Day varchar(100), Month varchar(100), Year varchar(100), Price DOUBLE)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqlDB, int older, int newer){
        sqlDB.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqlDB);
    }


    public boolean createTransaction(Model model){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_NAME_ITEM, model.mItem);
        contentValues.put(COL_NAME_DAY, model.mDay);
        contentValues.put(COL_NAME_MONTH, model.mMonth);
        contentValues.put(COL_NAME_YEAR, model.mYear);
        contentValues.put(COL_NAME_PRICE, model.mPrice);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1) {
            return false;
        }
        return true;
    }

    public Cursor pullData() {
        SQLiteDatabase sqlDB = this.getReadableDatabase();
        Cursor res = sqlDB.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return res;
    }

    public Cursor getFilteredData(String priceFromString, String priceToString, String yearFrom, String yearTo, String monthFrom, String monthTo, String dayFrom, String dayTo){
        SQLiteDatabase sqlDB = this.getReadableDatabase();
        Cursor result = null;

        Double priceFrom = null;
        Double priceTo = null;
        if (!priceFromString.isEmpty())
        {
            priceFrom = Double.parseDouble(priceFromString);
        }
        if (!priceToString.isEmpty())
        {
            priceTo = Double.parseDouble(priceToString);
        }

        // if price from
        if (priceFrom != null && priceTo == null && yearFrom.isEmpty() && yearTo.isEmpty() && monthFrom.isEmpty() && monthTo.isEmpty() && dayFrom.isEmpty() && dayTo.isEmpty())
        {
            result = sqlDB.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE Price >= " + priceFrom, null);
        }
        // if price to
        else if (priceFrom == null && priceTo != null && yearFrom.isEmpty() && yearTo.isEmpty() && monthFrom.isEmpty() && monthTo.isEmpty() && dayFrom.isEmpty() && dayTo.isEmpty())
        {
            result = sqlDB.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE Price <= " + priceTo, null);
        }
        // if date from
        else if (priceFrom == null && priceTo == null && !yearFrom.isEmpty() && yearTo.isEmpty() && !monthFrom.isEmpty() && monthTo.isEmpty() && !dayFrom.isEmpty() && dayTo.isEmpty())
        {
            result = sqlDB.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE Month >= '" + monthFrom + "'", null);
        }
        // if date to
        else if (priceFrom == null && priceTo == null && yearFrom.isEmpty() && !yearTo.isEmpty() && monthFrom.isEmpty() && !monthTo.isEmpty() && dayFrom.isEmpty() && !dayTo.isEmpty())
        {
            result = sqlDB.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE Month <= '" + monthTo + "'", null);
        }
        // if price
        else if (priceFrom != null && priceTo != null && yearFrom.isEmpty() && yearTo.isEmpty() && monthFrom.isEmpty() && monthTo.isEmpty() && dayFrom.isEmpty() && dayTo.isEmpty())
        {
            result = sqlDB.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE Price >= " + priceFrom + " AND Price <= " + priceTo, null);
        }
        // if date
        else if (priceFrom == null && priceTo == null && !yearFrom.isEmpty() && !yearTo.isEmpty() && !monthFrom.isEmpty() && !monthTo.isEmpty() && !dayFrom.isEmpty() && !dayTo.isEmpty())
        {
            result = sqlDB.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE Month >= '" + monthFrom + "' AND Month <= '" + monthTo + "'", null);
        }
        // if priceFrom dateFrom
        else if (priceFrom != null && priceTo == null && !yearFrom.isEmpty() && yearTo.isEmpty() && !monthFrom.isEmpty() && monthTo.isEmpty() && !dayFrom.isEmpty() && dayTo.isEmpty())
        {
            result = sqlDB.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE Price >= " + priceFrom + " AND Month >= '" + monthFrom + "'", null);
        }
        // if priceFrom dateTo
        else if (priceFrom != null && priceTo == null && yearFrom.isEmpty() && !yearTo.isEmpty() && monthFrom.isEmpty() && !monthTo.isEmpty() && dayFrom.isEmpty() && !dayTo.isEmpty())
        {
            result = sqlDB.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE Price >= " + priceFrom + " AND Month <= '" + monthTo + "'", null);
        }
        // if priceTo dateFrom
        else if (priceFrom == null && priceTo != null && !yearFrom.isEmpty() && yearTo.isEmpty() && !monthFrom.isEmpty() && monthTo.isEmpty() && !dayFrom.isEmpty() && dayTo.isEmpty())
        {
            result = sqlDB.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE Price <= " + priceTo + " AND Month >= '" + monthFrom + "'", null);
        }
        // if priceTo dateTo
        else if (priceFrom == null && priceTo != null && yearFrom.isEmpty() && !yearTo.isEmpty() && monthFrom.isEmpty() && !monthTo.isEmpty() && dayFrom.isEmpty() && !dayTo.isEmpty())
        {
            result = sqlDB.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE Price <= " + priceTo + " AND Month <= '" + monthTo + "'", null);
        }
        // if priceFrom priceTo dateFrom
        else if (priceFrom != null && priceTo != null && !yearFrom.isEmpty() && yearTo.isEmpty() && !monthFrom.isEmpty() && monthTo.isEmpty() && !dayFrom.isEmpty() && dayTo.isEmpty())
        {
            result = sqlDB.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE Price >= " + priceFrom + " AND Month <= " + priceTo + " AND Month >= '" + monthFrom + "'", null);
        }
        // if priceFrom priceTo dateTo
        else if (priceFrom != null && priceTo != null && yearFrom.isEmpty() && !yearTo.isEmpty() && monthFrom.isEmpty() && !monthTo.isEmpty() && dayFrom.isEmpty() && !dayTo.isEmpty())
        {
            result = sqlDB.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE Price >= " + priceFrom + " AND Price <= " + priceTo + " AND Month <= '" + monthTo + "'", null);
        }
        // if priceTo dateTo dateFrom
        else if (priceFrom == null && priceTo != null && !yearFrom.isEmpty() && !yearTo.isEmpty() && !monthFrom.isEmpty() && !monthTo.isEmpty() && !dayFrom.isEmpty() && !dayTo.isEmpty())
        {
            result = sqlDB.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE Price <= " + priceTo + " AND Month >= '" + monthFrom + "' AND Month <= '" + monthTo + "'", null);
        }
        // if priceFrom dateTo dateFrom
        else if (priceFrom != null && priceTo == null && !yearFrom.isEmpty() && !yearTo.isEmpty() && !monthFrom.isEmpty() && !monthTo.isEmpty() && !dayFrom.isEmpty() && !dayTo.isEmpty())
        {
            result = sqlDB.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE Price >= " + priceFrom + " AND Month >= '" + monthFrom + "' AND Month <= '" + monthTo +"'", null);
        }
        // if all filters in use
        else if (priceFrom != null && priceTo != null && yearFrom != null && yearTo != null && monthFrom != null && monthTo != null && dayFrom != null && dayTo != null)
        {
            result = sqlDB.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE Price >= " + priceFrom + " AND Price <= " + priceTo + " AND Month >= '" + monthFrom + "' AND Month <= '" + monthTo + "'", null);
        }
        return result;
    }
}
