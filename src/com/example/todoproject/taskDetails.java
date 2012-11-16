package com.example.todoproject;

import java.util.Date;

public class taskDetails {
	private String taskTitle;
	private String taskDesc;
	private int taskId;
	private static int taskIdRunner = 0;
	private Date creationTimeStamp;
	
	public taskDetails(String title, String desc)
	{
		taskId = ++taskIdRunner;
		taskTitle = title;
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

	public Date getCreationTimeStamp() {
		return creationTimeStamp;
	}
	public void setCreationTimeStamp(Date inputCreationTimeStamp) {
		creationTimeStamp = inputCreationTimeStamp;
	}
	public String getTaskDesc() {
		return taskDesc;
	}
	public void setTaskDesc(String taskDesc) {
		this.taskDesc = taskDesc;
	}
	

}
