package com.example.javanotebook.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.javanotebook.adapter.ListItem;

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

    public void insertToDb(String title, String desc, String uri) {
        /*Записывает данные в БД*/
        ContentValues cv = new ContentValues();
        cv.put(MyConstants.TITLE, title);
        cv.put(MyConstants.DESC, desc);
        cv.put(MyConstants.URI, uri);

        // записывает данные в таблицу
        db.insert(MyConstants.TABLE_NAME, null, cv);
    }

    public List<ListItem> getFromDb() {
        /*Считывает данные из БД*/
        List<ListItem> tempList = new ArrayList<>();
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
            ListItem item = new ListItem();
            // выбираем поля из таблицы
            String title = cursor.getString(cursor.getColumnIndex(MyConstants.TITLE));
            String desc = cursor.getString(cursor.getColumnIndex(MyConstants.DESC));
            String uri = cursor.getString(cursor.getColumnIndex(MyConstants.URI));
            item.setTitle(title);
            item.setDesc(desc);
            item.setUri(uri);
            // добавляет в список title, description и uri
            tempList.add(item);
        }
        cursor.close();
        return tempList;
    }

    public void closeDb() {
        /*Закрывает базу данных*/
        myDbHelper.close();
    }

}
