package caemandroid.activities;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import caemandroid.entity.Place;
import caemandroid.http.HttpUtility;

import com.example.caemandroid.R;
import com.example.caemandroid.R.layout;
import com.example.caemandroid.R.menu;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.view.Menu;
import android.widget.TextView;

public class PlaceDetailActivity extends Activity {

	private JSONObject jsonObject;
	private String placeId = "";
	private TextView name, address, desc, openHours, phone;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_place_detail);
		name = (TextView) findViewById(R.id.pdNameText);
		address =(TextView) findViewById(R.id.pdAddressText);
		desc = (TextView) findViewById(R.id.pdDescriptionText);
		openHours = (TextView) findViewById(R.id.pdOpenHoursText);
		phone = (TextView) findViewById(R.id.pdPhoneText);

		
		placeId = getIntent().getStringExtra("Id");
		requestPlace();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.place_detail, menu);
		return true;
	}
	private void requestPlace()
	{
		if (placeId.equals(""))
		{
			HttpUtility.toastMessage(this, "No place retrieved");
			//finish();
		}
		else{
			new placeAsyncTask(HttpUtility.WaitMessage).execute();
			

		}
	}
	
	private class placeAsyncTask extends AsyncTask<Void, Void, Void> {
		String modalMesaj;
		ProgressDialog dialog;
		//List<NameValuePair> pairs = new ArrayList <NameValuePair>(1);

        public placeAsyncTask(String mMesaj) {
            this.modalMesaj = mMesaj;
            this.dialog = new ProgressDialog(PlaceDetailActivity.this);
        }

        
        @Override
        protected void onPreExecute() {
        	
        	//pairs.add(new BasicNameValuePair("Id", placeId));
        	dialog.setMessage(modalMesaj);
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            dialog.show();
            
        }
        
		@Override
		protected Void doInBackground(Void... params) {
			
			jsonObject = HttpUtility.createGetRequestSingle(HttpUtility.GET_PLACE_URL, placeId);
			return null;
		}
		 @Override
	        protected void onPostExecute(Void str) {
	 
	            if (dialog.isShowing())
	                dialog.dismiss();
	            try {
		           	 
	            	if(jsonObject !=null && !HttpUtility.isNullOrEmpty(jsonObject.getString("Name"))){
	            		HttpUtility.passedJson = jsonObject;
	            		Place p = HttpUtility.parsePlace(jsonObject);
	        			if(p !=null) {
	        				name.setText(p.getName());
	        				address.setText(p.getAddress());
	        				desc.setText(p.getDescription());
	        				phone.setText(p.getPhone());
	        				openHours.setText(p.getOpenHours());
	        				
	        			}else{
	        				HttpUtility.toastMessage(PlaceDetailActivity.this, "No place retrieved in parsing");
	        				//finish();
	        			}
	            	
	            	}
	            	else{
		            	HttpUtility.toastMessage(PlaceDetailActivity.this, "No place found.");
		            	//finish();
	            	}

	            	
	               // String id = jsonObject.getString("Telefon").toString();
	              
	                	                
	 
	            } catch (JSONException e) {
	            	HttpUtility.toastMessage(PlaceDetailActivity.this, "Exception: No place found.");
	                e.printStackTrace();
	            }
	 
	           // text.setText(responseBody);
	 
	        }
	    }

}
