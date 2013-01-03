package il.co.shenkar.todoproject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.IntentService;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

public class Ex6RandomTaskService extends IntentService {

	private static String asyncTaskUrl = "http://mobile1-tasks-dispatcher.herokuapp.com/task/random";

	public Ex6RandomTaskService() {
		super("Ex6RandomTaskService");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		try {
			URL[] urls = { new URL(asyncTaskUrl) };
			new GetFromWebTask().execute(urls);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private class GetFromWebTask extends AsyncTask<URL, Integer, String> {
		@Override
		protected String doInBackground(URL... url) {
			String response = "";
			URL remoteUrl = url[0];
			BufferedReader reader;
			try {
				HttpURLConnection connection = (HttpURLConnection) remoteUrl
						.openConnection();
				connection.connect();
				reader = new BufferedReader(new InputStreamReader(
						connection.getInputStream()));
				StringBuilder responseBuilder = new StringBuilder();
				for (String line = reader.readLine(); line != null; line = reader
						.readLine()) {
					responseBuilder.append(line);
				}
				response = responseBuilder.toString();

			} catch (Exception e) {
				Log.e("doInBackground", e.getMessage());
			}
			return response;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			try {
				JSONObject jsonResponse = new JSONObject(result);
				createTask(jsonResponse);
			} catch (JSONException e) {
				Log.e("onPostExecute", "Could not create JSON object");
			}
		}

		public void createTask(JSONObject json) {
			DateFormat formatDateTime = DateFormat.getDateTimeInstance();

			TaskDetails newTask;
			try {
				newTask = new TaskDetails(json.getString("topic"),
						json.getString("description"),
						formatDateTime.toString(), System.currentTimeMillis());
				TaskDataBastModule tasksModel = TaskDataBastModule
						.getInstance(getApplicationContext());
				tasksModel.addTask(newTask);
				tasksModel.sortTasks();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
}
