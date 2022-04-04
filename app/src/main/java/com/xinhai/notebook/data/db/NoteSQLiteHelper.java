package com.xinhai.notebook.data.db;

import static com.xinhai.notebook.data.Constant.*;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;
import com.xinhai.notebook.data.Constant;

public class NoteSQLiteHelper extends SQLiteOpenHelper {

    private static final String CREATE_NOTE_TABLE_SQL = "create table "
            + TABLE_NAME_NOTE
            + " ( id integer primary key autoincrement,"
            + " uid text,"
            + " title text,"
            + " content text,"
            + " time text,"
            + " status integer)";

    private static final String CREATE_USER_TABLE_SQL = "create table "
            + TABLE_NAME_USER
            + " ( id integer primary key autoincrement,"
            + " email text,"
            + " name text,"
            + " pass text,"
            + " role integer)";

    private static final String CREATE_SYNC_TABLE_SQL = "create table "
            + TABLE_NAME_SYNC
            + " ( id integer primary key autoincrement,"
            + " uid text,"
            + " time text,"
            + " status blob)";

    public NoteSQLiteHelper(@Nullable Context context) {
        super(context, Constant.DB_NAME, null, Constant.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_NOTE_TABLE_SQL); //创建note表
        db.execSQL(CREATE_USER_TABLE_SQL); //创建user表
        db.execSQL(CREATE_SYNC_TABLE_SQL); //创建sync表
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
