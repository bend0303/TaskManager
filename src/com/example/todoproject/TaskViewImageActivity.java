package com.example.todoproject;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

public class TaskViewImageActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ArrayList<taskDetails> tasks = getTasks();
		final ListView lv1 = (ListView) findViewById(R.id.listV_main);
		
		ImageButton addButton = (ImageButton) findViewById(R.id.addButton);
		addButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), TaskAddActivity.class);
				startActivity(intent);
			}
		});
		lv1.setAdapter(new TaskListBaseAdapter(this, tasks));
		lv1.setOnItemClickListener(new OnItemClickListener() {
			@Override

			public void onItemClick(AdapterView<?> a, View v, int position, long id) { 
				try {
					Object o = lv1.getItemAtPosition(position);
					taskDetails taskDetailsObject = (taskDetails) o;
					Toast.makeText(getApplicationContext(), taskDetailsObject.getTaskDesc(), Toast.LENGTH_LONG).show();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} );
	



	}


	private ArrayList<taskDetails> getTasks() {
		ArrayList<taskDetails> result = new ArrayList<taskDetails>();	
		taskDetails t1 = new taskDetails("TASK NUMBER 1","BRING FOOD");
		taskDetails t2 = new taskDetails("TASK NUMBER 2","BRING MILK");
		taskDetails t3 = new taskDetails("TASK NUMBER 3","BRING KIDS");
		taskDetails t4 = new taskDetails("TASK NUMBER 4","BRING CLOTHS");
		taskDetails t5 = new taskDetails("TASK NUMBER 5","WAKE UP");
		result.add(t1);
		result.add(t2);
		result.add(t3);
		result.add(t4);
		result.add(t5);
		return result;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
}
