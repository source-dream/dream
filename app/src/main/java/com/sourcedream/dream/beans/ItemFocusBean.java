package com.sourcedream.dream.beans;

public class ItemFocusBean {
    public long focusTime;
    public String focusDate;
    public  int taskId;
    public String taskName;
    // 构造函数
    public ItemFocusBean(long focusTime, String focusDate, int taskId, String taskName) {
        this.focusTime = focusTime;
        this.focusDate = focusDate;
        this.taskId = taskId;
        this.taskName = taskName;
    }
}
