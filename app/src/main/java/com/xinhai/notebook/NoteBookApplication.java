package com.xinhai.notebook;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import com.xinhai.notebook.data.db.DBManager;

public class NoteBookApplication extends Application {

    @SuppressLint("StaticFieldLeak")
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        DBManager.initDB(getApplicationContext());
    }

    //返回
    public static Context getContext(){
        return context;
    }

}
