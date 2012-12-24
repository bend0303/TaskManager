package il.co.shenkar.todoproject;


import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ToDoBroadcastReciever extends BroadcastReceiver {

	@SuppressLint("NewApi")
	@Override
	public void onReceive(Context context, Intent intent) {
		Intent nIntent = new Intent(context, TaskViewImageActivity.class);
		int taskId = intent.getExtras().getInt("TASK_ID");
		nIntent.putExtra("TASK_ID", taskId);
		TaskDetails taskToView = TaskDataBastModule.getInstance(context).getTaskById(taskId);
		PendingIntent pIntent = PendingIntent.getActivity(context, 0, nIntent, 0);
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification noti = new Notification.Builder(context)
         .setContentTitle(taskToView.getTaskTitle())
         .setContentText(taskToView.getTaskDesc())
         .setSmallIcon(R.drawable.taskimage)
         .setAutoCancel(true)
         .addAction(R.drawable.donebutton, "View All Tasks", pIntent)
         .build();
	    // Hide the notification after its selected
	    noti.flags |= Notification.FLAG_AUTO_CANCEL;

	    notificationManager.notify(12, noti);
 
	}
}
