package il.co.shenkar.todoproject;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

public class TaskSingleViewActivity extends Activity {

	TextView taskTile, taskDescription, taskTBTime;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_single_view);
		getElementsByIds();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_task_single_view, menu);
		return true;
	}
	
	@Override
	protected void onStart() {
		Log.i("TaskSingleViewActivity", "Activity Started");
		super.onStart();
	}
	@Override
	protected void onStop() {
		Log.i("TaskSingleViewActivity", "Activity Stoped");
		super.onStop();
	}
	@Override
	protected void onResume() {
		Log.i("TaskSingleViewActivity", "Activity Resumed");
		super.onResume();
	}
	private void getElementsByIds() {
		taskDescription = (TextView) findViewById(R.TaskViewIds.taskDescInputView);
		taskTile = (TextView) findViewById(R.TaskViewIds.taskTitleInputView);
		taskTBTime = (TextView) findViewById(R.TaskViewIds.timeTxtView);
		
	}

}
