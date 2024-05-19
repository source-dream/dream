package com.sourcedream.dream;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;

import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sourcedream.dream.database.DatabaseHelper;

public class MainActivity extends AppCompatActivity {
    private  BottomNavigationView bottomNavigationView;
    private  FocusFragment focusFragment;
    private  ListFragment listFragment;
    private MyFragment myFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("YMDEBUG", "MainActivity OnCreate ");
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // 初始化控件
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        // 设置底部导航栏的监听事件
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if(item.getItemId()==R.id.focus){
                selectedFragment(0);
            } else if(item.getItemId()==R.id.list){
                selectedFragment(1);
            } else if(item.getItemId()==R.id.setting){
                selectedFragment(2);
            }
            return true;
        });
        // 默认首页选中
        bottomNavigationView.setSelectedItemId(R.id.focus);
        selectedFragment(0);
    }
    private void selectedFragment(int position){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        hideFragment(fragmentTransaction);
        if(position==0) {
            if(focusFragment==null){
                focusFragment = new FocusFragment();
                fragmentTransaction.add(R.id.content, focusFragment);
            } else {
                fragmentTransaction.show(focusFragment);
            }
        } else if(position==1) {
            if(listFragment==null){
                listFragment = new ListFragment();
                fragmentTransaction.add(R.id.content, listFragment);
            } else {
                fragmentTransaction.show(listFragment);
            }
        } else if(position==2) {
            if(myFragment ==null){
                myFragment = new MyFragment();
                fragmentTransaction.add(R.id.content, myFragment);
            } else {
                fragmentTransaction.show(myFragment);
            }
        }
        fragmentTransaction.commit();
    }
    private  void hideFragment(FragmentTransaction fragmentTransaction){
        if(myFragment !=null){
            fragmentTransaction.hide(myFragment);
        }
        if(listFragment!=null){
            fragmentTransaction.hide(listFragment);
        }
        if(focusFragment!=null) {
            fragmentTransaction.hide(focusFragment);
        }
    }

    @Override // 重写 onResume 方法
    protected void onResume() {
        super.onResume();
//        Log.d("YMDEBUG", "MainActivity OnResume ");
        // 获取 SharedPreferences 实例
        SharedPreferences sharedPreferences = getSharedPreferences("FocusData", MODE_PRIVATE);
        long focusTime = sharedPreferences.getLong("focusTime", 0);
        String focusDate = sharedPreferences.getString("focusDate", "");
        int taskId = sharedPreferences.getInt("focusTaskId", -1);
//        Log.d("YMDEBUG", "taskId: " + taskId + " focusTime: " + focusTime + " focusDate: " + focusDate);

        if (taskId != -1) {
            // 显示对话框
            AlertDialog dialog = getAlertDialog(taskId, focusTime, sharedPreferences);
            dialog.show();
            if(focusTime<60*1000) {
                return;
            }
            // 将专注时间保存到数据库
            try (DatabaseHelper dbHelper = new DatabaseHelper(this);
                 SQLiteDatabase db = dbHelper.getWritableDatabase()) {
                ContentValues values = new ContentValues();
                values.put(DatabaseHelper.COLUMN_FOCUS_TIME, focusTime); // 使用正确的列名
                values.put(DatabaseHelper.COLUMN_FOCUS_DATE, focusDate); // 使用正确的列名
                values.put(DatabaseHelper.COLUMN_TASK_ID, taskId); // 使用正确的列名
                db.insert(DatabaseHelper.FOCUS_TABLE_NAME, null, values); // 使用正确的表名
            } catch (Exception e) {
                Log.e("YMDEBUG", "Database operation failed", e);
            }

        }
    }

    @NonNull
    private AlertDialog getAlertDialog(int taskId, long focusTime, SharedPreferences sharedPreferences) {
        AlertDialog.Builder builder = getBuilder(taskId, focusTime, sharedPreferences);
        if(focusTime<60*1000) {
            builder.setPositiveButton("好的", (dialog, which) -> {
                Toast.makeText(MainActivity.this, "继续努力哦！", Toast.LENGTH_SHORT).show();
            });
            sharedPreferences.edit().clear().apply();
            return builder.create();
        }
        builder.setNegativeButton("否", (dialog, which) -> {
            Toast.makeText(MainActivity.this, "继续努力！", Toast.LENGTH_SHORT).show();
            // 清除 SharedPreferences 中的数据
            sharedPreferences.edit().clear().apply();
        });
        return builder.create();
    }

    @NonNull
    private AlertDialog.Builder getBuilder(int taskId, long focusTime, SharedPreferences sharedPreferences) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if(focusTime<60*1000) {
            builder.setTitle("专注任务无效");
            builder.setMessage("专注时间过短(一分钟以上才有效)，本次专注任务无效！");
        } else {
            builder.setTitle("专注任务完成");
            builder.setMessage("目标任务ID: " + taskId + "\n专注累计时间: " + focusTime / 1000 + "秒\n是否完成了任务？");
            builder.setPositiveButton("是", (dialog, which) -> {
                Toast.makeText(MainActivity.this, "任务完成！", Toast.LENGTH_SHORT).show();
                // 清除 SharedPreferences 中的数据
                sharedPreferences.edit().clear().apply();
            });
        }
        return builder;
    }
}