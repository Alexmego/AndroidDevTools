package com.hm.tools.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public static final String db_name = "score.db";
    //    public static final int db_version = 20180414;
//    public static final int db_version = 20180415;
    public static final int db_version = 20180416;
    public static DBHelper DB_HELPER = null;

    private DBHelper(Context context) {
        super(context, db_name, null, db_version);
    }


    public static synchronized DBHelper getInstance(Context context) {
        if (DB_HELPER == null) {
            DB_HELPER = new DBHelper(context);
        }
        return DB_HELPER;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create = "CREATE TABLE IF NOT EXISTS mach (id integer PRIMARY KEY AUTOINCREMENT,name varchar(20),aaage integer )";
        db.execSQL("CREATE TABLE IF NOT EXISTS hello (id integer PRIMARY KEY AUTOINCREMENT,myname varchar(20))");
        db.execSQL(create);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 20180415) {
            db.execSQL("CREATE TABLE IF NOT EXISTS hello (id integer PRIMARY KEY AUTOINCREMENT,myname varchar(20))");
        }
        if (oldVersion < 20180416) {
            db.execSQL("ALTER table mach add column aaage integer");
        }
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);

    }
}
