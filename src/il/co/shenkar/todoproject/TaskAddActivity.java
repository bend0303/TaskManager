package il.co.shenkar.todoproject;

import il.co.shenkar.todoproject.R.layout;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.TimePicker;

//test number 2
public class TaskAddActivity extends Activity {
	public static final int ADD_MODE = 5001;
	public static final int EDIT_MODE = 5002;
	public static final int VIEW_MODE = 5003;
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
		Bundle extras = getIntent().getExtras();
		int Activity_Mode = (Integer) extras.get("MODE");
		
		switch (Activity_Mode) {
			case ADD_MODE: {
				AddModeSettings();
				break;
			}
			case EDIT_MODE: { 
				TaskDetails task = (TaskDetails) extras.get("TASK");
				EditModeSettings(task);
				break;
			}
	
			case VIEW_MODE: {
				TaskDetails task = (TaskDetails) extras.get("TASK");
				ViewModeSettings(task);
				break;
			}

		}

	}

	private void ViewModeSettings(TaskDetails task) {
		//Disable all inputs on screen
		timeBtn.setVisibility(Button.INVISIBLE);
		dateBtn.setVisibility(Button.INVISIBLE);
		randomButton.setVisibility(Button.INVISIBLE);
		addButton.setVisibility(Button.INVISIBLE);
		taskTitle.setInputType(0);
		taskDesc.setInputType(0);
		cancelButton.setText("Go Back!");
		cancelButton.setWidth(LayoutParams.FILL_PARENT);
		cancelButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				TaskAddActivity.this.finish();
			}
		});
		pullTaskData(task);
	}

	private void pullTaskData(TaskDetails task) {
		taskTitle.setText(task.getTaskTitle());
		taskDesc.setText(task.getTaskDesc());
		dateTime.setTimeInMillis(task.getTaskActionTime());
		updateLabel(dateTime);	
	}

	private void EditModeSettings(final TaskDetails task) {
		pullTaskData(task);
		setDateTimeButtons();
		randomButton.setVisibility(Button.INVISIBLE);

		addButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				updateTask(v, task.getTaskId());
			}

		});
	
	
		cancelButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				TaskAddActivity.this.finish();
			}
		});
	}

	private void AddModeSettings() {
		setDateTimeButtons();
		updateLabel(dateTime);
		randomButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					URL[] urls = { new URL(asyncTaskUrl) };
					new GetFromWebTask().execute(urls);
				} catch (Exception e) {
					Log.e("Random onClick", e.getMessage());
				}

			}
		});
		addButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				createTask(v);
			}
		});
		cancelButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				TaskAddActivity.this.finish();
			}
		});
	}

	DialogInterface.OnDismissListener timeOdl = new DialogInterface.OnDismissListener() {
		@Override
		public void onDismiss(DialogInterface dialog) {
			updateLabel(dateTime);
		}
	};
	DialogInterface.OnDismissListener dateOdl = new DialogInterface.OnDismissListener() {

		@Override
		public void onDismiss(DialogInterface dialog) {
			updateLabel(dateTime);
		}
	};

	DialogInterface.OnCancelListener timeOcl = new DialogInterface.OnCancelListener() {

		@Override
		public void onCancel(DialogInterface dialog) {
			dateTime.set(Calendar.HOUR_OF_DAY,
					tempDateTime.get(Calendar.HOUR_OF_DAY));
			dateTime.set(Calendar.MINUTE, tempDateTime.get(Calendar.MINUTE));
			updateLabel(dateTime);
		}
	};
	DialogInterface.OnCancelListener dateOcl = new DialogInterface.OnCancelListener() {
		@Override
		public void onCancel(DialogInterface dialog) {
			dateTime.set(Calendar.YEAR, tempDateTime.get(Calendar.YEAR));
			dateTime.set(Calendar.MONTH, tempDateTime.get(Calendar.MONTH));
			dateTime.set(Calendar.DAY_OF_MONTH,
					tempDateTime.get(Calendar.DAY_OF_MONTH));
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

	private void updateLabel(Calendar inputDateTime) {
		timeLabel.setText(formatDateTime.format(dateTime.getTime()));
	}

	private void setDateTimeButtons() {
		timeBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				tempDateTime.set(Calendar.HOUR_OF_DAY,
						dateTime.get(Calendar.HOUR_OF_DAY));
				tempDateTime.set(Calendar.MINUTE, dateTime.get(Calendar.MINUTE));
				TimePickerDialog timeDialog = new TimePickerDialog(
						TaskAddActivity.this, t, dateTime
								.get(Calendar.HOUR_OF_DAY), dateTime
								.get(Calendar.MINUTE), true);
				timeDialog.setOnCancelListener(timeOcl);
				timeDialog.setOnDismissListener(timeOdl);
				timeDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface timeDialog,
									int which) {
								if (which == DialogInterface.BUTTON_NEGATIVE) {
									timeDialog.cancel();
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
				tempDateTime.set(Calendar.DAY_OF_MONTH,
						dateTime.get(Calendar.DAY_OF_MONTH));
				DatePickerDialog dateDialog = new DatePickerDialog(
						TaskAddActivity.this, d, dateTime.get(Calendar.YEAR),
						dateTime.get(Calendar.MONTH), dateTime
								.get(Calendar.DAY_OF_MONTH));
				dateDialog.setOnCancelListener(dateOcl);
				dateDialog.setOnDismissListener(dateOdl);
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
	}

	// Time on set listener
	private TimePickerDialog.OnTimeSetListener t = new TimePickerDialog.OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			dateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
			dateTime.set(Calendar.MINUTE, minute);
			updateLabel(dateTime);
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_task_add, menu);
		return true;
	}

	
	private void updateTask(View v, int pId) {
		Intent intent = new Intent(this, TaskViewImageActivity.class);
		TaskDetails updatedTask = new TaskDetails(taskTitle.getText().toString(),
				taskDesc.getText().toString(), dateTime.getTimeInMillis(),
				System.currentTimeMillis());
		updatedTask.setTaskId(pId);
		TaskDataBastModule tasksModel = TaskDataBastModule
				.getInstance(getApplicationContext());
		tasksModel.updateTask(updatedTask);
		setActivityAlarm(updatedTask.getTaskId());
		startActivity(intent);
	}
	
	private void createTask(View v) {
		Intent intent = new Intent(this, TaskViewImageActivity.class);
		TaskDetails newTask = new TaskDetails(taskTitle.getText().toString(),
				taskDesc.getText().toString(), dateTime.getTimeInMillis(),
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
				getApplicationContext(), tId, intent, 0);
		AlarmManager alarmManager = (AlarmManager) getSystemService("alarm");
		alarmManager.set(AlarmManager.RTC_WAKEUP, dateTime.getTimeInMillis(),
				pendingIntent);
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

		@Override
		protected String doInBackground(URL... url) {
			String response = "";
			URL remoteUrl = url[0];
			BufferedReader reader;
			try {
				HttpURLConnection connection = (HttpURLConnection) remoteUrl
						.openConnection();
				connection.connect();
				reader = new BufferedReader(new InputStreamReader(
						connection.getInputStream()));
				StringBuilder responseBuilder = new StringBuilder();
				for (String line = reader.readLine(); line != null; line = reader
						.readLine()) {
					responseBuilder.append(line);
				}
				response = responseBuilder.toString();

			} catch (Exception e) {
				Log.e("doInBackground", e.getMessage());
			}
			return response;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			try {
				JSONObject jsonResponse = new JSONObject(result);
				taskTitle.setText(jsonResponse.getString("topic"));
				taskDesc.setText(jsonResponse.getString("description"));
			} catch (JSONException e) {
				Log.e("onPostExecute", "Could not create JSON object");
			}
		}
	}

}
