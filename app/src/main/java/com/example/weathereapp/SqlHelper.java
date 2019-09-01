package com.example.weathereapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SqlHelper extends SQLiteOpenHelper {
    String tableName="City";
    List<String> cities;
    public SqlHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_QUERY="CREATE TABLE " +tableName +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT ,name TEXT)";
        db.execSQL(CREATE_TABLE_QUERY);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public boolean CITY_EXISTS(String cityName){
        SQLiteDatabase db=this.getReadableDatabase();
        Boolean CityExists = false;
        try
        {
            String GET_CITY_NAME_QUERY="SELECT name FROM " + tableName + " WHERE name ='" + cityName + "'";
            Cursor cursor=db.rawQuery(GET_CITY_NAME_QUERY,null);

            while (cursor.moveToNext()){
                CityExists = true;
            }

            cursor.close();
        }catch (Exception ex){

        }finally {
           // db.close();
        }

        return CityExists ;
    }

    public  void INSERT_CITY(String name) {
        SQLiteDatabase db=this.getWritableDatabase();

        try{
            if(CITY_EXISTS(name)) return;
            String INSERT_CITY = "INSERT INTO " + tableName + "(name) VALUES ('" + name +"')";
            db.execSQL(INSERT_CITY);
        }catch (Exception ex){

        }finally {
            db.close();
        }
    }

    public List<String> SELECT_CITY()
    {
        SQLiteDatabase db=this.getReadableDatabase();
        cities=new ArrayList<>();

        try {
            String GET_CITY_NAME_QUERY="SELECT  name FROM " + tableName;
            Cursor cursor=db.rawQuery(GET_CITY_NAME_QUERY,null);
            if (cursor.moveToLast()){
            do {
                cities.add(cursor.getString(0));
            }while (cursor.moveToPrevious());}
            cursor.close();
        }catch (Exception ex){

        }finally {
            db.close();
        }
        return cities;
    }
}
