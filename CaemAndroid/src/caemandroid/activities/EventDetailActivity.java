package caemandroid.activities;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import caemandroid.entity.Event;
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

public class EventDetailActivity extends Activity {

	private JSONObject jsonObject;
	private String eventId = "";
	private boolean isUserRegistered = false;
	private Button register, back;
	private EditText title, startTime, finishTime, eventType, description;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_detail);
		title = (EditText) findViewById(R.id.edTitleText);
		startTime = (EditText) findViewById(R.id.edStartTimeText);
		finishTime = (EditText) findViewById(R.id.edFinishTimeText);
		eventType = (EditText) findViewById(R.id.edEventTypeText);
		description = (EditText) findViewById(R.id.edDescriptionText);
		register = (Button) findViewById(R.id.edRegisterButton);
		
		 
		eventId = getIntent().getStringExtra("Id");
		requestEvent();
		isUserRegistered = isRegistered();
		
		register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	if(isUserRegistered){
            		modifyRegisterButtonView(true);

            	}
            	else{
            		registerUser();
            	}
            	
            }
        });
		back = (Button) findViewById(R.id.edBackButton);
		back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	finish();
            	
            }
        });
		
	}

	private void registerUser(){
		
		new registerUserAsyncTask(HttpUtility.WaitMessage);
	}
	private boolean isRegistered()
	{
		JSONArray evs = HttpUtility.passedUserEvents;
		Integer eId = null;
		
		if(evs!= null){
			int eventIdInt =Integer.parseInt(eventId);
			for (int i = 0; i < evs.length(); i++) {
				try {
					JSONObject object = evs.getJSONObject(i);
					eId = object.getInt("Id");
					if(eId == eventIdInt){
						return true;
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
				
			}
		}
		
		return false;
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.event_detail, menu);
		return true;
	}
	private void requestEvent()
	{
		if (eventId== null || eventId.equals(""))
		{
			HttpUtility.toastMessage(this, "No event retrieved");
			finish();
		}
		else{
			new eventAsyncTask(HttpUtility.WaitMessage).execute();
			Event p = HttpUtility.parseEvent(jsonObject);
			if(p !=null) {
				title.setText(p.getTitle());
				startTime.setText(p.getStartTime());
				description.setText(p.getDescription());
				finishTime.setText(p.getFinishTime());
				eventType.setText(p.getEventType());
				
			}
			else{
				HttpUtility.toastMessage(this, "No event retrieved in parsing");
				finish();
			}

		}
	}
	
	private class eventAsyncTask extends AsyncTask<Void, Void, Void> {
		String modalMesaj;
		ProgressDialog dialog;
		List<NameValuePair> pairs = new ArrayList <NameValuePair>(1);

        public eventAsyncTask(String mMesaj) {
            this.modalMesaj = mMesaj;
            this.dialog = new ProgressDialog(EventDetailActivity.this);
        }

        
        @Override
        protected void onPreExecute() {
        	
        	pairs.add(new BasicNameValuePair("Id", eventId));
        	dialog.setMessage(modalMesaj);
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            dialog.show();
            
        }
        
		@Override
		protected Void doInBackground(Void... params) {
			
			jsonObject = HttpUtility.createGetRequest(HttpUtility.GET_EVENT_URL, pairs);
			return null;
		}
		 @Override
	        protected void onPostExecute(Void str) {
	 
	            if (dialog.isShowing())
	                dialog.dismiss();
	            try {
		           	 
	            	if(jsonObject !=null && !HttpUtility.isNullOrEmpty(jsonObject.getString("Title"))){
	            		HttpUtility.passedJson = jsonObject;
	            	
	            	}
	            	else{
		            	HttpUtility.toastMessage(EventDetailActivity.this, "No event found.");
		            	finish();
	            	}

	            	
	               // String id = jsonObject.getString("Telefon").toString();
	              
	                	                
	 
	            } catch (JSONException e) {
	            	HttpUtility.toastMessage(EventDetailActivity.this, "Exception: No event found");
	                e.printStackTrace();
	            }
	 
	           // text.setText(responseBody);
	 
	        }
	    }

	private class registerUserAsyncTask extends AsyncTask<Void, Void, Void> {
		String modalMesaj;
		ProgressDialog dialog;
		JSONObject jO = new JSONObject();

        public registerUserAsyncTask(String mMesaj) {
            this.modalMesaj = mMesaj;
            this.dialog = new ProgressDialog(EventDetailActivity.this);
        }

        
        @Override
        protected void onPreExecute() {
        	try {
				jO.put("UserId", String.valueOf(HttpUtility.passedUser) );
				jO.put("EventId", eventId);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
        	//pairs.add(new BasicNameValuePair("UserId",String.valueOf(HttpUtility.passedUser )));
        	//pairs.add(new BasicNameValuePair("EventId",eventId ));
        	dialog.setMessage(modalMesaj);
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            dialog.show();
            
        }
        
        
		@Override
		protected Void doInBackground(Void... params) {
			
			jsonObject = HttpUtility.createPostRequest(HttpUtility.POST_REGISTER_USERTOEVENT_URL, jO);
			return null;
		}
		 @Override
	        protected void onPostExecute(Void str) {
	 
	            if (dialog.isShowing())
	                dialog.dismiss();
	            try {
		           	 
	            	if(jsonObject !=null && !HttpUtility.isNullOrEmpty(jsonObject.getString("Id"))){
	            		HttpUtility.passedRegisterInfoJson = jsonObject;
	            		HttpUtility.toastMessage(EventDetailActivity.this, "You are registered!");
	            		modifyRegisterButtonView(true);
	            	
	            	}
	            	else{
		            	HttpUtility.toastMessage(EventDetailActivity.this, "Registration cannot be completed");
	            	}

	            	
	               // String id = jsonObject.getString("Telefon").toString();
	              
	                	                
	 
	            } catch (JSONException e) {
	            	HttpUtility.toastMessage(EventDetailActivity.this, "Exception: Registration cannot be completed");
	                e.printStackTrace();
	            }
	 
	           // text.setText(responseBody);
	 
	        }
	    }
	private void modifyRegisterButtonView(boolean t)
    {
    	if (t){
    		register.setText("Registered");
        	register.setEnabled(false);
    	}
    	else{
    		register.setText("Register");
        	register.setEnabled(true);
    	}
    }
}
