package com.sourcedream.dream;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.sourcedream.dream.adapters.ListViewAdapter;
import com.sourcedream.dream.beans.ItemTaskBean;
import com.sourcedream.dream.database.DatabaseHelper;
import java.util.ArrayList;
import java.util.List;

public class TaskFragment extends Fragment {
    private RecyclerView taskList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task, container, false);
        taskList = (RecyclerView) view.findViewById(R.id.task_list);
        // 数据获取
        updateTaskData();
        Button btnAddTask = view.findViewById(R.id.btn_add_task);
        btnAddTask.setOnClickListener(v -> {
            // 创建任务添加碎片
            AddTaskDialogFragment addTaskDialogFragment = new AddTaskDialogFragment();
            // 显示碎片
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            addTaskDialogFragment.show(fragmentTransaction, "addTaskDialogFragment");
        });
        return view;
    }
    private void updateTaskData() {
        // 创建数据对象
        List<ItemTaskBean> taskData = new ArrayList<>();
        // 创建数据对象
        // 获取数据
        DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
        taskData = databaseHelper.getTaskData();
        // 输出调试信息
//        for (ItemTaskBean itemTaskBean : taskData) {
//            Log.d("TaskData", itemTaskBean.toString());
//        }
        // 模拟数据
//        for(int i=0; i<20;i++) {
//            ItemTaskBean data = new ItemTaskBean();
//            data.id = 0;
//            data.name = "任务名称";
//            data.status = 0;
//            data.focusTime = 0;
//            data.deadline = "截至日期";
//            taskData.add(data);
//        }
        // 设置样式(布局管理器)
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        taskList.setLayoutManager(layoutManager);
        // 创建适配器
        ListViewAdapter adapter = new ListViewAdapter(1, taskData);
        // 设置适配器
        taskList.setAdapter(adapter);
    }
}