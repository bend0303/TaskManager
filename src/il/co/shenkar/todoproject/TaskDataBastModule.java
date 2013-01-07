package il.co.shenkar.todoproject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class TaskDataBastModule extends SQLiteOpenHelper {

	private static TaskDataBastModule instance = null;
	private ArrayList<TaskDetails> tasks;

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "TASKS_DB";

	// Tasks table name
	private static final String TABLE_TASKS = "TASKS";

	// Tasks Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_TITLE = "title";
	private static final String KEY_DESC = "description";
	private static final String KEY_CRE_TIME = "creationTime";
	private static final String KEY_DET_TIME = "determinedTime";

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_TASKS + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_TITLE + " TEXT,"
				+ KEY_DESC + " TEXT," + KEY_CRE_TIME + " TEXT," + KEY_DET_TIME
				+ " TEXT" + ")";
		db.execSQL(CREATE_CONTACTS_TABLE);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXIST " + TABLE_TASKS);
		// recreate table
		onCreate(db);
	}

	private TaskDataBastModule(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		tasks = getTasks();
	}

	public static TaskDataBastModule getInstance(Context context) {
		if (instance == null) {
			instance = new TaskDataBastModule(context);

		}
		return instance;
	}

	void addTask(TaskDetails task) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();

		values.put(KEY_TITLE, task.getTaskTitle());
		values.put(KEY_DESC, task.getTaskDesc());
		values.put(KEY_CRE_TIME, task.getCreationTimeStamp());
		values.put(KEY_DET_TIME, task.getTaskActionTime());
		int id = (int) db.insert(TABLE_TASKS, null, values);
		db.close();
		task.setTaskId(id);
		tasks.add(task);
	}

	void updateTask(TaskDetails task) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		
		values.put(KEY_TITLE, task.getTaskTitle());
		values.put(KEY_DESC, task.getTaskDesc());
		values.put(KEY_DET_TIME, task.getTaskActionTime());
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

		return null;
	}

	public TaskDetails getTaskDB(int id) {
		SQLiteDatabase db = this.getReadableDatabase();
		
		Cursor cursor = db.query(TABLE_TASKS, new String[] { KEY_ID, KEY_TITLE,
				KEY_DESC, KEY_CRE_TIME, KEY_DET_TIME }, KEY_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null);
		if (cursor == null)
			return null;
		cursor.moveToFirst();
		TaskDetails task = new TaskDetails(
				Integer.parseInt(cursor.getString(0)), cursor.getString(1),
				cursor.getString(2), Double.parseDouble(cursor.getString(3)),
				Long.parseLong(cursor.getString(4)));
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
					TaskDetails task = new TaskDetails(Integer.parseInt(cursor
							.getString(0)), cursor.getString(1),
							cursor.getString(2), Double.parseDouble(cursor
									.getString(3)),Long.parseLong(cursor.getString(4)));
					tasks.add(task);
				} while (cursor.moveToNext());
			}
			db.close();
		}

		return tasks;
	}

	private void removeTask(TaskDetails task) {
		String METHOD = "removeTask(TaskDetails task)";
		try {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_TASKS, KEY_ID + " = ?",
				new String[] { String.valueOf(task.getTaskId())});
		db.close();
		Log.d(METHOD, "Successfully removed the task: " + " From the data base");
		} catch(Exception e) {
			e.printStackTrace();
			Log.e(METHOD, "Error occured while trying to remove " + task.getTaskTitle() +" From the data base");
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
	public void remove(int pos) {
		removeTask(getTask(pos));
		tasks.remove(pos);
	}

	public void sortTasks() {
		Collections.sort(tasks, new TaskDetails.DateCompe());
	}
}
