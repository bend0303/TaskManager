package il.co.shenkar.todoproject;
/* ************************************
 *  Shenkar Java mobile final project
 * 
 *  Created by:
 *  Ben Diamant (bend0303@gmail.com)
 *  Or Guz (Orguz100@gmail.com)
 * 	=- Handed on 02/13 -=
 * 
 * ************************************
 *  Location search activity
 * ************************************
 */
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.analytics.tracking.android.EasyTracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

public class TaskGeoSetActivity extends Activity {
	private LocationManager locationManager;
	public ArrayList<String> suggestions;
	private TaskGeoSubBaseAdapter Adapter;
	private ListView lv;
	private static boolean isOnJob = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_task_geo_set);
		lv = (ListView) findViewById(R.id.addsugliss);

		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

		final EditText location = (EditText) findViewById(R.id.location);
		ImageView search = (ImageView) findViewById(R.id.searchimg);
		search.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Location loc = locationManager.getLastKnownLocation(locationManager.getBestProvider(new Criteria(), false));
				new GetNearestSug(loc).execute(location.getText().toString());

			}

		});

	}

	public void returnLoc(Address a) {
		Intent intent = new Intent();
		intent.putExtra("ADDRESS", a);
		setResult(TaskAddActivity.LOCATION_RES_OK, intent);
		finish();
	}

	private class GetNearestSug extends AsyncTask<String, Integer, ArrayList<Address>> {

		private Location mLocation;
		final static double LON_DEG_PER_KM = 0.012682308180089;
		final static double LAT_DEG_PER_KM = 0.009009009009009;
		private int[] SEARCH_RANGES = { 10, 50, 800, -1 }; // city, region,
															// state, everywhere

		public GetNearestSug(Location location) {
			mLocation = location;

		}

		@Override
		protected ArrayList<Address> doInBackground(String... params) {
			// TODO Auto-generated method stub
			if (isOnJob) {
				return null;
			} else
				isOnJob = true;
			Geocoder gc = new Geocoder(getApplicationContext());
			ArrayList<Address> addresses = new ArrayList<Address>();
			;
			double lat = mLocation.getLatitude();
			double lon = mLocation.getLongitude();
			int i = 0;
			try {
				do {
					if (SEARCH_RANGES[i] != -1) {
						double lowerLeftLatitude = translateLat(lat, -SEARCH_RANGES[i]);
						double lowerLeftLongitude = translateLon(lon, SEARCH_RANGES[i]);
						double upperRightLatitude = translateLat(lat, SEARCH_RANGES[i]);
						double upperRightLongitude = translateLon(lon, -SEARCH_RANGES[i]);

						addresses.addAll(gc.getFromLocationName(params[0], 5, lowerLeftLatitude, lowerLeftLongitude, upperRightLatitude,
								upperRightLongitude));

					} else {
						// last resort, try unbounded call with 20 result
						addresses.addAll(gc.getFromLocationName(params[0], 20));
					}
					i++;
				} while ((addresses == null || addresses.size() < 4) && i < SEARCH_RANGES.length);
			} catch (IOException e) {

			}

			return addresses;
		}

		@Override
		protected void onPostExecute(ArrayList<Address> result) {

			super.onPostExecute(result);
			if (result == null)
				return;
			// suggestions = new ArrayList<String>(arrayToString(result));
			Adapter = new TaskGeoSubBaseAdapter(getApplicationContext(), R.id.sugtext, result);
			Adapter.setActivity(TaskGeoSetActivity.this);
			lv.setAdapter(Adapter);
			lv.setItemsCanFocus(true);
			isOnJob = false;
		}

		private ArrayList<String> arrayToString(ArrayList<Address> result) {
			ArrayList<String> tmpArray = new ArrayList<String>();
			for (Address a : result) {
				tmpArray.add(a.getCountryName() + ", " + a.getFeatureName());
			}
			return tmpArray;

		}

		private double translateLat(double lat, double dx) {
			if (lat > 0)
				return (lat + dx * LAT_DEG_PER_KM);
			else
				return (lat - dx * LAT_DEG_PER_KM);
		}

		private double translateLon(double lon, double dy) {
			if (lon > 0)
				return (lon + dy * LON_DEG_PER_KM);
			else
				return (lon - dy * LON_DEG_PER_KM);

		}

	}

	@Override
	protected void onStart() {
		EasyTracker.getInstance().setContext(this);
		EasyTracker.getInstance().activityStart(this);
		super.onStart();
	}
	@Override
	protected void onStop() {
		EasyTracker.getInstance().setContext(this);
		EasyTracker.getInstance().activityStop(this);
		super.onStop();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_task_geo_set, menu);
		return true;
	}

}
