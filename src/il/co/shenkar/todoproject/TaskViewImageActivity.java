package il.co.shenkar.todoproject;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

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


		
}
