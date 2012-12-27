package il.co.shenkar.todoproject;

import java.net.URL;
import java.text.DateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

//test number 2
public class TaskAddActivity extends Activity {
	private Button timeBtn, dateBtn, addButton, randomButton, cancelButton;
	private DateFormat formatDateTime = DateFormat.getDateTimeInstance();
	private Calendar dateTime = Calendar.getInstance();
	private Calendar tempDateTime = Calendar.getInstance();
	private TextView timeLabel;
	private EditText taskTitle, taskDesc;
	private static String asyncTaskUrl = "http://mobile1-tasks-dispatcher.herokuapp.com/task/random";

	private void getAllbyId() {
		timeLabel = (TextView) findViewById(R.id.timeTxt);
		timeBtn = (Button) findViewById(R.id.timeBtn);
		taskTitle = (EditText) findViewById(R.id.taskTitleInput);
		taskDesc = (EditText) findViewById(R.id.taskDescInput);
		dateBtn = (Button) findViewById(R.id.dateBtn);
		addButton = (Button) findViewById(R.id.addTaskButton);
		randomButton = (Button) findViewById(R.id.randomAddButton);
		cancelButton = (Button) findViewById(R.id.cancelAddButton);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i("TaskAddActivity", "Activity Created");
		setContentView(R.layout.activity_task_add);
		getAllbyId();

		timeBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				TimePickerDialog timeDialog = new TimePickerDialog(
						TaskAddActivity.this, t, dateTime
								.get(Calendar.HOUR_OF_DAY), dateTime
								.get(Calendar.MINUTE), true);
				timeDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface timeDialog,
									int which) {
								if (which == DialogInterface.BUTTON_NEGATIVE) {
									timeDialog.dismiss();
								}
							}
						});
				timeDialog.show();
			}
		});
		dateBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				tempDateTime.set(Calendar.YEAR, dateTime.get(Calendar.YEAR));
				tempDateTime.set(Calendar.MONTH, dateTime.get(Calendar.MONTH));
				tempDateTime.set(Calendar.DAY_OF_MONTH, dateTime.get(Calendar.DAY_OF_MONTH));
				DatePickerDialog dateDialog = new DatePickerDialog(
						TaskAddActivity.this, d, dateTime.get(Calendar.YEAR),
						dateTime.get(Calendar.MONTH), dateTime
								.get(Calendar.DAY_OF_MONTH));
				dateDialog.setOnCancelListener(ocl);
				dateDialog.setOnDismissListener(odl);	
				dateDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dateDialog,
									int which) {
								if (which == DialogInterface.BUTTON_NEGATIVE) {
									dateDialog.cancel();
								}
							}
						});
				dateDialog.show();
			}
		});
		addButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		updateLabel(dateTime);
	}

	DialogInterface.OnDismissListener odl = new DialogInterface.OnDismissListener() {
		
		@Override
		public void onDismiss(DialogInterface dialog) {
			updateLabel(dateTime);
		}
	};
	DialogInterface.OnCancelListener ocl = new DialogInterface.OnCancelListener() {

		@Override
		public void onCancel(DialogInterface dialog) {
			dateTime.set(Calendar.YEAR, tempDateTime.get(Calendar.YEAR));
			dateTime.set(Calendar.MONTH, tempDateTime.get(Calendar.MONTH));
			dateTime.set(Calendar.DAY_OF_MONTH, tempDateTime.get(Calendar.DAY_OF_MONTH));
			updateLabel(dateTime);
		}
	};
	// Date on set listener
	private DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			dateTime.set(Calendar.YEAR, year);
			dateTime.set(Calendar.MONTH, monthOfYear);
			dateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
		}
	};
	
	// Time on set listener
	private TimePickerDialog.OnTimeSetListener t = new TimePickerDialog.OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			dateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
			dateTime.set(Calendar.MINUTE, minute);
			updateLabel(dateTime);
		}
	};

	private void updateLabel(Calendar inputDateTime) {
		timeLabel.setText(formatDateTime.format(dateTime.getTime()));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_task_add, menu);
		return true;
	}

	public void createTask(View v) {
		Intent intent = new Intent(this, TaskViewImageActivity.class);
		TaskDetails newTask = new TaskDetails(taskTitle.getText().toString(),
				taskDesc.getText().toString(), formatDateTime.toString(),
				System.currentTimeMillis());
		TaskDataBastModule tasksModel = TaskDataBastModule
				.getInstance(getApplicationContext());
		tasksModel.addTask(newTask);
		tasksModel.sortTasks();
		setActivityAlarm(newTask.getTaskId());
		startActivity(intent);

	}

	private void setActivityAlarm(int tId) {
		Intent intent = new Intent("android.intent.action.TASKS");
		intent.putExtra("TASK_ID", tId);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(
				getApplicationContext(), 0, intent, 0);
		AlarmManager alarmManager = (AlarmManager) getSystemService("alarm");
		alarmManager.set(AlarmManager.RTC_WAKEUP,
				System.currentTimeMillis() + 12000, pendingIntent);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.i("TaskAddActivity", "KeyDown action");

		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			Log.i("TaskAddActivity", "Back KeyDown");
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onStart() {
		Log.i("TaskAddActivity", "Activity Started");
		super.onStart();
	}

	@Override
	protected void onStop() {
		Log.i("TaskAddActivity", "Activity Stoped");
		super.onStop();
		finish();
	}

	@Override
	protected void onResume() {
		Log.i("TaskAddActivity", "Activity Resumed");
		super.onResume();
	}

	private class GetFromWebTask extends AsyncTask<URL, Integer, String> {

		protected String doInBackground(URL urls) {
			// return getFromWeb(urls[0]); // BG thread
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// resultTextView.setText(result); // UI thread
		}

		@Override
		protected String doInBackground(URL... params) {
			// TODO Auto-generated method stub
			return null;
		}

	}

}
