package com.example.caemandroid;

import java.nio.charset.CodingErrorAction;
import java.util.ArrayList;








import org.json.JSONArray;

import caemandroid.activities.CreateEventActivity;
import caemandroid.activities.CreateInterestActivity;
import caemandroid.entity.InterestsListObject;
import caemandroid.http.HttpUtility;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;

public class EventTagsActivity extends Activity {
	InterestCustomAdapter dataAdapter = null;
	private Button saveButton, createButton;
    private JSONArray jsonArray ;
    ArrayList<InterestsListObject> selectedList = null;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_tags);
		
    saveButton = (Button) findViewById(R.id.intSaveButton);
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
		    
		    HttpUtility.startIntent(getApplicationContext(), CreateEventActivity.class,"SelectedListTag",selectedList);
		 
		
			
			
		}
	});
    
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
