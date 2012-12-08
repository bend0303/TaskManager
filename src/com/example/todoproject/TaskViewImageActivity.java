package com.example.todoproject;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
		TaskDataBastModule dataModel = TaskDataBastModule.getInstance(getApplicationContext());
		ImageButton addButton = (ImageButton) findViewById(R.id.addButton);

		addButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						TaskAddActivity.class);
				startActivity(intent);
			}
		});
		try {
			final ListView lv1 = (ListView) findViewById(R.id.listV_main);
			lv1.setAdapter(new TaskListBaseAdapter(this, dataModel.getTasks()));

		} catch (Exception e) {

			Log.e("LV1", "Null");
		}

	}
 
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
}
