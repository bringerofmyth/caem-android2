package caemandroid.activities;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import caemandroid.http.HttpUtility;

import com.example.caemandroid.R;
import com.example.caemandroid.R.layout;
import com.example.caemandroid.R.menu;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class WelcomeScreenActivity extends Activity {

	private Button bPlaces, bInterests, bEvents, bLocations, bMessages, bRegistrations;
	private JSONArray jsonArray, jsonArrayRegs;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome_screen);
		findComponents();
		new registeredEventsAsyncTask(HttpUtility.GET_USER_EVENTS_URL);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.welcome_screen, menu);
		return true;
	}

	private void bringRegistrations(){
		new bringRegistrationsAsyncTask(HttpUtility.WaitMessage);
	}
	private void findComponents(){
		bRegistrations = (Button) findViewById(R.id.wRegistrationsButton);
		bRegistrations.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	bringRegistrations();
            }
        });
		bPlaces = (Button) findViewById(R.id.wPlacesButton);
		bPlaces.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {

	            }
	        });
		
		bInterests = (Button) findViewById(R.id.wInterestsButton);
		bInterests.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {

	            }
	        });
		bEvents = (Button) findViewById(R.id.wEventsButton);
		bEvents.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {

	            }
	        });
		bLocations = (Button) findViewById(R.id.wHistoryLocation);
		bLocations.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {

	            }
	        });
		bMessages = (Button) findViewById(R.id.wMessagesButton);
		bMessages.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {

	            }
	        });
	}
	
	private class registeredEventsAsyncTask extends AsyncTask<Void, Void, Void> {
		String modalMesaj;
		ProgressDialog dialog;
		List<NameValuePair> pairs = new ArrayList <NameValuePair>(1);

        public registeredEventsAsyncTask(String mMesaj) {
            this.modalMesaj = mMesaj;
            this.dialog = new ProgressDialog(WelcomeScreenActivity.this);
        }

        
        @Override
        protected void onPreExecute() {
        	
        	pairs.add(new BasicNameValuePair("Id", String.valueOf(HttpUtility.passedUser)));
        	dialog.setMessage(modalMesaj);
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            dialog.show();
            
        }
        
		@Override
		protected Void doInBackground(Void... params) {
			
			jsonArray = HttpUtility.createGetList(HttpUtility.GET_USER_EVENTS_URL, pairs);
			return null;
		}
		 @Override
	        protected void onPostExecute(Void str) {
	 
	            if (dialog.isShowing())
	                dialog.dismiss();
	            try {
		           	 
	            	if(jsonArray !=null && jsonArray.length()>0){
	            		HttpUtility.passedUserEvents = jsonArray;
	            	
	            	}
	            	else{
		            	HttpUtility.toastMessage(WelcomeScreenActivity.this, "No user event found.");
		            	//finish();
	            	}

	            	
	               // String id = jsonObject.getString("Telefon").toString();
	              
	                	                
	 
	            } catch (Exception e) {
	            	HttpUtility.toastMessage(WelcomeScreenActivity.this, "Exception: No event found");
	                e.printStackTrace();
	            }
	 
	           // text.setText(responseBody);
	 
	        }
	    }
	private class bringRegistrationsAsyncTask extends AsyncTask<Void, Void, Void> {
		String modalMesaj;
		ProgressDialog dialog;
		List<NameValuePair> pairs = new ArrayList <NameValuePair>(1);

        public bringRegistrationsAsyncTask(String mMesaj) {
            this.modalMesaj = mMesaj;
            this.dialog = new ProgressDialog(WelcomeScreenActivity.this);
        }

        
        @Override
        protected void onPreExecute() {
        	
        	pairs.add(new BasicNameValuePair("Id", String.valueOf(HttpUtility.passedUser)));
        	dialog.setMessage(modalMesaj);
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            dialog.show();
            
        }
        
		@Override
		protected Void doInBackground(Void... params) {
			
			jsonArrayRegs = HttpUtility.createGetList(HttpUtility.GET_USER_REGISTRATIONS_URL, pairs);
			return null;
		}
		 @Override
	        protected void onPostExecute(Void str) {
	 
	            if (dialog.isShowing())
	                dialog.dismiss();
	            try {
		           	 
	            	if(jsonArrayRegs !=null && jsonArrayRegs.length()>0){
	            		HttpUtility.passedUserRegistrations = jsonArrayRegs;
	            		HttpUtility.createIntent(WelcomeScreenActivity.this, RegistrationsListActivity.class);
	            	
	            	}
	            	else{
		            	HttpUtility.toastMessage(WelcomeScreenActivity.this, "No user registrations found.");
		            	//finish();
	            	}

	            	
	               // String id = jsonObject.getString("Telefon").toString();
	              
	                	                
	 
	            } catch (Exception e) {
	            	HttpUtility.toastMessage(WelcomeScreenActivity.this, "Exception: No registration found");
	                e.printStackTrace();
	            }
	 
	           // text.setText(responseBody);
	 
	        }
	    }


}
