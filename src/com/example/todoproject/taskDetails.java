package com.example.todoproject;

import java.util.Comparator;


public class taskDetails {
	private String taskTitle;
	private String taskDesc;
	private int taskId;
	private static int taskIdRunner = 0;
	private double creationTimeStamp;
	
	public taskDetails(String title, String desc)
	{
		taskId = ++taskIdRunner;
		taskTitle = title;
		creationTimeStamp = System.currentTimeMillis();
		setTaskDesc(desc);
		
	}
	public String getTaskTitle() {
		return taskTitle;
	}
	public void setTaskTitle(String inputTaskName) {
		taskTitle = inputTaskName;
	}
	public double getTaskId() {
		return taskId;
	}

	public Double getCreationTimeStamp() {
		return creationTimeStamp;
	}
	public void setCreationTimeStamp(Double inputCreationTimeStamp) {
		creationTimeStamp = inputCreationTimeStamp;
	}
	public String getTaskDesc() {
		return taskDesc;
	}
	public void setTaskDesc(String taskDesc) {
		this.taskDesc = taskDesc;
	}


	public static class DateCompe implements Comparator<taskDetails>
	{

		@Override
		public int compare(taskDetails lhs, taskDetails rhs) {
			// TODO Auto-generated method stub
			return (int) (rhs.getCreationTimeStamp() - lhs.getCreationTimeStamp());
		}


	}

}
