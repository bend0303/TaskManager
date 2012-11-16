package com.example.todoproject;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class TaskAddActivity extends Activity {

	EditText taskTitle,taskDesc;
	
	private void getAllbyId()
	{
         taskTitle = (EditText) findViewById(R.id.taskTitleInput);
         taskDesc = (EditText) findViewById(R.id.taskDescInput); 
         
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_add);
        getAllbyId();
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
    	startActivity(intent);
    	
    }
}
