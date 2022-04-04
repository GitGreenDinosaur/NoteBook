package com.xinhai.notebook.data.db.bean;

/**
 * 同步类
 */
public class Sync {

    private int id;
    private String uuid;
    private String time;
    private boolean status; //同步状态true false

    @Override
    public String toString() {
        return "Sync{" +
                "id=" + id +
                ", uuid='" + uuid + '\'' +
                ", time='" + time + '\'' +
                ", status=" + status +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
