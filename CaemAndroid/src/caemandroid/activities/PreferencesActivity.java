package caemandroid.activities;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;


import caemandroid.http.HttpUtility;

import com.example.caemandroid.R;
import com.example.caemandroid.R.id;
import com.example.caemandroid.R.layout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;

public class PreferencesActivity extends Activity {

	
	private String array_spinner[];
	private Spinner s;
	private int timeIndex, weatherIndex, locationIndex, userLocIndex;
	private Button saveButton ;
	private RadioGroup radioButtonGroupTime, radioButtonGroupLocation, radioButtonGroupWeather ;
	private String time, location, weather, lastlocation;
	private JSONObject jsonObject = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_preferences);
		
		radioButtonGroupTime = (RadioGroup) findViewById(R.id.prefRadioGroupTime);
		radioButtonGroupLocation = (RadioGroup) findViewById(R.id.prefRadioGroupLocation);
		radioButtonGroupWeather = (RadioGroup) findViewById(R.id.prefRadioGroupWeather);
		 array_spinner=new String[3];
	     array_spinner[0]="Ankara";
	     array_spinner[1]="Istanbul";
	     array_spinner[2]="Izmir";
	     
	     s = (Spinner) findViewById(R.id.prefSpinner1);
	        ArrayAdapter adapter = new ArrayAdapter(this,
	        android.R.layout.simple_spinner_item, array_spinner);
	        s.setAdapter(adapter);
	        
	    // userLocIndex = s.getSelectedItemPosition();
	     setViews();
	     saveButton = (Button) findViewById(R.id.prefbutton1);
	     saveButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				setProfiles();
				new profilesSetAsyncTask(HttpUtility.WaitMessage).execute();
				
			}
		});
	 
	}
	private void setViews()  {
		String usloc = "";
		String weather="";
		String loc="";
		String time="";
		try {
			usloc = HttpUtility.passedUserObject.getString("LastLocation");
			weather = HttpUtility.passedUserObject.getString("WeatherProfile");
			loc = HttpUtility.passedUserObject.getString("LocationProfile");
			time = HttpUtility.passedUserObject.getString("TimeProfile");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(usloc.equals("istanbul"))
			 s.setSelection(1);
		else{
			if (usloc.equals("izmir"))
				s.setSelection(2);
			else 
				s.setSelection(0);
		}
		if(time.equals("business-hours"))
			radioButtonGroupTime.check(R.id.preftRadiobusiness);
		else  {
			if(time.equals("non-business-hours"))
				radioButtonGroupTime.check(R.id.preftradiononbuss);
			else{
				if(time.equals("night-hours"))
					radioButtonGroupTime.check(R.id.preftradionight);
				else
					radioButtonGroupTime.check(R.id.preftradiotimenone);
			}
		}
		if(loc.equals("on"))
			radioButtonGroupLocation.check(R.id.preflradioon);
		else 
			radioButtonGroupLocation.check(R.id.preflradiooff);
		
		if(weather.equals("on"))
			radioButtonGroupWeather.check(R.id.prefwradioon);
		else 
			radioButtonGroupWeather.check(R.id.prefwradiooff);
		
	}
	private void setProfiles(){
		int radioButtonTimeID = radioButtonGroupTime.getCheckedRadioButtonId();
		View radioButtonTimeIndex = radioButtonGroupTime.findViewById(radioButtonTimeID);
		timeIndex = radioButtonGroupTime.indexOfChild(radioButtonTimeIndex);
		if(timeIndex==1){
			time = "business-hours";
		}
		else if(timeIndex ==2 )
			time  = "non-business-hours";
		else if(timeIndex==3)
			time = "night-hours";
		else
			time="none";
		
		int radioButtonWeatherID = radioButtonGroupWeather.getCheckedRadioButtonId();
		View radioButtonWeatherIndex = radioButtonGroupWeather.findViewById(radioButtonWeatherID);
		weatherIndex = radioButtonGroupWeather.indexOfChild(radioButtonWeatherIndex);
		
		if(weatherIndex==1){
			weather = "off";
		}
		else
			weather="on";
		
		int radioButtonLocationID = radioButtonGroupLocation.getCheckedRadioButtonId();
		View radioButtonLocationIndex = radioButtonGroupLocation.findViewById(radioButtonLocationID);
		locationIndex = radioButtonGroupLocation.indexOfChild(radioButtonLocationIndex);
		
		if(locationIndex ==1)
			location ="off";
		else 
			location= "on";
		userLocIndex = s.getSelectedItemPosition();
		if(userLocIndex==1)
			lastlocation="istanbul";
		else if(userLocIndex==2)
			lastlocation="izmir";
		else 
			lastlocation="ankara";
	}
	
	private class profilesSetAsyncTask extends AsyncTask<Void, Void, Void> {
		String modalMesaj;
		ProgressDialog dialog;
		//List<NameValuePair> pairs = new ArrayList <NameValuePair>(0);
		JSONObject jo = new JSONObject();
	
        public profilesSetAsyncTask(String mMesaj) {
            this.modalMesaj = mMesaj;
            this.dialog = new ProgressDialog(PreferencesActivity.this);
        }

        @Override
        protected void onPreExecute() {
        	try{
        		jo.put("TimeProfile", time);
            	jo.put("WeatherProfile", weather);
            	jo.put("LocationProfile", location);
            	jo.put("LastLocation", lastlocation);
            	jo.put("Id", HttpUtility.passedUser);
        	}catch (JSONException e){
        		
        	}
             	

        	dialog.setMessage(modalMesaj);
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            dialog.show();
            
        }
        
        
		@Override
		protected Void doInBackground(Void... params) {
			
			jsonObject = HttpUtility.createPostRequest(HttpUtility.POST_SET_PREFERENCES_URL, jo);
			return null;
		}
		 @SuppressLint("NewApi")
		@Override
	        protected void onPostExecute(Void str) {
	 
	            if (dialog.isShowing())
	                dialog.dismiss();
	            try {
		           	 
	            	if(jsonObject !=null && jsonObject.getString("Id")!=null && !jsonObject.getString("Id").isEmpty() ){
	            		HttpUtility.passedJson = jsonObject;
	            		HttpUtility.toastMessage(PreferencesActivity.this, "Preferences updated.");
	            		//finish();
		            	return;
	            	}
	            	HttpUtility.toastMessage(PreferencesActivity.this, "Problem occured in setting preferences");
	            	
	               // String id = jsonObject.getString("Telefon").toString();
	              
	                	                
	 
	            } catch (JSONException e) {
	            	HttpUtility.toastMessage(PreferencesActivity.this, "Exception:Problem occured in setting preferences");
	                e.printStackTrace();
	            }
	 
	           // text.setText(responseBody);
	 
	        }
	    }

}
