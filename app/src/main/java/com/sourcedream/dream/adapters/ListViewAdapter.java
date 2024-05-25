package com.sourcedream.dream.adapters;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sourcedream.dream.R;
import com.sourcedream.dream.beans.ItemFocusBean;
import com.sourcedream.dream.beans.ItemTaskBean;

import java.util.List;

public class ListViewAdapter extends RecyclerView.Adapter<ListViewAdapter.InnerHolder> {
    private final List<ItemFocusBean> focusData;
    private final List<ItemTaskBean> taskData;
    private final int dataType;
    // 构造方法
    public ListViewAdapter(int dataType, List<?> data) {
        this.dataType = dataType;
        if (dataType == 0) {
            this.focusData = (List<ItemFocusBean>) data;
            // 按照focusDate排序
            this.focusData.sort((o1, o2) -> {
                if(o1.focusDate.compareTo(o2.focusDate) > 0) {
                    return -1;
                } else if(o1.focusDate.compareTo(o2.focusDate) < 0) {
                    return 1;
                } else {
                    return 0;
                }
            });
            this.taskData = null;
        } else if (dataType == 1) {
            this.taskData = (List<ItemTaskBean>) data;
            this.focusData = null;
        } else {
            this.taskData = null;
            this.focusData = null;
        }
    }
    // 创建ViewHolder
    @NonNull
    @Override
    public ListViewAdapter.InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(dataType == 0) {
            view = View.inflate(parent.getContext(), R.layout.item_list_focus, null);
        } else {
            view = View.inflate(parent.getContext(), R.layout.item_list_task, null);
        }
        return new InnerHolder(view, dataType);
    }
    // 绑定数据
    @Override
    public void onBindViewHolder(@NonNull ListViewAdapter.InnerHolder holder, int position) {
        if(dataType == 0) {
            ItemFocusBean itemFocusBean = focusData.get(position); // 获取当前位置的数据
            holder.setData(itemFocusBean);
        } else {
            ItemTaskBean itemTaskBean = taskData.get(position); // 获取当前位置的数据
            holder.setData(itemTaskBean);
        }
    }
    // 返回条目个数
    @Override
    public int getItemCount() {
        if(dataType==0 && focusData != null){
            return focusData.size();
        }
        if(dataType==1 && taskData != null){
            return taskData.size();
        }
        return 0;
    }
    @Override
    public int getItemViewType(int position) {
        return dataType; // 0 for focus, 1 for task
    }
    public static class InnerHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView time;
        TextView date;
        TextView deadline;
        TextView title;
        TextView level;
        int dataType;
        public InnerHolder(@NonNull View itemView, int dataType) {
            super(itemView);
            this.dataType = dataType;
            if(dataType==0) {
                // 找到条目控件
                time = itemView.findViewById(R.id.list_focus_time);
                date = itemView.findViewById(R.id.list_focus_date);
                title = itemView.findViewById(R.id.list_focus_title);
            } else if (dataType==1) {
                // 找到条目控件
                icon = itemView.findViewById(R.id.list_task_icon);
                title = itemView.findViewById(R.id.list_task_title);
                deadline = itemView.findViewById(R.id.list_task_deadline);
                level = itemView.findViewById(R.id.list_task_level);
            }
        }
        @SuppressLint("SetTextI18n")
        public void setData(ItemFocusBean itemFocusBean) {
                time.setText(itemFocusBean.focusTime+"");
                date.setText(itemFocusBean.focusDate);
                title.setText(itemFocusBean.taskName);
        }
        @SuppressLint("SetTextI18n")
        public void setData(ItemTaskBean itemTaskBean) {
            if(itemTaskBean.status == 0) {
                icon.setImageResource(R.drawable.checkbox_false);
            } else {
                icon.setImageResource(R.drawable.checkbox_true);
            }
            title.setText(itemTaskBean.name);
            deadline.setText(itemTaskBean.deadline);
            level.setText(itemTaskBean.level+"");
        }
    }
}
