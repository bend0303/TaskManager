package il.co.shenkar.todoproject;

import il.co.shenkar.todoproject.utils.AnimationHelper;
import il.co.shenkar.todoproject.utils.TrackerHelper;

import java.util.ArrayList;

import com.google.analytics.tracking.android.EasyTracker;

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
	private final ArrayList<TaskDetails> deleteableItems;
	private final ArrayList<TaskDetails> doneItems;
	private TaskDoneListBaseAdapter doneAdapter;
	private final OnClickListener doneButtonOnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			EasyTracker.getTracker().sendEvent(TrackerHelper.UI_ACTION, TrackerHelper.BUTTON_PRESSED, TrackerHelper.DELETE_BUTTON, null);
			int position = (Integer) v.getTag();
			doneItems.add(tasksDataModule.getTask(position));
			EasyTracker.getTracker().sendEvent(TrackerHelper.LOGIC_ACTION, TrackerHelper.TASK_DELETED, TrackerHelper.TASK_DELETED, null);
			doneAdapter.notifyDataSetChanged();
			notifyDataSetChanged();
		}
	};
	private final OnClickListener deleteButtonOnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			EasyTracker.getTracker().sendEvent(TrackerHelper.UI_ACTION, TrackerHelper.BUTTON_PRESSED, TrackerHelper.DELETE_BUTTON, null);
			int position = (Integer) v.getTag();
			deleteableItems.add(tasksDataModule.getTask(position));
			EasyTracker.getTracker().sendEvent(TrackerHelper.LOGIC_ACTION, TrackerHelper.TASK_DELETED, TrackerHelper.TASK_DELETED, null);
			notifyDataSetChanged();
		}
	};
	private final OnClickListener editButtonOnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int position = (Integer) v.getTag();
			Intent intent = new Intent(context, TaskAddActivity.class);
			intent.putExtra("MODE", TaskAddActivity.EDIT_MODE);
			intent.putExtra("TASK", tasksDataModule.getTask(position));
			context.startActivity(intent);
		}
	};

	public TaskListBaseAdapter(Context context, ArrayList<TaskDetails> results, TaskDoneListBaseAdapter doneAdapter) {
		tasksDataModule = TaskDataBastModule.getInstance(context);
		l_Inflater = LayoutInflater.from(context);
		this.context = context;
		deleteableItems = new ArrayList<TaskDetails>();
		doneItems = new ArrayList<TaskDetails>();
		this.doneAdapter = doneAdapter;
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
			holder.delButton = (TextView) convertView.findViewById(R.id.delButton);
			holder.doneButton = (TextView) convertView.findViewById(R.id.doneButton);
			holder.editButton = (TextView) convertView.findViewById(R.id.editButton);
			holder.delButton.setOnClickListener(deleteButtonOnClick);
			holder.editButton.setOnClickListener(editButtonOnClick);
			holder.doneButton.setOnClickListener(doneButtonOnClick);
			holder.taskMenu = (RelativeLayout) convertView.findViewById(R.id.taskMenu);
			
			convertView.setClickable(true);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		TaskDetails task = tasksDataModule.getTask(position);
		holder.txt_taskName.setText(task.getTaskTitle());
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (holder.taskMenu.getVisibility() == RelativeLayout.VISIBLE) {
					Animation fadeOutAnimation = AnimationUtils.loadAnimation(context, R.anim.fadeout);
					holder.taskMenu.startAnimation(fadeOutAnimation);
					holder.taskMenu.setVisibility(RelativeLayout.GONE);
				} else {
					ListView l = (ListView) v.getParent();
					LinearLayout ll;
					RelativeLayout rl;
					for (int i = 0; i < l.getChildCount(); i++) {
						ll = (LinearLayout) l.getChildAt(i);
						rl = (RelativeLayout) ll.getChildAt(1);
						if (rl.getVisibility() == RelativeLayout.VISIBLE)
							rl.setVisibility(RelativeLayout.GONE);
					}
					Animation fadeInAnimation = AnimationUtils.loadAnimation(context, R.anim.fadein);
					holder.taskMenu.startAnimation(fadeInAnimation);
					holder.taskMenu.setVisibility(RelativeLayout.VISIBLE);
				}
			}
		});
		checkIfItemHasBeenMarkedAsDeleted(convertView, task);
		checkIfItemHasBeenMarkedAsDone(convertView, task);
		holder.taskMenu.setVisibility(RelativeLayout.GONE);
		holder.taskMenu.setTag(position);
		holder.delButton.setTag(position);
		holder.doneButton.setTag(position);
		holder.editButton.setTag(position);
		return convertView;
	}
	private void checkIfItemHasBeenMarkedAsDone(View view, TaskDetails item) {
		for (TaskDetails deletable : doneItems) {
			removeTaskMarkedAsDone(view, item, deletable);

		}
	}
	private void removeTaskMarkedAsDone(View view, TaskDetails item, TaskDetails doneable) {
		if(itemsIsDone(item, doneable)){
			Animation fadeout = AnimationHelper.createFadeoutAnimation();
			removeOnAnimationComplete(fadeout, item);
			animate(view, fadeout);
		}
	}
	private void checkIfItemHasBeenMarkedAsDeleted(View view, TaskDetails item) {
		for (TaskDetails doneable : deleteableItems) {
			deleteItemIfMarkedAsDeletable(view, item, doneable);

		}
	}

	private void deleteItemIfMarkedAsDeletable(View view, TaskDetails item, TaskDetails deletable) {
		if(itemIsDeletable(item, deletable)){
			Animation fadeout = AnimationHelper.createFadeoutAnimation();
			deleteOnAnimationComplete(fadeout, item);
			animate(view, fadeout);
		}
	}
	private static boolean itemIsDeletable(TaskDetails item, TaskDetails deletable) {
		return item.getTaskId() == deletable.getTaskId();
	}
	private static boolean itemsIsDone(TaskDetails item, TaskDetails doneable) {
		return item.getTaskId() == doneable.getTaskId();
	}
	private void removeOnAnimationComplete(Animation fadeout, final TaskDetails item) {
		fadeout.setAnimationListener(new  Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) { }
			@Override
			public void onAnimationRepeat(Animation animation) { }

			@Override
			public void onAnimationEnd(Animation animation)  {
				tasksDataModule.moveToDone(item);
				doneItems.remove(item);
				notifyDataSetChanged();
			}
		});
	}
	private void deleteOnAnimationComplete(Animation fadeout, final TaskDetails item) {
		fadeout.setAnimationListener(new  Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) { }
			@Override
			public void onAnimationRepeat(Animation animation) { }

			@Override
			public void onAnimationEnd(Animation animation)  {
				tasksDataModule.removeTask(item);
				deleteableItems.remove(item);
				notifyDataSetChanged();
			}
		});
	}
	// actually do the animate on our row
	private static void animate(View view, Animation animation) {
		view.startAnimation(animation);
	}


	static class ViewHolder {
		TextView editButton;
		TextView doneButton;
		TextView delButton;
		TextView txt_taskName;
		RelativeLayout taskMenu;
	}

}