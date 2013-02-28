package il.co.shenkar.todoproject.entities;

/* ************************************
 *  Shenkar Java mobile final project
 * 
 *  Created by:
 *  Ben Diamant (bend0303@gmail.com)
 *  Or Guz (Orguz100@gmail.com)
 * 	=- Handed on 02/13 -=
 * 
 * ************************************
 *  Task Class
 * ************************************
 */
import java.io.Serializable;
import java.util.Comparator;

public class TaskDetails implements Serializable {

	public static final int TODO_MODE = 9001;
	public static final int DONE_MODE = 9002;
	private static final long serialVersionUID = -1282594779289783019L;
	private String taskTitle;
	private int taskId;
	private long taskActionTime;
	private double taskCreationTimeStamp;
	private String addressLine;
	private boolean timeReminder, locReminder;
	private int taskMode;

	public long getTaskActionTime() {
		return taskActionTime;
	}

	public void setTaskActionTime(long taskActionTime) {
		this.taskActionTime = taskActionTime;
	}

	public TaskDetails(String taskTitle, long taskActionTime, double taskCreationTimeStamp, String addressLine, boolean timeReminder,
			boolean locReminder, int taskMode) {
		super();
		this.taskTitle = taskTitle;
		this.taskActionTime = taskActionTime;
		this.taskCreationTimeStamp = taskCreationTimeStamp;
		this.addressLine = addressLine;
		this.timeReminder = timeReminder;
		this.locReminder = locReminder;
		this.taskMode = taskMode;
	}

	public TaskDetails(String taskTitle, int taskId, long taskActionTime, double taskCreationTimeStamp, String addressLine,
			boolean timeReminder, boolean locReminder, int taskMode) {
		super();
		this.taskTitle = taskTitle;
		this.taskId = taskId;
		this.taskActionTime = taskActionTime;
		this.taskCreationTimeStamp = taskCreationTimeStamp;
		this.addressLine = addressLine;
		this.timeReminder = timeReminder;
		this.locReminder = locReminder;
		this.taskMode = taskMode;
	}

	public TaskDetails() {
	}

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

	public void setTaskId(int id) {
		taskId = id;
	}

	public boolean isTimeReminder() {
		return timeReminder;
	}

	public void setTimeReminder(boolean timeReminder) {
		this.timeReminder = timeReminder;
	}

	public boolean isLocReminder() {
		return locReminder;
	}

	public void setLocReminder(boolean locReminder) {
		this.locReminder = locReminder;
	}

	public String getAddressLine() {
		return addressLine;
	}

	public void setAddressLine(String addressLine) {
		this.addressLine = addressLine;
	}

	public int getTaskMode() {
		return taskMode;
	}

	public void setTaskMode(int taskMode) {
		this.taskMode = taskMode;
	}

	public static class DateCompe implements Comparator<TaskDetails> {

		@Override
		public int compare(TaskDetails lhs, TaskDetails rhs) {
			// TODO Auto-generated method stub
			return (int) (rhs.getCreationTimeStamp() - lhs.getCreationTimeStamp());
		}

	}

}
