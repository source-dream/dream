package com.sourcedream.dream.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sourcedream.dream.beans.ItemFocusBean;
import com.sourcedream.dream.beans.ItemTaskBean;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "FocusDatabase.db";
    public static final int DATABASE_VERSION = 1;

    public static final String FOCUS_TABLE_NAME = "focus"; // 专注记录表
    public static final String COLUMN_ID = "id"; // 专注记录id
    public static final String COLUMN_FOCUS_TIME = "focus_time"; // 专注时间
    public static final String COLUMN_FOCUS_DATE = "focus_date"; // 专注完成日期
    public static final String COLUMN_TASK_ID = "task_id"; // 专注任务id

    public static final String TASK_TABLE_NAME = "task"; //  任务表
    public static final String TASK_COLUMN_TASK_ID = "id"; // 任务id
    public static final String TASK_COLUMN_TASK_NAME = "name"; // 任务名称
    public static final String TASK_COLUMN_TASK_STATUS = "status"; // 任务状态
    public static final  String TASK_COLUMN_TASK_FOCUS_TIME = "focus_time"; // 任务专注时间
    public static final String TASK_COLUMN_TASK_DATE = "date"; // 任务创建日期
    public static final String TASK_COLUMN_TASK_DEADLINE = "deadline"; // 任务截至日期
    public static final String TASK_COLUMN_TASK_LEVEL = "level";// 任务重要性

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    // 创建数据库
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createFocusTable = "CREATE TABLE " + FOCUS_TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," // 专注id
                + COLUMN_FOCUS_TIME + " LONG," // 专注时间
                + COLUMN_FOCUS_DATE + " TEXT," //  专注完成日期
                + COLUMN_TASK_ID + " INTEGER, " // 专注任务id
                + "FOREIGN KEY(" + COLUMN_TASK_ID + ") REFERENCES " + TASK_TABLE_NAME + "(" + TASK_COLUMN_TASK_ID + ")" // 外键约束
                + ")";
        db.execSQL(createFocusTable);
        String createTaskTable = "CREATE TABLE " + TASK_TABLE_NAME + "("
                + TASK_COLUMN_TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," // 任务id
                + TASK_COLUMN_TASK_NAME + " TEXT," // 任务名称
                + TASK_COLUMN_TASK_STATUS + " INTEGER," // 任务状态
                + TASK_COLUMN_TASK_FOCUS_TIME + " LONG," // 任务专注时间
                + TASK_COLUMN_TASK_DATE + " TEXT," //  任务创建日期
                + TASK_COLUMN_TASK_DEADLINE + " TEXT," // 任务截至日期
                + TASK_COLUMN_TASK_LEVEL + " INTEGER" // 任务重要性
                + ")";
        db.execSQL(createTaskTable);
        insertInitData(db); // 插入初始化数据
    }

    // 添加任务数据
    public int addTask(SQLiteDatabase db, String name, String data, String deadline,int level ) {
        ContentValues values = new ContentValues();
        values.put(TASK_COLUMN_TASK_NAME, name); // 任务名称
        values.put(TASK_COLUMN_TASK_STATUS, 0); // 任务状态
        values.put(TASK_COLUMN_TASK_FOCUS_TIME, 0); // 任务专注时间
        values.put(TASK_COLUMN_TASK_DATE, data); // 任务创建日期
        values.put(TASK_COLUMN_TASK_DEADLINE, deadline); // 任务截至日期
        values.put(TASK_COLUMN_TASK_LEVEL, level); // 任务重要性
        db.insert(TASK_TABLE_NAME, null, values); // 插入数据
        return (int) db.compileStatement("SELECT last_insert_rowid()").simpleQueryForLong(); // 返回插入数据的id
    }
    // 更新任务数据
    public void updateTask(SQLiteDatabase db, int taskId, String taskName, String taskDate, String taskDeadline) {
        ContentValues values = new ContentValues();
        values.put(TASK_COLUMN_TASK_NAME, taskName); // 任务名称
        values.put(TASK_COLUMN_TASK_DATE, taskDate); // 任务创建日期
        values.put(TASK_COLUMN_TASK_DEADLINE, taskDeadline); // 任务截至日期
        db.update(TASK_TABLE_NAME, values, TASK_COLUMN_TASK_ID + " = ?", new String[]{String.valueOf(taskId)}); // 更新数据
    }

    // 添加专注数据
    public void addFocus(SQLiteDatabase db, long focusTime, String focusDate, int taskId) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_FOCUS_TIME, focusTime); // 专注时间
        values.put(COLUMN_FOCUS_DATE, focusDate); // 专注完成日期
        values.put(COLUMN_TASK_ID, taskId); // 专注任务id
        db.insert(FOCUS_TABLE_NAME, null, values); // 插入数据
    }
    // 插入初始化数据
    public void insertInitData(SQLiteDatabase db) {
        // 添加任务数据
        int taskId1 = addTask(db, "任务1", "2021-01-01", "2021-01-10", 1);
        int taskId2 = addTask(db, "任务2", "2021-01-02", "2021-01-11", 2);
        int taskId3 = addTask(db, "任务3", "2021-01-03", "2021-01-12", 3);
        int taskId4 = addTask(db, "任务4", "2021-01-04", "2021-01-13", 4);
        int taskId5 = addTask(db, "任务5", "2021-01-05", "2021-01-14", 5);

        // 添加专注数据
        addFocus(db, 1000 * 60 * 25, "2021-01-01", 1);
        addFocus(db, 1000 * 60 * 25, "2021-01-02", 1);
        addFocus(db, 1000 * 60 * 25, "2021-01-03", 1);
        addFocus(db, 1000 * 60 * 25, "2021-01-04", 1);
        addFocus(db, 1000 * 60 * 25, "2021-01-05", 1);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    // 获取任务数据
    @SuppressLint("Range")
    public List<ItemTaskBean> getTaskData() {
        // 创建任务数据对象
        List<ItemTaskBean> taskList = new ArrayList<>();
        // 获取数据库
        try (SQLiteDatabase db = getReadableDatabase();
             // 创建游标
             Cursor cursor = db.query(TASK_TABLE_NAME, null, null, null, null, null, null)) {
            // 遍历游标
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndex(TASK_COLUMN_TASK_ID));
                    String name = cursor.getString(cursor.getColumnIndex(TASK_COLUMN_TASK_NAME));
                    int status = cursor.getInt(cursor.getColumnIndex(TASK_COLUMN_TASK_STATUS));
                    int level = cursor.getInt(cursor.getColumnIndex(TASK_COLUMN_TASK_LEVEL));
                    String deadline = cursor.getString(cursor.getColumnIndex(TASK_COLUMN_TASK_DEADLINE));
                    int focusTime = cursor.getInt(cursor.getColumnIndex(TASK_COLUMN_TASK_FOCUS_TIME));
                    taskList.add(new ItemTaskBean(id, name, status, level, deadline, focusTime));
                } while (cursor.moveToNext());
            }
        }
        return taskList;
    }
    // 获取专注数据
    @SuppressLint("Range")
    public List<ItemFocusBean> getFocusData() {
        List<ItemFocusBean> focusList = new ArrayList<>();
        try (SQLiteDatabase db = getReadableDatabase();
             Cursor cursor = db.query(FOCUS_TABLE_NAME, null, null, null, null, null, null)) {
            if (cursor.moveToFirst()) {
                do {
                    long focusTime = cursor.getLong(cursor.getColumnIndex(COLUMN_FOCUS_TIME));
                    String focusDate = cursor.getString(cursor.getColumnIndex(COLUMN_FOCUS_DATE));
                    int taskId = cursor.getInt(cursor.getColumnIndex(COLUMN_TASK_ID));
                    String taskName = "任务名字";
                    focusList.add(new ItemFocusBean(focusTime, focusDate, taskId, taskName));
                } while (cursor.moveToNext());
            }
        }
        return focusList;
    }
}