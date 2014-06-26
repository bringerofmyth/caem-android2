package caemandroid.activities;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.caemandroid.R;
import com.example.caemandroid.R.id;
import com.example.caemandroid.R.layout;

import caemandroid.entity.InterestsListObject;
import caemandroid.http.HttpUtility;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class CreatePlaceActivity extends Activity {

	private Button clear, create, tagButton;
	private EditText name, address, description, phone, openHours;
	private String strName, strAddress, strDescription, strPhone, strOpenHours;
	private JSONObject jsonObject = null;
	public static ArrayList<InterestsListObject> taglist = new ArrayList<InterestsListObject>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_place);
		findComponents();
	}
	private void findComponents(){
		name = (EditText) findViewById(R.id.cpNameEdit);
		address = (EditText) findViewById(R.id.cpAddressEdit);
		description = (EditText) findViewById(R.id.cpDescriptionEdit);
		phone = (EditText) findViewById(R.id.cpPhoneEdit);
		openHours = (EditText) findViewById(R.id.cpOpenHoursEdit);
		
		tagButton = (Button) findViewById(R.id.cpAddTagButton);
		tagButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				HttpUtility.startIntent(CreatePlaceActivity.this, PlaceTagsActivity.class);
				
			}
		});
		create = (Button) findViewById(R.id.cpCreateButton);
		create.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				createPlace();
				
			}
		});

		
	
	}
	private boolean validateAndCheck (){
		strName = name.getText().toString();
		strDescription = description.getText().toString();
		strAddress = address.getText().toString();
		strOpenHours = openHours.getText().toString();
		strPhone = phone.getText().toString();
	
		if(HttpUtility.isNullOrEmpty(strName) ){
			return false;
		}
		else{
			return true;
		}

	}
	private void createPlace(){
		if(validateAndCheck() == false)
		{
			HttpUtility.toastMessage(CreatePlaceActivity.this, "Please fill Name field");
		}
		else{
			//taglist  = (ArrayList<InterestsListObject>) getIntent().getSerializableExtra("SelectedListTag");
			new createPlaceAsyncTask(HttpUtility.WaitMessage).execute();
		}
	
	}
	private class createPlaceAsyncTask extends AsyncTask<Void, Void, Void> {
		String modalMesaj;
		ProgressDialog dialog;
		JSONObject j = new JSONObject();
		String tags=null;

        public createPlaceAsyncTask(String mMesaj) {
            this.modalMesaj = mMesaj;
            this.dialog = new ProgressDialog(CreatePlaceActivity.this);
        }

        @Override
        protected void onPreExecute() {
            if(taglist !=null && !taglist.isEmpty()){
            	tags = HttpUtility.composeTagDTO(taglist);
            	try {
					j.put("Tags", tags);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	//pairs.add(new BasicNameValuePair("Tags", tags));
            }
			try {
				j.put("Name", strName);
				j.put("Phone", strPhone);
				j.put("Address", strAddress);
				j.put("Description", strDescription);
				j.put("OpenHours", strOpenHours);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	/*pairs.add(new BasicNameValuePair("Name", strName));
        	pairs.add(new BasicNameValuePair("Phone", strPhone));
        	pairs.add(new BasicNameValuePair("Description", strDescription));
        	pairs.add(new BasicNameValuePair("Address", strAddress));
        	pairs.add(new BasicNameValuePair("OpenHours",strOpenHours));*/
        	

        	dialog.setMessage(modalMesaj);
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            dialog.show();
            
        }
        
        
		@Override
		protected Void doInBackground(Void... params) {
			
			jsonObject = HttpUtility.createPostRequest(HttpUtility.POST_CREATE_PLACE_URL, j);
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
	            		HttpUtility.toastMessage(CreatePlaceActivity.this, "Place created, notifications sent.");
	            		finish();
		            	return;
	            	}
	            	HttpUtility.toastMessage(CreatePlaceActivity.this, "Problem occured in creating place");
	            	
	               // String id = jsonObject.getString("Telefon").toString();
	              
	                	                
	 
	            } catch (JSONException e) {
	            	HttpUtility.toastMessage(CreatePlaceActivity.this, "Exception:Problem occured in creating place");
	                e.printStackTrace();
	            }
	 
	           // text.setText(responseBody);
	 
	        }
	    }
	@Override
	public void onResume(){
		TextView txt = (TextView) findViewById(R.id.cpTagtextView2);
		if(taglist != null && !taglist.isEmpty()){
			txt.setText("Added");
		}
		else{
			txt.setText("none");
		}
		super.onResume();
	}

}
