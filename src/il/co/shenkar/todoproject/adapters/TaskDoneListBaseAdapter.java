package il.co.shenkar.todoproject.adapters;

/* ************************************
 *  Shenkar Java mobile final project
 * 
 *  Created by:
 *  Ben Diamant (bend0303@gmail.com)
 *  Or Guz (Orguz100@gmail.com)
 * 	=- Handed on 02/13 -=
 * 
 * ************************************
 *  Done tasks tab list custom adapter
 * ************************************
 */
import il.co.shenkar.todoproject.R;
import il.co.shenkar.todoproject.R.id;
import il.co.shenkar.todoproject.R.layout;
import il.co.shenkar.todoproject.dao.TaskDataBaseModule;
import il.co.shenkar.todoproject.entities.TaskDetails;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TaskDoneListBaseAdapter extends BaseAdapter {
	private static ArrayList<TaskDetails> tasks;
	private LayoutInflater l_Inflater;
	private Context context;

	private final OnClickListener doneButtonOnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int position = (Integer) v.getTag();
			TaskDataBaseModule.getInstance(context).removeTask(tasks.get(position));
			notifyDataSetChanged();
		}
	};

	public TaskDoneListBaseAdapter(Context context, ArrayList<TaskDetails> results) {
		l_Inflater = LayoutInflater.from(context);
		this.context = context;
		tasks = results;
	}

	public int getCount() {
		return tasks.size();
	}

	public TaskDetails getItem(int position) {
		return tasks.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = l_Inflater.inflate(R.layout.task_done_view, null);
			holder = new ViewHolder();
			holder.txt_taskName = (TextView) convertView.findViewById(R.id.task);
			holder.itemImage = (ImageView) convertView.findViewById(R.id.redx);
			holder.itemImage.setOnClickListener(doneButtonOnClick);
			convertView.setClickable(true);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.txt_taskName.setText(tasks.get(position).getTaskTitle());
		holder.itemImage.setTag(position);
		return convertView;
	}

	static class ViewHolder {
		TextView txt_taskName;
		ImageView itemImage;
	}

}