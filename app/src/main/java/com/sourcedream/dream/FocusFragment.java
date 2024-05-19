package com.sourcedream.dream;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.sourcedream.dream.adapters.ListViewAdapter;
import com.sourcedream.dream.beans.ItemFocusBean;

import java.util.ArrayList;
import java.util.List;


public class FocusFragment extends Fragment {
    private RecyclerView focusList;
    private int focusListLength = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_focus, container, false);
        Button btnStartFocus = view.findViewById(R.id.btn_start_focus);
        btnStartFocus.setOnClickListener(new ClickBtn());
        // 找到控件
        focusList = (RecyclerView) view.findViewById(R.id.focus_list);
        // 数据获取
        updateFocusData();
        return view;
    }
    class ClickBtn implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), Focus.class);
            startActivity(intent);
        }
    }
    private  void  updateFocusData() {
        Log.d("YMDEBUG", "mainActivity updateFocusData");
        // List<DataBea>---->Adapter---->setAdapter---->显示数据
        // 创建数据对象
        List<ItemFocusBean> focusData = new ArrayList<>();
        // 创建数据对象
        for(int i=0; i<20;i++) {
            ItemFocusBean data = new ItemFocusBean();
            data.focusDate  = "专注日期";
            data.focusTime = 0;
            data.taskId = 0;
            data.taskName = "任务名称";
            focusData.add(data);
        }
        // 设置样式(布局管理器)
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        focusList.setLayoutManager(layoutManager);
        // 创建适配器
        ListViewAdapter adapter = new ListViewAdapter(0, focusData);
        // 设置适配器
        focusList.setAdapter(adapter);
    }
}