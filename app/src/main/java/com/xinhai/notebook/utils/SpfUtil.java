package com.xinhai.notebook.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.xinhai.notebook.NoteBookApplication;

public class SpfUtil {
    private static final String SPF_NAME = "noteBookSpf";
    public static void saveString(String key, String value){
        SharedPreferences spf = NoteBookApplication.getContext().getSharedPreferences(SPF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = spf.edit();
        editor.putString(key, value);
        editor.apply();
    }
    public static String getString(String key){
        SharedPreferences spf = NoteBookApplication.getContext().getSharedPreferences(SPF_NAME,Context.MODE_PRIVATE);
        return spf.getString(key,"");
    }
    public static void saveInt(String key, int value){
        SharedPreferences spf = NoteBookApplication.getContext().getSharedPreferences(SPF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = spf.edit();
        editor.putInt(key,value);
        editor.apply();
    }
    public static int getInt(String key){
        SharedPreferences spf = NoteBookApplication.getContext().getSharedPreferences(SPF_NAME,Context.MODE_PRIVATE);
        return spf.getInt(key,-1);
    }
    public static int getIntWithDefault(String key,int defValue){
        SharedPreferences spf = NoteBookApplication.getContext().getSharedPreferences(SPF_NAME,Context.MODE_PRIVATE);
        return spf.getInt(key, defValue);
    }


}
