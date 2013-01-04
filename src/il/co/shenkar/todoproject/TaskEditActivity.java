package il.co.shenkar.todoproject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.support.v4.app.NavUtils;
import android.text.style.UpdateLayout;

public class TaskEditActivity extends Activity {

	private Button timeBtn, dateBtn, addButton, randomButton, cancelButton;
	private DateFormat formatDateTime = DateFormat.getDateTimeInstance();
	private Calendar dateTime;
	private Calendar tempDateTime;
	private TextView timeLabel;
	private EditText taskTitle, taskDesc;
	TaskDetails taskToEdit;
	
	private void getAllbyId() {
		timeLabel = (TextView) findViewById(R.id.timeTxt);
		timeBtn = (Button) findViewById(R.id.timeBtn);
		taskTitle = (EditText) findViewById(R.id.taskTitleInput);
		taskDesc = (EditText) findViewById(R.id.taskDescInput);
		dateBtn = (Button) findViewById(R.id.dateBtn);
		addButton = (Button) findViewById(R.id.addTaskButton);
		cancelButton = (Button) findViewById(R.id.cancelAddButton);
		
	}
	private void setInfoByTask() {
		
		try {
			DateFormat sdf = SimpleDateFormat.getDateTimeInstance();
			Date date = sdf.parse(taskToEdit.getTaskActionTime());
			dateTime.setTime(date);
			tempDateTime.setTime(date);
			updateLabel(dateTime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	private void updateLabel(Calendar inputDateTime) {
		timeLabel.setText(formatDateTime.format(dateTime.getTime()));
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_edit);
		getAllbyId();
		taskToEdit = TaskDataBastModule.getInstance(getApplicationContext()).getTaskById((Integer) getIntent().getExtras().get("ID"));
		setInfoByTask();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
