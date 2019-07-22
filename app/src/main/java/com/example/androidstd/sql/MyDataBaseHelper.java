package com.example.androidstd.sql;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.widget.Toast;

public class MyDataBaseHelper extends SQLiteOpenHelper {

    private Context mContext;
    private static final String CREATESQL="create table user(id integer primary key autoincrement,name text ,tel text)";
    private static final String CREATESQL2="create table time(id integer primary key autoincrement,name text ,tel text)";

    public MyDataBaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATESQL);
        db.execSQL(CREATESQL2);
        Toast.makeText(mContext, "数据库创建成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists user");
        db.execSQL("drop table if exists time");
        onCreate(db);
    }
}
