package com.example.todoproject;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class TaskListBaseAdapter extends BaseAdapter {
	private TaskDataBastModule tasksDataModule;
	private LayoutInflater l_Inflater;
	private Context context;
	private final OnClickListener doneButtonOnClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int position = (Integer) v.getTag();
			TaskDataBastModule.getInstance(context).remove(position);
			notifyDataSetChanged();
		}
	};
	
	public TaskListBaseAdapter(Context context, ArrayList<TaskDetails> results) {
		tasksDataModule = TaskDataBastModule.getInstance(context);
		l_Inflater = LayoutInflater.from(context);
		this.context = context;
	}

	public int getCount() {
		return tasksDataModule.getCount();
	}

	public TaskDetails getItem(int position) {
		return tasksDataModule.getTask(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = l_Inflater.inflate(R.layout.task_view, null);
			holder = new ViewHolder();
			holder.txt_taskName = (TextView) convertView.findViewById(R.id.task);
			holder.itemImage = (ImageView) convertView.findViewById(R.id.doneb);
			holder.itemImage.setOnClickListener(doneButtonOnClick);
			convertView.setClickable(true);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.txt_taskName.setText(tasksDataModule.getTask(position)
				.getTaskTitle());
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Toast.makeText(context, "Task Desc: " + tasksDataModule.getTask(position).getTaskDesc(), Toast.LENGTH_LONG)
						.show();
			}
		});	
		holder.itemImage.setTag(position);
		return convertView;
	}

	static class ViewHolder {
		TextView txt_taskName;
		ImageView itemImage;
	}
	

}