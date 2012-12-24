package il.co.shenkar.todoproject;

import java.text.DateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
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
    private Button timeBtn;
	private Button dateBtn;
	DateFormat formatDateTime = DateFormat.getDateTimeInstance();
	Calendar dateTime = Calendar.getInstance();
	private TextView timeLabel;
	EditText taskTitle,taskDesc;
	
	private void getAllbyId()
	{
		timeLabel = (TextView)findViewById(R.id.timeTxt);
        timeBtn = (Button) findViewById(R.id.timeBtn);
        taskTitle = (EditText) findViewById(R.id.taskTitleInput);
        taskDesc = (EditText) findViewById(R.id.taskDescInput); 
        dateBtn = (Button) findViewById(R.id.dateBtn);
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
		    	new TimePickerDialog(TaskAddActivity.this, t, dateTime.get(Calendar.HOUR_OF_DAY), dateTime.get(Calendar.MINUTE), true).show();
				
			}
		});
        dateBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new DatePickerDialog(TaskAddActivity.this, d, dateTime.get(Calendar.YEAR),dateTime.get(Calendar.MONTH), dateTime.get(Calendar.DAY_OF_MONTH)).show();
				 		
			}
		});
        updateLabel();
        

    }
    
    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
		
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) {
			dateTime.set(Calendar.YEAR,year);
			dateTime.set(Calendar.MONTH, monthOfYear);
			dateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			
			updateLabel();
		}
    };
	
	TimePickerDialog.OnTimeSetListener t = new TimePickerDialog.OnTimeSetListener() {
		
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			dateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
			dateTime.set(Calendar.MINUTE,minute);	
			updateLabel();
		}
	};
	
	private void updateLabel() {
		timeLabel.setText(formatDateTime.format(dateTime.getTime()));
	}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_task_add, menu);
        return true;
    }
    public void createTask(View v)
    {
    	Intent intent = new Intent(this, TaskViewImageActivity.class);
    	TaskDetails newTask = new TaskDetails(taskTitle.getText().toString(), taskDesc.getText().toString(), formatDateTime.toString(), System.currentTimeMillis());     
    	TaskDataBastModule tasksModel = TaskDataBastModule.getInstance(getApplicationContext());
    	tasksModel.addTask(newTask);
    	tasksModel.sortTasks();
    	setActivityAlarm(newTask.getTaskId());
    	startActivity(intent);
    	
    }
    private void setActivityAlarm(int tId) {
    	Intent intent = new Intent("android.intent.action.TASKS");
    	intent.putExtra("TASK_ID", tId);
    	PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
    	AlarmManager alarmManager = (AlarmManager) getSystemService("alarm");
    	alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 12000, pendingIntent);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
		Log.i("TaskAddActivity", "KeyDown action");

        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
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
}
