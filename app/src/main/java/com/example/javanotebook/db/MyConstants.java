package com.example.javanotebook.db;

public class MyConstants {
    /*
    * Конфиг для базы данных
    * */
    public static final String TABLE_NAME = "my_table";
    public static final String _ID = "_id";
    public static final String TITLE = "title";
    public static final String DESC = "desc";
    public static final String URI = "uri";
    public static final String DB_NAME = "my_db.db";
    // версия БД нужна для апгрейда БД без удаления данных
    public static final int DB_VERSION = 2;

    public static final String TABLE_STRUCTURE = "CREATE TABLE IF NOT EXISTS " +
            TABLE_NAME + " (" + _ID + " INTEGER PRIMARY KEY," + TITLE + " TEXT," + URI + " TEXT," +
            DESC + " TEXT)";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

}
