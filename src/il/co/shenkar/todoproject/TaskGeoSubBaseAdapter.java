package il.co.shenkar.todoproject;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.*;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.TextView;


public class TaskGeoSubBaseAdapter extends ArrayAdapter<Address> {

    private ArrayList<Address> items;
    private Context context;
    private Activity parentAct;
		
    public TaskGeoSubBaseAdapter(Context context, int textViewResourceId, ArrayList<Address> items) {
        super(context, textViewResourceId, items);
        this.context = context;
        this.items = items;
    }
    
    public void setActivity(Activity act) {
    	parentAct = act;
    }
    private final OnClickListener locpicker = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Address a = items.get((Integer) v.getTag());
			Intent intent = new Intent(context, TaskAddActivity.class);
			intent.putExtra("Address", a);
		//	TaskGeoSetActivity.this.setResult(TaskGeoSetActivity.RESULT_OK, intent);
			
		}

	};

    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.sugitem, null);
        }
        
        Address item = items.get(position);
        if (item!= null) {
            // My layout has only one TextView
            TextView itemView = (TextView) view.findViewById(R.id.sugtext);
            if (itemView != null) {
                // do whatever you want with your string and long
                itemView.setText(item.getLocality()+", "+item.getCountryName());
            }
         }
        view.setOnClickListener(locpicker);
        view.setTag(position);
        return view;
    }
}