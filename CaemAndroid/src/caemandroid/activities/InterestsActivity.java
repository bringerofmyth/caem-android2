package caemandroid.activities;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import caemandroid.entity.InterestsListObject;
import caemandroid.entity.Tag;
import caemandroid.http.HttpUtility;

import com.example.caemandroid.R;
import com.example.caemandroid.R.layout;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

public class InterestsActivity extends Activity {
	Activity myContext;
	MyCustomAdapter dataAdapter = null;

	private ArrayList<InterestsListObject> interestList;
	private Button  createButton;
    ArrayList<InterestsListObject> selectedList = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_interests);
		 displayListView();
		
			     
	     createButton = (Button) findViewById(R.id.ginintCreateButton);
	     createButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				HttpUtility.startIntent(InterestsActivity.this, CreateInterestActivity.class);
				
			}
	     });

	}

	private void displayListView() {

		//Array list of countries
		JSONArray cast = HttpUtility.passedTags;
		if(cast ==null || cast.length()<1 ){
			HttpUtility.toastMessage(InterestsActivity.this, "No interests found.");
		}
		else
		{

			interestList = new ArrayList<InterestsListObject>();
			try {
					for (int i=0; i<cast.length(); i++) {
						JSONObject pla;

						pla = cast.getJSONObject(i);
						InterestsListObject h = new InterestsListObject(pla.getString("Id"), pla.getString("Title"));
						interestList.add(h);
						}
				
			}catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				HttpUtility.toastMessage(InterestsActivity.this, e.getStackTrace().toString());
			}
			//hotelArray[i] =h;
		}





	  //create an ArrayAdaptar from the String Array
	  dataAdapter = new MyCustomAdapter(this,
	    R.layout.checkbox_list_interest, interestList);
	  ListView listView = (ListView) findViewById(R.id.ginListView1);
	  // Assign adapter to ListView
	  listView.setAdapter(dataAdapter);
	 
	 }

	 private class MyCustomAdapter extends ArrayAdapter<InterestsListObject> {
		 
		  private ArrayList<InterestsListObject> intList;
		 
		  public MyCustomAdapter(Context context, int textViewResourceId,    ArrayList<InterestsListObject> interestList) {
		   super(context, textViewResourceId, interestList);
		   this.intList = new ArrayList<InterestsListObject>();
		   this.intList.addAll(interestList);
		  }
		 
		  private class ViewHolder {
		   //TextView code;
		   CheckBox name;
		  }
		 
		  @Override
		  public View getView(int position, View convertView, ViewGroup parent) {
		 
		   ViewHolder holder = null;
		   Log.v("ConvertView", String.valueOf(position));
		 
		   if (convertView == null) {
		   LayoutInflater vi = (LayoutInflater)getSystemService(
		     Context.LAYOUT_INFLATER_SERVICE);
		   convertView = vi.inflate(R.layout.checkbox_list_interest, null);
		 
		   holder = new ViewHolder();
		  // holder.code = (TextView) convertView.findViewById(R.id.code);
		   holder.name = (CheckBox) convertView.findViewById(R.id.ly_ch_interest_checkBox1);
		   convertView.setTag(holder);
		 
		    holder.name.setOnClickListener( new View.OnClickListener() {  
		     public void onClick(View v) {  
		      CheckBox cb = (CheckBox) v ;  
		      InterestsListObject country = (InterestsListObject) cb.getTag();  
		      country.setSelected(cb.isChecked());
		     }  
		    });  
		   } 
		   else {
		    holder = (ViewHolder) convertView.getTag();
		   }
		 
		   InterestsListObject country = intList.get(position);
		 //  holder.code.setText(" (" +  country.getCode() + ")");
		   holder.name.setText(country.getName());
		   holder.name.setChecked(country.isSelected());
		   holder.name.setTag(country);
		 
		   return convertView;
		 
		  }
		 
		 }
	 /*public void updateResults(ArrayList<Contact> results) {
	        searchArrayList = results;
	        //Triggers the list update
	        notifyDataSetChanged();
	    }
	 @Override
	 public void onResume(){
		 
		 super.onResume();
	 }*/
		 
}
	

