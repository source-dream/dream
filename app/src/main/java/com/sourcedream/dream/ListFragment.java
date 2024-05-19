package com.sourcedream.dream;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sourcedream.dream.adapters.ListViewAdapter;
import com.sourcedream.dream.beans.ItemFocusBean;
import com.sourcedream.dream.beans.ItemTaskBean;

import java.util.ArrayList;
import java.util.List;


public class ListFragment extends Fragment {
    private RecyclerView taskList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        taskList = (RecyclerView) view.findViewById(R.id.task_list);
        // 数据获取
        updateTaskData();

        return view;
    }

    private void updateTaskData() {
        Log.d("YMDEBUG", "mainActivity updateTaskData");
        // List<DataBea>---->Adapter---->setAdapter---->显示数据
        // 创建数据对象
        List<ItemTaskBean> taskData = new ArrayList<>();
        // 创建数据对象
        for(int i=0; i<20;i++) {
            ItemTaskBean data = new ItemTaskBean();
            data.id = 0;
            data.name = "任务名称";
            data.status = 0;
            data.focusTime = 0;
            data.deadline = "截至日期";
            taskData.add(data);
        }
        // 设置样式(布局管理器)
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        taskList.setLayoutManager(layoutManager);

        // 创建适配器
        ListViewAdapter adapter = new ListViewAdapter(1, taskData);
        // 设置适配器
        taskList.setAdapter(adapter);
    }
}