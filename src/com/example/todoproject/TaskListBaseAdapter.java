
package com.example.todoproject;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TaskListBaseAdapter extends BaseAdapter {
	private TaskDataBastModule tasksDataModule;
	private LayoutInflater l_Inflater;

	public TaskListBaseAdapter(Context context, ArrayList<taskDetails> results) {
		tasksDataModule = TaskDataBastModule.getInstance(results);
		l_Inflater = LayoutInflater.from(context);
	}

	public int getCount() {
		return tasksDataModule.getCount();
	}

	public taskDetails getItem(int position) {
		return tasksDataModule.getTask(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = l_Inflater.inflate(R.layout.task_view, null);
			holder = new ViewHolder();
			holder.txt_taskName = (TextView) convertView.findViewById(R.id.task);
			convertView.setClickable(true);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.txt_taskName.setText(tasksDataModule.getTask(position).getTaskTitle());
		return convertView;
	}

	static class ViewHolder {
		TextView txt_taskName;
		ImageView itemImage;
	}
}