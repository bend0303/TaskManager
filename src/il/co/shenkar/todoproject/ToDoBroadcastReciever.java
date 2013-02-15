package il.co.shenkar.todoproject;
/* ************************************
 *  Shenkar Java mobile final project
 * 
 *  Created by:
 *  Ben Diamant (bend0303@gmail.com)
 *  Or Guz (Orguz100@gmail.com)
 * 	=- Handed on 02/13 -=
 * 
 * ************************************
 *  Customized Broadcast reciever 
 * ************************************
 */
import il.co.shenkar.todoproject.R;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

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
		Notification noti = new NotificationCompat.Builder(context).setContentTitle(taskToView.getTaskTitle())
				.setSmallIcon(R.drawable.taskimage).setAutoCancel(true).addAction(R.drawable.doneb, "View All Tasks", pIntent).build();
		// Hide the notification after its selected
		noti.flags |= Notification.FLAG_AUTO_CANCEL;

		notificationManager.notify(12, noti);

	}
}
