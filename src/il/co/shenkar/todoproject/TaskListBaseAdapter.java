package il.co.shenkar.todoproject;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TaskListBaseAdapter extends BaseAdapter {
	private TaskDataBastModule tasksDataModule;
	private LayoutInflater l_Inflater;
	private Context context;
	
	
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
		final ViewHolder holder;
		if (convertView == null) {
			convertView = l_Inflater.inflate(R.layout.task_view, null);
			holder = new ViewHolder();
			holder.txt_taskName = (TextView) convertView.findViewById(R.id.task);
			holder.taskMenu = (RelativeLayout) convertView.findViewById(R.id.taskMenu);
			convertView.setClickable(true);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.txt_taskName.setText(tasksDataModule.getTask(position).getTaskTitle());
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (holder.taskMenu.getVisibility() == RelativeLayout.VISIBLE) {
					 Animation fadeInAnimation = AnimationUtils.loadAnimation(context, R.anim.fadeout);
					 holder.taskMenu.startAnimation(fadeInAnimation);		
					 holder.taskMenu.setVisibility(RelativeLayout.GONE);
				}
				else {
					ListView l =  (ListView) v.getParent();
					LinearLayout ll;
					RelativeLayout rl;
					for (int i=0; i<l.getChildCount(); i++) {
						ll = (LinearLayout) l.getChildAt(i);
						rl = (RelativeLayout) ll.getChildAt(1);
						if (rl.getVisibility() ==  RelativeLayout.VISIBLE)
							rl.setVisibility(RelativeLayout.GONE);
					}
					Animation fadeInAnimation = AnimationUtils.loadAnimation(context, R.anim.fadein);
					holder.taskMenu.startAnimation(fadeInAnimation);		
					holder.taskMenu.setVisibility(RelativeLayout.VISIBLE);
				}
		}
		});	
		holder.taskMenu.setTag(position);
		return convertView;
	}

	static class ViewHolder {
		
		TextView txt_taskName;
		RelativeLayout taskMenu;
	}
	

}