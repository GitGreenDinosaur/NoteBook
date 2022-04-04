package com.xinhai.notebook.data.db.bean;

/**
 * 笔记内容
 */
public class Note {

    private int id;
    private String uid;
    private String title;
    private String content;
    private String time;
    private int status; //0：不是废纸   1：是废纸  2：是私密内容

    public Note() {}

    public Note(int id, String uid, String title, String content, String time, int status) {
        this.id = id;
        this.uid = uid;
        this.title = title;
        this.content = content;
        this.time = time;
        this.status = status;
    }

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", uid='" + uid + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", time='" + time + '\'' +
                ", status=" + status +
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
