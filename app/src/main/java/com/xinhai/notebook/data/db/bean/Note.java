package com.xinhai.notebook.data.db.bean;

import androidx.annotation.NonNull;

import java.io.Serializable;

/**
 * 笔记内容
 */
public class Note implements Serializable {

    private int id;
    private String uid;
    private String title;
    private String content;
    private int status; //0表示无任何状态 1表示置顶 2表示废纸篓 3表示私密
    private String time;

    public Note() {}

    public Note(int id, String uid, String title, String content, int status, String time) {
        this.id = id;
        this.uid = uid;
        this.title = title;
        this.content = content;
        this.status = status;
        this.time = time;
    }

    @NonNull
    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", uid='" + uid + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", status=" + status +
                ", time='" + time + '\'' +
                '}';
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
