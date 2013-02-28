package il.co.shenkar.todoproject.dao;

/* ************************************
 *  Shenkar Java mobile final project
 * 
 *  Created by:
 *  Ben Diamant (bend0303@gmail.com)
 *  Or Guz (Orguz100@gmail.com)
 * 	=- Handed on 02/13 -=
 * 
 * ************************************
 *  SQLite DB Implementation
 * ************************************
 */
import il.co.shenkar.todoproject.entities.TaskDetails;
import il.co.shenkar.todoproject.entities.TaskDetails.DateCompe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class TaskDataBaseModule extends SQLiteOpenHelper {

	private static TaskDataBaseModule instance = null;
	private ArrayList<TaskDetails> tasks;
	private ArrayList<TaskDetails> doneTasks;

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "NEED2DO_DB";

	// Tasks table name
	private static final String TABLE_TASKS = "TASKSTABLE";

	// Tasks Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_TITLE = "title";
	private static final String KEY_CRE_TIME = "creationTime";
	private static final String KEY_DET_TIME = "determinedTime";
	private static final String KEY_ADR_LINE = "addressline";
	private static final String KEY_TIME_REMINDER = "timereminder";
	private static final String KEY_LOC_REMINDER = "locreminder";
	private static final String KEY_MODE = "mode";

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_TASKS_TABLE = "CREATE TABLE " + TABLE_TASKS + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TITLE + " TEXT,"
				+ KEY_CRE_TIME + " TEXT," + KEY_DET_TIME + " TEXT," + KEY_ADR_LINE + " TEXT," + KEY_TIME_REMINDER + " INTEGER,"
				+ KEY_LOC_REMINDER + " INTEGER," + KEY_MODE + " INTEGER" +")";
		db.execSQL(CREATE_TASKS_TABLE);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXIST " + TABLE_TASKS);
		// recreate table
		onCreate(db);
	}

	private TaskDataBaseModule(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		//context.deleteDatabase(DATABASE_NAME);
		tasks = getTasks();
		doneTasks = getDoneTasks();
	}

	public static TaskDataBaseModule getInstance(Context context) {
		if (instance == null) {
			instance = new TaskDataBaseModule(context);
		}
		return instance;
	}

	public void addTask(TaskDetails task) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_TITLE, task.getTaskTitle());
		values.put(KEY_CRE_TIME, task.getCreationTimeStamp());
		values.put(KEY_DET_TIME, task.getTaskActionTime());
		values.put(KEY_ADR_LINE, task.getAddressLine());
		values.put(KEY_TIME_REMINDER, task.isTimeReminder() ? 1 : 0);
		values.put(KEY_LOC_REMINDER, task.isLocReminder() ? 1 : 0);
		values.put(KEY_MODE, task.getTaskMode());
		int id = (int) db.insert(TABLE_TASKS, null, values);
		db.close();
		task.setTaskId(id);
		tasks.add(task);
	}

	public void updateTask(TaskDetails task) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_TITLE, task.getTaskTitle());
		values.put(KEY_CRE_TIME, task.getCreationTimeStamp());
		values.put(KEY_DET_TIME, task.getTaskActionTime());
		values.put(KEY_ADR_LINE, task.getAddressLine());
		values.put(KEY_TIME_REMINDER, task.isTimeReminder() ? 1 : 0);
		values.put(KEY_LOC_REMINDER, task.isLocReminder() ? 1 : 0);
		values.put(KEY_MODE, task.getTaskMode());
		db.update(TABLE_TASKS, values, KEY_ID + "=" + task.getTaskId(), null);
		db.close();
		tasks.remove(getTaskById(task.getTaskId()));
		tasks.add(task);
	}

	public TaskDetails getTask(int pos) {
		return tasks.get(pos);
	}

	public TaskDetails getTaskById(int id) {

		for (TaskDetails t : tasks) {
			if (t.getTaskId() == id)
				return t;
		}
		for (TaskDetails t : doneTasks) {
			if (t.getTaskId() == id)
				return t;
		}

		return null;
	}

	public TaskDetails getTaskDB(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_TASKS, new String[] { KEY_ID, KEY_TITLE, KEY_CRE_TIME, KEY_DET_TIME , KEY_ADR_LINE, KEY_TIME_REMINDER, KEY_LOC_REMINDER, KEY_MODE}, KEY_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null);
		if (cursor == null)
			return null;
		cursor.moveToFirst();
		TaskDetails task = taskFromCursor(cursor);
		db.close();
		return task;
	}

	public ArrayList<TaskDetails> getTasks() {
		if (tasks == null) {
			tasks = new ArrayList<TaskDetails>();
			String selectQuery = "SELECT  * FROM " + TABLE_TASKS;
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			if (cursor.moveToFirst()) {
				do {
					TaskDetails task = taskFromCursor(cursor);
					if (task.getTaskMode() == TaskDetails.TODO_MODE)
						tasks.add(task);
				} while (cursor.moveToNext());
			}
			db.close();
		}

		return tasks;
	}
	public ArrayList<TaskDetails> getDoneTasks() {
		if (doneTasks == null) {
			doneTasks = new ArrayList<TaskDetails>();
			String selectQuery = "SELECT  * FROM " + TABLE_TASKS;
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			if (cursor.moveToFirst()) {
				do {
					TaskDetails task = taskFromCursor(cursor);
					if (task.getTaskMode() == TaskDetails.DONE_MODE)
						doneTasks.add(task);
				} while (cursor.moveToNext());
			}
			db.close();
		}

		return doneTasks;
	}
	
	private TaskDetails taskFromCursor(Cursor cursor) {

		int taskId = Integer.parseInt(cursor.getString(0));
		String taskTitle = cursor.getString(1);
		double taskCreationTimeStamp = Double.parseDouble(cursor.getString(2));
		long taskActionTime = Long.parseLong(cursor.getString(3));
		String addressLine = cursor.getString(4);
		boolean timeReminder = (Integer.parseInt(cursor.getString(5)) == 1) ? true : false; 
		boolean locReminder = (Integer.parseInt(cursor.getString(6)) == 1) ? true : false; 
		int taskMode = Integer.parseInt(cursor.getString(7));
		return new TaskDetails(taskTitle, taskId, taskActionTime, taskCreationTimeStamp, addressLine, timeReminder, locReminder, taskMode);
	}

	public void removeTask(TaskDetails task) {
		String METHOD = "removeTask(TaskDetails task)";
		try {
			SQLiteDatabase db = this.getWritableDatabase();
			db.delete(TABLE_TASKS, KEY_ID + " = ?", new String[] { String.valueOf(task.getTaskId()) });
			db.close();
			if (task.getTaskMode() == TaskDetails.DONE_MODE)
				doneTasks.remove(task);
			else tasks.remove(task);
			Log.d(METHOD, "Successfully removed the task: " + " From the data base");
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(METHOD, "Error occured while trying to remove " + task.getTaskTitle() + " From the data base");
		}

	}

	public int getCount() {
		return tasks.size();
	}

	public int getIdByTag(int pos) {
		if (tasks.get(pos) != null) {
			return tasks.get(pos).getTaskId();
		}
		return -1;

	}
	public void moveToDone(TaskDetails task) {
		task.setTaskMode(TaskDetails.DONE_MODE);
		updateTask(task);
		doneTasks.add(task);
		tasks.remove(task);
	}
	public void remove(int pos) {
		removeTask(getTask(pos));
	}

	public void sortTasks() {
		Collections.sort(tasks, new TaskDetails.DateCompe());
	}
}
