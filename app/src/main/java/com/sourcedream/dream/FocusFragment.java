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
import com.sourcedream.dream.database.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;


public class FocusFragment extends Fragment {
    private RecyclerView focusList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 加载布局
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
        // 创建数据对象
        List<ItemFocusBean> focusData;
        // 获取数据
        DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
        focusData = databaseHelper.getFocusData();
        // 设置样式(布局管理器)
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        focusList.setLayoutManager(layoutManager);
        // 创建适配器
        ListViewAdapter adapter = new ListViewAdapter(0, focusData);
        // 设置适配器
        focusList.setAdapter(adapter);
    }
}