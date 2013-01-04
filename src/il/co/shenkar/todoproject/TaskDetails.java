package il.co.shenkar.todoproject;

import java.io.Serializable;
import java.util.Comparator;


public class TaskDetails implements Serializable{

	private static final long serialVersionUID = -1282594779289783019L;
	private String taskTitle;
	private String taskDesc;
	private int taskId;
	private String taskActionTime;
	private double taskCreationTimeStamp;

	public String getTaskActionTime() {
		return taskActionTime;
	}

	public void setTaskActionTime(String taskActionTime) {
		this.taskActionTime = taskActionTime;
	}

	public TaskDetails(String taskTitle, String taskDesc,
			String taskActionTime, double taskCreationTimeStamp) {
		super();
		this.taskTitle = taskTitle;
		this.taskDesc = taskDesc;
		this.taskActionTime = taskActionTime;
		this.taskCreationTimeStamp = taskCreationTimeStamp;
	}
	
	public TaskDetails(int taskId, String taskTitle, String taskDesc,
			 double taskCreationTimeStamp, String taskActionTime) {
		super();
		this.taskTitle = taskTitle;
		this.taskDesc = taskDesc;
		this.taskId = taskId;
		this.taskActionTime = taskActionTime;
		this.taskCreationTimeStamp = taskCreationTimeStamp;
	}
	
	public TaskDetails() {}

	public String getTaskTitle() {
		return taskTitle;
	}
	public void setTaskTitle(String inputTaskName) {
		taskTitle = inputTaskName;
	}
	public int getTaskId() {
		return taskId;
	}

	public Double getCreationTimeStamp() {
		return taskCreationTimeStamp;
	}
	public void setCreationTimeStamp(Double inputCreationTimeStamp) {
		taskCreationTimeStamp = inputCreationTimeStamp;
	}
	public String getTaskDesc() {
		return taskDesc;
	}
	public void setTaskDesc(String taskDesc) {
		this.taskDesc = taskDesc;
	}

	public void setTaskId(int id) {
		taskId = id;
	}
	public static class DateCompe implements Comparator<TaskDetails>
	{

		@Override
		public int compare(TaskDetails lhs, TaskDetails rhs) {
			// TODO Auto-generated method stub
			return (int) (rhs.getCreationTimeStamp() - lhs.getCreationTimeStamp());
		}


	}

}
