package com.example.javanotebook.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class MyDbManager {
    /*
    * Вспомогательный класс для работы с БД
    * */
    private Context context;
    private MyDbHelper myDbHelper;
    private SQLiteDatabase db;

    public MyDbManager(Context context) {
        this.context = context;
        myDbHelper = new MyDbHelper(context);
    }

    public void openDb() {
        /*Открывает подключение к БД*/
        db = myDbHelper.getWritableDatabase();
    }

    public void insertToDb(String title, String desc) {
        /*Записывает данные в БД*/
        ContentValues cv = new ContentValues();
        cv.put(MyConstants.TITLE, title);
        cv.put(MyConstants.DESC, desc);

        // записывает данные в таблицу
        db.insert(MyConstants.TABLE_NAME, null, cv);
    }

    public List<String> getFromDb() {
        /*Считывает данные из БД*/
        List<String> tempList = new ArrayList<>();
        Cursor cursor = db.query(
                MyConstants.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
        while(cursor.moveToNext()) {
            String title = cursor.getString(cursor.getColumnIndex(MyConstants.TITLE));
            tempList.add(title);
        }
        cursor.close();
        return tempList;
    }

    public void closeDb() {
        /*Закрывает базу данных*/
        myDbHelper.close();
    }

}
