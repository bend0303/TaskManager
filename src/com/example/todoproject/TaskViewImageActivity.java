package com.example.todoproject;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;

public class TaskViewImageActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ArrayList<TaskDetails> tasks = getTasks();
		final ListView lv1 = (ListView) findViewById(R.id.listV_main);
		
		ImageButton addButton = (ImageButton) findViewById(R.id.addButton);
		lv1.setAdapter(new TaskListBaseAdapter(this, tasks));
		addButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), TaskAddActivity.class);
				startActivity(intent);
			}
		});



	}


	private ArrayList<TaskDetails> getTasks() {
		ArrayList<TaskDetails> result = new ArrayList<TaskDetails>();	
//		TaskDetails t1 = new TaskDetails("TASK NUMBER 1","BRING FOOD");
//		TaskDetails t2 = new TaskDetails("TASK NUMBER 2","BRING MILK");
//		TaskDetails t3 = new TaskDetails("TASK NUMBER 3","BRING KIDS");
//		TaskDetails t4 = new TaskDetails("TASK NUMBER 4","BRING CLOTHS");
//		TaskDetails t5 = new TaskDetails("TASK NUMBER 5","WAKE UP");
//		result.add(t1);
//		result.add(t2);
//		result.add(t3);
//		result.add(t4);
//		result.add(t5);
//		Collections.sort(result, new TaskDetails.DateCompe());
		return result;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
}
