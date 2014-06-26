package caemandroid.activities;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;

import caemandroid.entity.InterestsListObject;
import caemandroid.http.HttpUtility;

import com.example.caemandroid.R;

public class PlaceTagsActivity extends Activity {
	InterestCustomAdapter dataAdapter = null;
	private Button saveButton, createButton;
    private JSONArray jsonArray ;
    private ArrayList<InterestsListObject> selectedList = null;
    private ArrayList<InterestsListObject> interestList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_place_tags);
		displayListView();
	    saveButton = (Button) findViewById(R.id.pTagsSaveButton);
	    saveButton.setOnClickListener(new View.OnClickListener() {
	    	
			@Override
			public void onClick(View v) {
				/*StringBuffer responseText = new StringBuffer();
			    responseText.append("The following were selected...\n");*/
				
				selectedList =null;
				selectedList = new ArrayList<InterestsListObject>();
			    selectedList.addAll(dataAdapter.intList);
			    for (InterestsListObject  elm : dataAdapter.intList) {
			    	
			     if(!(elm.isSelected())){
			    	 selectedList.remove(elm);
			     }
			    }
			    if(CreatePlaceActivity.taglist == null){
			    	CreatePlaceActivity.taglist = new ArrayList<InterestsListObject>();
			    }
			    CreatePlaceActivity.taglist.clear();
			    CreatePlaceActivity.taglist.addAll( selectedList);
		    
		
			 
			finish();
				
				
			}
		});
	    
		}
		private void displayListView() {

			//Array list of countries
			JSONArray cast = HttpUtility.passedTags;
			//JSONArray userCast = HttpUtility.passedUserTags;
			if(cast ==null || cast.length()<1 ){
				HttpUtility.toastMessage(PlaceTagsActivity.this, "No interests found.");
			}
			else
			{
				//boolean isExist =false;
				JSONObject pla1 = null;
			
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
					HttpUtility.toastMessage(PlaceTagsActivity.this, e.getStackTrace().toString());
				}
				//hotelArray[i] =h;
			}

		  dataAdapter = new InterestCustomAdapter(this,
		    R.layout.checkbox_list_interest, interestList);
		  ListView listView = (ListView) findViewById(R.id.pTagsListView1);
		  // Assign adapter to ListView
		  listView.setAdapter(dataAdapter);
		 
	 
		 }
		
	private class InterestCustomAdapter extends ArrayAdapter<InterestsListObject> {
		 
		  private ArrayList<InterestsListObject> intList;
		 
		  public InterestCustomAdapter(Context context, int textViewResourceId,    ArrayList<InterestsListObject> interestList) {
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
}
