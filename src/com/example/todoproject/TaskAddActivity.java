package com.example.todoproject;

import java.text.DateFormat;
import java.util.Calendar;

import android.os.Bundle;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
//test number 2
public class TaskAddActivity extends Activity {
    private Button timeBtn;
	private Button dateBtn;
	DateFormat formatDateTime=DateFormat.getDateTimeInstance();
	Calendar dateTime=Calendar.getInstance();
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
    	taskDetails newTask = new taskDetails(taskTitle.getText().toString(), taskDesc.getText().toString());     	TaskDataBastModule tasks = TaskDataBastModule.getInstance();
    	tasks.getTasks().add(newTask);
    	tasks.sortTasks();
    	startActivity(intent);
    	
    }
}
