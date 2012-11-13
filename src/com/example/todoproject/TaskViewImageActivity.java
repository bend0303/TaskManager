package com.example.todoproject;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ListView;

public class TaskViewImageActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ArrayList<taskDetails> tasks = getTasks();
        final ListView lv1 = (ListView) findViewById(R.id.listV_main);
        lv1.setAdapter(new TaskListBaseAdapter(this, tasks));
    }

    private ArrayList<taskDetails> getTasks() {
		ArrayList<taskDetails> result = new ArrayList<taskDetails>();	
		taskDetails t1 = new taskDetails();
		
		t1.setTaskName("bring food");	
		result.add(t1);
		taskDetails t2 = new taskDetails();
		
		t2.setTaskName("buy milk food");	
		result.add(t2);
		
		
		taskDetails t3 = new taskDetails();		
		t3.setTaskName("take kids to garden");	
		result.add(t3);
		taskDetails t4 = new taskDetails();
		
		t4.setTaskName("go to sleep");	
		result.add(t4);
		
		
		
		
		
		return result;
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
