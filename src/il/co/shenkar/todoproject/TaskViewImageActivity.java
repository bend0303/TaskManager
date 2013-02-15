package il.co.shenkar.todoproject;

/* ************************************
 *  Shenkar Java mobile final project
 * 
 *  Created by:
 *  Ben Diamant (bend0303@gmail.com)
 *  Or Guz (Orguz100@gmail.com)
 * 	=- Handed on 02/13 -=
 * 
 * ************************************
 *  Application main activity
 * ************************************
 */
import il.co.shenkar.todoproject.utils.TrackerHelper;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Tracker;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class TaskViewImageActivity extends Activity {
	private ListView lv1, lv2;
	TaskDataBastModule dataModel;
	//private Tracker mGaTracker;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		TabHost tabHost = (TabHost) findViewById(R.id.tabHost);
		tabHost.setup();

		TabSpec spec1 = tabHost.newTabSpec("Tasks");
		spec1.setContent(R.id.tab1);
		spec1.setIndicator("Your Tasks");

		TabSpec spec2 = tabHost.newTabSpec("Done");
		spec2.setIndicator("Done");
		spec2.setContent(R.id.tab2);

		dataModel = TaskDataBastModule.getInstance(getApplicationContext());

		TaskDoneListBaseAdapter doneAdapter = new TaskDoneListBaseAdapter(this, dataModel.getDoneTasks());
		lv2 = (ListView) findViewById(R.id.listV_main2);
		lv2.setAdapter(doneAdapter);
		lv1 = (ListView) findViewById(R.id.listV_main1);
		lv1.setAdapter(new TaskListBaseAdapter(this, dataModel.getTasks(), doneAdapter));
		ImageButton addButton = (ImageButton) findViewById(R.id.addButton1);

		addButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				EasyTracker.getTracker().sendEvent(TrackerHelper.UI_ACTION, TrackerHelper.BUTTON_PRESSED, TrackerHelper.ADD_BUTTON, null);
				Intent intent = new Intent(getApplicationContext(), TaskAddActivity.class);
				intent.putExtra("MODE", TaskAddActivity.ADD_MODE);
				startActivity(intent);
			}
		});

		tabHost.addTab(spec1);
		tabHost.addTab(spec2);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_test_tabs, menu);
		return true;
	}

	@Override
	protected void onResume() {
		Log.i("TaskViewImageActivity", "Activity Resumed");
		((BaseAdapter) lv1.getAdapter()).notifyDataSetChanged();
		((BaseAdapter) lv2.getAdapter()).notifyDataSetChanged();

		super.onResume();
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
}
