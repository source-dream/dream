package com.sourcedream.dream.beans;

public class ItemTaskBean {
    public Integer id;
    public String name;
    public Integer status;
    public Integer level;
    public String deadline;
    public Integer focusTime; // 任务专注时间
    // 构造函数
    public ItemTaskBean(int id, String name, int status, int level, String deadline, int focusTime) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.level = level;
        this.deadline = deadline;
        this.focusTime = focusTime;
    }
}
