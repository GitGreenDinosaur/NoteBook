package com.xinhai.notebook.data.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.xinhai.notebook.data.Constant;
import com.xinhai.notebook.data.db.bean.Note;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库管理工具类，主要进行CRUD
 */
public class DBManager {

    private static SQLiteDatabase db;


    /**
     * 初始化数据库对象
     */
    public static void initDB(Context context) {
        NoteSQLiteHelper helper = new NoteSQLiteHelper(context); //得到帮助类对象
        db = helper.getWritableDatabase();//得到数据库对象
    }

    /**
     * 向Note表添加内容
     */
    public static void insertItemToNoteTab(Note note) {
        ContentValues values = new ContentValues();
        values.put("uid", note.getUid());
        values.put("title", note.getTitle());
        values.put("content", note.getContent());
        values.put("time", note.getTime());
        db.insert(Constant.TABLE_NAME_NOTE, null, values);
    }

    /**
     * 获取Note表的全部内容
     */
    @SuppressLint("Recycle")
    public static List<Note> getNoteListFromNoteTab() {
        List<Note> noteList = new ArrayList<>();
        Cursor cursor = db.query(Constant.TABLE_NAME_NOTE, null,null,null,null,null,null);
        //遍历结果
        while (cursor.moveToNext()) {
//            int id = cursor.getInt(cursor.getColumnIndex("id"));
            int id = cursor.getInt(0);
            String uid = cursor.getString(1);
            String title = cursor.getString(2);
            String content = cursor.getString(3);
            String time = cursor.getString(4);
            int status = cursor.getInt(5);
            Note note = new Note(id, uid, title,content,time,status);
            noteList.add(note);
        }
        return noteList;
    }

    /**
     * 通过uid删除条目
     */
    public static void deleteItemByUidToNoteTab(String uid) {
//        db.delete(TABLE_NAME_SYNC, "uuid like ?", new String[]{uuid});
        db.delete(Constant.TABLE_NAME_NOTE, "uid like ?",new String[]{uid});
    }

    /**
     * 通过uid更新条目
     * @param note Note对象
     * @return 删除条数
     */
    public static int updateItemByUidToNoteTab(Note note) {
        ContentValues values = new ContentValues();
        values.put("title", note.getTitle());
        values.put("content", note.getContent());
        values.put("time", note.getTime());
        return db.update(Constant.TABLE_NAME_NOTE, values, "uid like ?", new String[]{note.getUid()});
    }

    /**
     * 放入或移出废纸篓
     * @param uid 唯一标识
     * @param status 是否是废纸 0不是 1是 2是私密
     * @return 更新条数
     */
    public static int updateWasterStatusByUidToNote(String uid, int status) {
        ContentValues values = new ContentValues();
        values.put("status", status);
        return db.update(Constant.TABLE_NAME_NOTE, values, "uid like ?", new String[]{uid});
    }

}
