package il.co.shenkar.todoproject.activities;

/* ************************************
 *  Shenkar Java mobile final project
 * 
 *  Created by:
 *  Ben Diamant (bend0303@gmail.com)
 *  Or Guz (Orguz100@gmail.com)
 * 	=- Handed on 02/13 -=
 * 
 * ************************************
 *  Task creating activity
 * ************************************
 */

import il.co.shenkar.todoproject.R;
import il.co.shenkar.todoproject.R.anim;
import il.co.shenkar.todoproject.R.id;
import il.co.shenkar.todoproject.R.layout;
import il.co.shenkar.todoproject.R.menu;
import il.co.shenkar.todoproject.dao.TaskDataBaseModule;
import il.co.shenkar.todoproject.entities.TaskDetails;
import il.co.shenkar.todoproject.utils.ToDoBroadcastReciever;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.analytics.tracking.android.EasyTracker;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Address;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

//test number 2
public class TaskAddActivity extends Activity {
	public static final int LOC_ACTIVITY_CODE = 6001;
	public static final int LOCATION_RES_OK = 6002;
	public static final int ADD_MODE = 5001;
	public static final int EDIT_MODE = 5002;
	public static final int VIEW_MODE = 5003;
	private Button timeBtn, dateBtn, addButton, randomButton, cancelButton, locBtn;
	private DateFormat formatDateTime = DateFormat.getDateTimeInstance();
	private Calendar dateTime = Calendar.getInstance();
	private Calendar tempDateTime = Calendar.getInstance();
	private TextView locText;
	private TextView timeLabel;
	private EditText taskTitle;
	private Switch reminderSwitch;
	private LinearLayout reminderLayout;
	private String addressLine;
	private double latitude, longitude;
	private boolean setReminder = false;
	private boolean timeReminder = false;
	private boolean locReminder = false;
	private static String asyncTaskUrl = "http://mobile1-tasks-dispatcher.herokuapp.com/task/random";

	private void getAllbyId() {
		locText = (TextView) findViewById(R.id.locText);
		reminderLayout = (LinearLayout) findViewById(R.id.reminder_layout);
		reminderSwitch = (Switch) findViewById(R.id.reminderSwitch);
		timeLabel = (TextView) findViewById(R.id.timeTxt);
		timeBtn = (Button) findViewById(R.id.timeBtn);
		taskTitle = (EditText) findViewById(R.id.taskTitleInput);
		dateBtn = (Button) findViewById(R.id.dateBtn);
		addButton = (Button) findViewById(R.id.addTaskButton);
		randomButton = (Button) findViewById(R.id.randomAddButton);
		cancelButton = (Button) findViewById(R.id.cancelAddButton);
		locBtn = (Button) findViewById(R.id.setlocBtn);

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
		// Disable all inputs on screen
		reminderSwitch.setVisibility(Switch.INVISIBLE);
		timeBtn.setVisibility(Button.INVISIBLE);
		dateBtn.setVisibility(Button.INVISIBLE);
		randomButton.setVisibility(Button.INVISIBLE);
		addButton.setVisibility(Button.INVISIBLE);
		taskTitle.setInputType(0);
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
		dateTime.setTimeInMillis(task.getTaskActionTime());
		locText.setText(task.getAddressLine() == null ? "" : task.getAddressLine());
		if (task.isLocReminder() || task.isTimeReminder()) {
			reminderSwitch.setChecked(true);
			ReminderSwitchInit(reminderSwitch);
		}
		updateLabel(dateTime);
	}

