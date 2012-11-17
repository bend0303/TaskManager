package com.example.todoproject;

import java.util.ArrayList;
import java.util.Collections;

public class TaskDataBastModule {

	private static TaskDataBastModule instance = null; 
	private ArrayList<taskDetails> tasks;
	private TaskDataBastModule() {
		
	}
	
	public static TaskDataBastModule getInstance() {
		if (instance == null) {
			instance = new TaskDataBastModule();
		}
		return instance;
	}
	
	public static TaskDataBastModule getInstance(ArrayList<taskDetails> inputTasks) {
		if (instance == null)	{
			instance = new TaskDataBastModule();
			instance.setTasks(inputTasks);
		}
		return instance;
	}
	
	public ArrayList<taskDetails> getTasks () {
		return tasks;
	}
	
	public int getCount() {
		return tasks.size();
	}
	
	private void setTasks(ArrayList<taskDetails> input) {
		tasks = input;
	}
	
	public taskDetails getTask(int pos) {
		return tasks.get(pos);
	}
	
	public void sortTasks() {
		Collections.sort(tasks, new taskDetails.DateCompe());
	}
	
}
