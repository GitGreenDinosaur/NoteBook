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
        values.put("status", note.getStatus());
        values.put("time", note.getTime());
        db.insert(Constant.TABLE_NAME_NOTE, null, values);
    }

    /**
     * 查询置顶状态
     *
     * @param uid 根据uid查询
     * @return status
     */
    @SuppressLint("Recycle")
    public static int getStatusByUidFromNoteTab(String uid, String tab_name) {
        int status = -1;
        Cursor query = db.query(tab_name, null, null, new String[]{uid}, null, null, null, null);
        if (query.getCount() > 0) {
            while (query.moveToNext()) {
                status = query.getInt(4);
            }
        }
        return status;
    }

    /**
     * 获取Note表的全部内容
     */
    @SuppressLint("Recycle")
    public static List<Note> getNoteListFromTab(String tabName) {
        List<Note> noteList = new ArrayList<>();
        Cursor cursor = db.query(tabName, null, null, null, null, null, "id desc", null);
        //遍历结果
        while (cursor.moveToNext()) {
//            int id = cursor.getInt(cursor.getColumnIndex("id"));
            int id = cursor.getInt(0);
            String uid = cursor.getString(1);
            String title = cursor.getString(2);
            String content = cursor.getString(3);
            int status = cursor.getInt(4);
            String time = cursor.getString(5);
            Note note = new Note(id, uid, title, content, status, time);
            noteList.add(note);
        }
        return noteList;
    }

    /**
     * 通过uid删除条目
     */
    public static void deleteItemByUidFromTab(String uid,String tabName) {
//        db.delete(TABLE_NAME_SYNC, "uuid like ?", new String[]{uuid});
        db.delete(tabName, "uid like ?", new String[]{uid});
    }

    /**
     * 通过uid更新条目
     *
     * @param note Note对象
     * @return 更新条数
     */
    public static int updateItemByUidToNoteTab(Note note) {
        ContentValues values = new ContentValues();
        values.put("title", note.getTitle());
        values.put("content", note.getContent());
        values.put("time", note.getTime());
        return db.update(Constant.TABLE_NAME_NOTE, values, "uid like ?", new String[]{note.getUid()});
    }

    /**
     * 放入废纸篓
     *
     * @param note Note对象
     */
    public static void removeItemToWasterTab(Note note, String tab_name) {
        if (tab_name.equals(""))
            return;
        db.beginTransaction();//开启事务
        try {
            ContentValues values = new ContentValues();
            values.put("uid", note.getUid());
            values.put("title", note.getTitle());
            values.put("content", note.getContent());
            values.put("status", 0);
            values.put("time", note.getTime());
            db.insert(Constant.TABLE_NAME_WASTER, null, values);
            db.delete(tab_name, "uid like ?", new String[]{note.getUid()});
            db.setTransactionSuccessful();//事务成功
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction(); //结束事务
        }
    }

    /**
     * 从废纸篓恢复数据
     *
     * @param note Note对象
     */
    public static void recoverItemToNoteTab(Note note) {
        db.beginTransaction();//开启事务
        try {
            ContentValues values = new ContentValues();
            values.put("uid", note.getUid());
            values.put("title", note.getTitle());
            values.put("content", note.getContent());
            values.put("status", 0);
            values.put("time", note.getTime());
            db.insert(Constant.TABLE_NAME_NOTE, null, values);
            db.delete(Constant.TABLE_NAME_WASTER, "uid like ?", new String[]{note.getUid()});
            db.setTransactionSuccessful();//事务成功
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction(); //结束事务
        }
    }

    /**
     * 置顶内容
     *
     * @param note Note对象
     */
    public static void removeItemToTopTab(Note note) {
        db.beginTransaction();//开启事务
        try {
            ContentValues values = new ContentValues();
            values.put("uid", note.getUid());
            values.put("title", note.getTitle());
            values.put("content", note.getContent());
            values.put("status", 1);
            values.put("time", note.getTime());
            db.insert(Constant.TABLE_NAME_TOP, null, values);
//            db.delete(Constant.TABLE_NAME_NOTE, "uid like ?", new String[]{note.getUid()});
            db.update(Constant.TABLE_NAME_NOTE, values,"uid like ?", new String[]{note.getUid()});
            db.setTransactionSuccessful();//事务成功
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction(); //结束事务
        }
    }

    /**
     * 取消置顶
     *
     * @param note Note对象
     */
    public static void removeItemToNoteTab(Note note) {
        db.beginTransaction();//开启事务
        try {
            ContentValues values = new ContentValues();
            values.put("uid", note.getUid());
            values.put("title", note.getTitle());
            values.put("content", note.getContent());
            values.put("status", 0);
            values.put("time", note.getTime());
            db.update(Constant.TABLE_NAME_NOTE,values, "uid like ?", new String[]{note.getUid()});
            db.delete(Constant.TABLE_NAME_TOP, "uid like ?", new String[]{note.getUid()});
            db.setTransactionSuccessful();//事务成功
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction(); //结束事务
        }
    }

    /**
     * 通过uid更新条目 操作Top表
     *
     * @param note Note对象
     * @return 更新条目
     */
    public static int updateItemByUidToTopTab(Note note) {
        ContentValues values = new ContentValues();
        values.put("title", note.getTitle());
        values.put("content", note.getContent());
        values.put("time", note.getTime());
        return db.update(Constant.TABLE_NAME_TOP, values, "uid like ?", new String[]{note.getUid()});
    }

    /**
     * 内容私密
     *
     * @param note Note对象
     */
    public static void removeItemToPrivateTab(Note note, String tab_name) {
        db.beginTransaction();//开启事务
        try {
            ContentValues values = new ContentValues();
            values.put("uid", note.getUid());
            values.put("title", note.getTitle());
            values.put("content", note.getContent());
            values.put("status", 3);
            values.put("time", note.getTime());
            db.insert(Constant.TABLE_NAME_PRIVATE, null, values);
            db.delete(tab_name, "uid like ?", new String[]{note.getUid()});
            db.setTransactionSuccessful();//事务成功
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction(); //结束事务
        }
    }

    /**
     * 通过uid更新条目 操作private表
     *
     * @param note Note对象
     * @return 更新条数
     */
    public static int updateItemByUidFromPrivateTab(Note note) {
        ContentValues values = new ContentValues();
        values.put("title", note.getTitle());
        values.put("content", note.getContent());
        values.put("time", note.getTime());
        return db.update(Constant.TABLE_NAME_PRIVATE, values, "uid like ?", new String[]{note.getUid()});
    }

}
