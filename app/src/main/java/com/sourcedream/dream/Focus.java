package com.sourcedream.dream;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Chronometer;
import androidx.activity.EdgeToEdge;
import android.os.SystemClock;
import android.view.WindowManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Focus extends AppCompatActivity {
    private Chronometer chronometer; // 计时器
    private final int taskId = 123; // 模拟专注任务id
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置全屏 防止锁屏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_focus);

        // 找控件
        chronometer = findViewById(R.id.chronometer);

        // 开始计时
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.setFormat("%H:%M:%S");
        chronometer.start();
    }

    // 服务处于后台时，暂停计时
    @Override
    protected void onPause() {
        super.onPause();
        // 停止计时器
        chronometer.stop();
        // 计算专注时间
        long elapsedMillis = SystemClock.elapsedRealtime() - chronometer.getBase();
        // 获取 SharedPreferences 实例
        SharedPreferences sharedPreferences = getSharedPreferences("FocusData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // 保存数据
        editor.putLong("focusTime", elapsedMillis); // 保存专注时间
        editor.putString("focusDate", new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date())); // 保存日期
        editor.putInt("focusTaskId", taskId);
        // 提交保存
        editor.apply();
        // 退出活动
        finish();
    }
    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 清理资源，如果有必要
    }
}