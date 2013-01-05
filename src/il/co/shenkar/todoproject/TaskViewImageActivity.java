package il.co.shenkar.todoproject;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

public class TaskViewImageActivity extends Activity {
	private ListView lv1;
	TaskDataBastModule dataModel;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i("TaskViewImageActivity", "Activity Created");
		setContentView(R.layout.activity_main);
		dataModel = TaskDataBastModule.getInstance(getApplicationContext());

		lv1 = (ListView) findViewById(R.id.listV_main);
		ImageButton addButton = (ImageButton) findViewById(R.id.addButton);

		addButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						TaskAddActivity.class);
				intent.putExtra("MODE", TaskAddActivity.ADD_MODE);
				startActivity(intent);
			}
		});

		lv1.setAdapter(new TaskListBaseAdapter(this, dataModel.getTasks()));

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	protected void onStart() {
		Log.i("TaskViewImageActivity", "Activity Started");
		super.onStart();
	}

	@Override
	protected void onStop() {
		Log.i("TaskViewImageActivity", "Activity Stoped");
		super.onStop();
	}

	@Override
	protected void onResume() {
		Log.i("TaskViewImageActivity", "Activity Resumed");
		((BaseAdapter) lv1.getAdapter()).notifyDataSetChanged();
		super.onResume();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float downXValue = 0;
		float downYValue = 0;
		Log.i("onTouchEvent", "Enter function");
		switch (event.getAction()) {

		case MotionEvent.ACTION_DOWN: {
			downXValue = event.getX();
			downYValue = event.getY();
			Log.i("onTouchEvent", "ACTION_DOWN");
			break;
		}
		case MotionEvent.ACTION_UP: {
            float currentX = event.getX();   
            float currentY= event.getY();
			Log.i("onTouchEvent", "ACTION_UP");

			double sizeInX = Math.abs(downXValue - currentX);
			double sizeInY = Math.abs(downYValue - currentY);
			if (sizeInX > sizeInY) {
				// you better swipe horizontally
			} else {
				// you better swipe vertically
				Intent intent = new Intent(getApplicationContext(),
						TaskAddActivity.class);
				startActivity(intent);
			}

		}

		}

		return super.onTouchEvent(event);

	}
}
