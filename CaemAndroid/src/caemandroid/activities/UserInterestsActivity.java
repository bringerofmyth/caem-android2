package caemandroid.activities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import caemandroid.activities.EventsActivity.EventListAdapter;
import caemandroid.activities.PlacesActivity.PlaceListAdapter.PlaceViewHolder;
import caemandroid.entity.EventsListObject;
import caemandroid.entity.InterestsListObject;
import caemandroid.entity.PlacesListObject;
import caemandroid.entity.Tag;
import caemandroid.http.HttpUtility;

import com.example.caemandroid.R;
import com.example.caemandroid.R.layout;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class UserInterestsActivity extends Activity {

	private JSONArray interests;
	Activity myContext;
	MyCustomAdapter dataAdapter = null;

	private ListView view1;
	private JSONObject json3;
	private ArrayList<InterestsListObject> interestList;
	private ArrayList<InterestsListObject> userInterestList;
	private Button saveButton, createButton;
    private JSONArray jsonArray ;
    ArrayList<InterestsListObject> selectedList = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_interests);
		
		 displayListView();
		 //checkButtonClick();
		//arrangeInterests();
		
		
		     
	     saveButton = (Button) findViewById(R.id.intSaveButton);
	     saveButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				StringBuffer responseText = new StringBuffer();
			    responseText.append("The following were selected...\n");
			 
			    selectedList = dataAdapter.intList;
			    for(int i=0;i<selectedList.size();i++){
			    	InterestsListObject sTag = selectedList.get(i);
			     if(!(sTag.isSelected())){
			    	 selectedList.remove(sTag);
			     }
			    }
			 
			    /*Toast.makeText(getApplicationContext(),
			      responseText, Toast.LENGTH_LONG).show();*/
				
			
				new saveInterestsAsyncTask(HttpUtility.WaitMessage).execute();
				
			}
		});
	     
	     createButton = (Button) findViewById(R.id.intCreateButton);
	     createButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				HttpUtility.startIntent(UserInterestsActivity.this, CreateInterestActivity.class);
				
			}
	     });

	}

	private void displayListView() {

		//Array list of countries
		JSONArray cast = HttpUtility.passedTags;
		JSONArray userCast = HttpUtility.passedUserTags;
		if(cast ==null || cast.length()<1 ){
			HttpUtility.toastMessage(UserInterestsActivity.this, "No interests found.");
		}
		else
		{
			//boolean isExist =false;
			JSONObject pla1 = null;
		
			interestList = new ArrayList<InterestsListObject>();
			try {
				if(userCast != null && userCast.length()>0){
					for(int j = 0; j < cast.length(); j ++){
						JSONObject obj;
						obj = cast.getJSONObject(j);
						InterestsListObject h = new InterestsListObject(obj.getString("Id"), obj.getString("Title"));
						interestList.add(h);
						String oId = obj.getString("Id");
						for (int i=0; i<userCast.length(); i++) {
							
							pla1 = userCast.getJSONObject(i);
							String uId = pla1.getString("Id");
							if(uId.equals(oId)){
								modifyListObject(uId);
								break;
							}
							

						}
						

					}
					

				}
				else{
					for (int i=0; i<cast.length(); i++) {
						JSONObject pla;

						pla = cast.getJSONObject(i);
						InterestsListObject h = new InterestsListObject(pla.getString("Id"), pla.getString("Title"));
						interestList.add(h);
						}
			
				}

				
			}catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				HttpUtility.toastMessage(UserInterestsActivity.this, e.getStackTrace().toString());
			}
			//hotelArray[i] =h;
		}





	  //create an ArrayAdaptar from the String Array
	  dataAdapter = new MyCustomAdapter(this,
	    R.layout.checkbox_list_interest, interestList);
	  ListView listView = (ListView) findViewById(R.id.inListView1);
	  // Assign adapter to ListView
	  listView.setAdapter(dataAdapter);
	 
	 
	/*  listView.setOnItemClickListener(new OnItemClickListener() {
	   public void onItemClick(AdapterView<?> parent, View view,
	     int position, long id) {
	    // When clicked, show a toast with the TextView text
	    Country country = (Country) parent.getItemAtPosition(position);
	    Toast.makeText(getApplicationContext(),
	      "Clicked on Row: " + country.getName(), 
	      Toast.LENGTH_LONG).show();
	   }
	  });*/
	 
	 }
		private void modifyListObject(String idd){
		for (InterestsListObject o  : interestList) {
			if(o.getId().equals(idd))
			{
				o.setSelected(true);
				interestList.set(interestList.indexOf(o),o);
			}
		} 
	}
	private String createDTO(){
		StringBuffer bfr = new StringBuffer();
		if(selectedList != null && !selectedList.isEmpty()){
			for (int i = 0 ; i < selectedList.size(); i++) {

				bfr.append(selectedList.get(i).getId());
				if(i<selectedList.size()-1){
					bfr.append(",");
				}

			}
			return bfr.toString();
		}
		else{
			return null;
		}
		
	}
	private class saveInterestsAsyncTask extends AsyncTask<Void, Void, Void> {
		String modalMesaj;
		ProgressDialog dialog;
		List<NameValuePair> pairs = new ArrayList<NameValuePair>(1);
		JSONObject jo = new JSONObject();
		public saveInterestsAsyncTask(String mMesaj) {
			this.modalMesaj = mMesaj;
			this.dialog = new ProgressDialog(UserInterestsActivity.this);
		}

		@Override
		protected void onPreExecute() {
			try {
				Tag t = new Tag();
				t.setTitle(createDTO());
				//jo.put("userTagList", t);
				pairs.add(new BasicNameValuePair("id", String.valueOf(HttpUtility.passedUser)));
				pairs.add(new BasicNameValuePair("userTagList", t.getTitle()));

				//jo.put("id", HttpUtility.passedUser);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		
			dialog.setMessage(modalMesaj);
			dialog.setIndeterminate(true);
			dialog.setCancelable(false);
			dialog.show();

		}

		@Override
		protected Void doInBackground(Void... params) {

			jsonArray = HttpUtility.createPostList2(HttpUtility.POST_UPDATE_USER_TAGS_URL,pairs);
			return null;
		}

		@Override
		protected void onPostExecute(Void str) {

			if (dialog.isShowing())
				dialog.dismiss();
			try {

				if (jsonArray != null && jsonArray.length() > 0) {

				HttpUtility.toastMessage(UserInterestsActivity.this, "Interests saved.");

				} else {
					HttpUtility.toastMessage(UserInterestsActivity.this,
							"Interest could not be save found. "+jsonArray.toString());
					// finish();
				}

				// String id = jsonObject.getString("Telefon").toString();

			} catch (Exception e) {
				HttpUtility.toastMessage(UserInterestsActivity.this,
						"Exception: Interests exception");
				e.printStackTrace();
			}

			// text.setText(responseBody);

		}
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
		      Toast.makeText(getApplicationContext(),
		       "Clicked on Checkbox: " + cb.getText() +
		       " is " + cb.isChecked(), 
		       Toast.LENGTH_LONG).show();
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
		 
		 private void checkButtonClick() {
		 
		 
		  Button myButton = (Button) findViewById(R.id.intSaveButton);
		  myButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			    StringBuffer responseText = new StringBuffer();
			    responseText.append("The following were selected...\n");
			 
			    ArrayList<InterestsListObject> countryList = dataAdapter.intList;
			    for(int i=0;i<countryList.size();i++){
			    	InterestsListObject country = countryList.get(i);
			     if(country.isSelected()){
			      responseText.append("\n" + country.getName());
			     }
			    }
			 
			    Toast.makeText(getApplicationContext(),
			      responseText, Toast.LENGTH_LONG).show();
				
			}
			});
		  
	
	
		 }
}
	