	public void ReminderSwitchInit(View view) {
		boolean on = ((Switch) view).isChecked();

		if (on) {
			setReminder = true;
			Animation fadeInAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein);
			reminderLayout.startAnimation(fadeInAnimation);
			reminderLayout.setVisibility(RelativeLayout.VISIBLE);
		} else {
			setReminder = false;
			Animation fadeInAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadeout);
			reminderLayout.startAnimation(fadeInAnimation);
			reminderLayout.setVisibility(RelativeLayout.INVISIBLE);
		}

	}

	private void EditModeSettings(final TaskDetails task) {
		pullTaskData(task);
		setDateTimeButtons();
		randomButton.setVisibility(Button.INVISIBLE);
		locBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent Intent = new Intent(TaskAddActivity.this, TaskGeoSetActivity.class);
				TaskAddActivity.this.startActivityForResult(Intent, 1);

			}
		});
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
		locBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent Intent = new Intent(TaskAddActivity.this, TaskGeoSetActivity.class);
				TaskAddActivity.this.startActivityForResult(Intent, 1);

			}
		});
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
			dateTime.set(Calendar.HOUR_OF_DAY, tempDateTime.get(Calendar.HOUR_OF_DAY));
			dateTime.set(Calendar.MINUTE, tempDateTime.get(Calendar.MINUTE));
			updateLabel(dateTime);
		}
	};
	DialogInterface.OnCancelListener dateOcl = new DialogInterface.OnCancelListener() {
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
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			dateTime.set(Calendar.YEAR, year);
			dateTime.set(Calendar.MONTH, monthOfYear);
			dateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
		}
	};

	private void updateLabel(Calendar inputDateTime) {
		timeReminder = true;
		timeLabel.setText(formatDateTime.format(dateTime.getTime()));
	}

	private void setDateTimeButtons() {
		timeBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				tempDateTime.set(Calendar.HOUR_OF_DAY, dateTime.get(Calendar.HOUR_OF_DAY));
				tempDateTime.set(Calendar.MINUTE, dateTime.get(Calendar.MINUTE));
				TimePickerDialog timeDialog = new TimePickerDialog(TaskAddActivity.this, t, dateTime.get(Calendar.HOUR_OF_DAY), dateTime
						.get(Calendar.MINUTE), true);
				timeDialog.setOnCancelListener(timeOcl);
				timeDialog.setOnDismissListener(timeOdl);
				timeDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface timeDialog, int which) {
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
				tempDateTime.set(Calendar.DAY_OF_MONTH, dateTime.get(Calendar.DAY_OF_MONTH));
				DatePickerDialog dateDialog = new DatePickerDialog(TaskAddActivity.this, d, dateTime.get(Calendar.YEAR), dateTime
						.get(Calendar.MONTH), dateTime.get(Calendar.DAY_OF_MONTH));
				dateDialog.setOnCancelListener(dateOcl);
				dateDialog.setOnDismissListener(dateOdl);
				dateDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dateDialog, int which) {
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
		if (!setReminder) {
			addressLine = "";
			timeReminder = false;
			locReminder = false;
		}
		TaskDetails updatedTask = new TaskDetails(taskTitle.getText().toString(), dateTime.getTimeInMillis(), System.currentTimeMillis(),
				addressLine, timeReminder, locReminder, TaskDetails.TODO_MODE);

		updatedTask.setTaskId(pId);
		TaskDataBaseModule tasksModel = TaskDataBaseModule.getInstance(getApplicationContext());
		tasksModel.updateTask(updatedTask);
		if (setReminder) {
			if (timeReminder)
				setActivityAlarm(updatedTask.getTaskId());
			if (locReminder)
				setActivityLocAlarm(updatedTask.getTaskId());
		}

		startActivity(intent);
	}

	private void createTask(View v) {
		Intent intent = new Intent(this, TaskViewImageActivity.class);
		TaskDetails newTask = new TaskDetails(taskTitle.getText().toString(), dateTime.getTimeInMillis(), System.currentTimeMillis(),
				addressLine, timeReminder, locReminder, TaskDetails.TODO_MODE);
		TaskDataBaseModule tasksModel = TaskDataBaseModule.getInstance(getApplicationContext());

		tasksModel.addTask(newTask);
		tasksModel.sortTasks();
		if (setReminder) {
			if (timeReminder)
				setActivityAlarm(newTask.getTaskId());
			if (locReminder)
				setActivityLocAlarm(newTask.getTaskId());
		}

		finish();
		startActivity(intent);
	}

	private void setActivityLocAlarm(int tId) {
		Intent intent = new Intent("android.intent.action.LOCTASKS");
		intent.putExtra("TASK_ID", tId);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), tId+20000, intent, 0);
		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		lm.addProximityAlert(latitude, longitude, 100, -1, pendingIntent);
		IntentFilter filter = new IntentFilter("android.intent.action.LOCTASKS");
		registerReceiver(new ToDoBroadcastReciever(), filter);
	}

	private void setActivityAlarm(int tId) {
		Intent intent = new Intent("android.intent.action.TASKS");
		intent.putExtra("TASK_ID", tId);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), tId, intent, 0);
		AlarmManager alarmManager = (AlarmManager) getSystemService("alarm");
		alarmManager.set(AlarmManager.RTC_WAKEUP, dateTime.getTimeInMillis(), pendingIntent);
	}

	@Override
	protected void onStart() {
		EasyTracker.getInstance().setContext(this);
		EasyTracker.getInstance().activityStart(this);
		super.onStart();
	}
	@Override
	protected void onStop() {
		EasyTracker.getInstance().setContext(this);
		EasyTracker.getInstance().activityStop(this);
		super.onStop();
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.i("TaskAddActivity", "Activity onActivityResult");
		if (resultCode == LOCATION_RES_OK) {
			Address item = (Address) data.getExtras().get("ADDRESS");
			if (item != null) {
				addressLine = item.getLocality() + ", " + item.getCountryName() + ", " + item.getFeatureName();
				locText.setText(addressLine);
				locReminder = true;
				latitude = item.getLatitude();
				longitude = item.getLongitude();
			}
		}
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		Log.i("TaskAddActivity", "Activity onRestoreInstanceState");
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
				HttpURLConnection connection = (HttpURLConnection) remoteUrl.openConnection();
				connection.connect();
				reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				StringBuilder responseBuilder = new StringBuilder();
				for (String line = reader.readLine(); line != null; line = reader.readLine()) {
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
			} catch (JSONException e) {
				Log.e("onPostExecute", "Could not create JSON object");
			}
		}
	}

}
