package com.xinhai.notebook.data.db.bean;

/**
 * 同步类
 */
public class Sync {

    private int id;
    private String uuid;
    private String time;
    private int status; //同步状态 0：未同步   1：已同步

    @Override
    public String toString() {
        return "Sync{" +
                "id=" + id +
                ", uuid='" + uuid + '\'' +
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
}
