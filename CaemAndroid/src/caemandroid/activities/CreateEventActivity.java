package caemandroid.activities;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import caemandroid.entity.Tag;
import caemandroid.http.HttpUtility;

import com.example.caemandroid.R;
import com.example.caemandroid.R.layout;

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
import android.widget.Spinner;

public class CreateEventActivity extends Activity {


	private Button clear, create;
	private EditText title, startTime, startDate, finishTime, finishDate, desc;
	private List<Tag> selectedTags = new ArrayList();
	private String strTitle, strStartTime, strStartDate, strFinishTime, strFinishDate, strDesc;
	private JSONObject jsonObject = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_event);
		findComponents();
		
		
	}
	private boolean validateAndCheck (){
		strTitle = title.getText().toString();
		strStartDate = startDate.getText().toString();
		strStartTime = startTime.getText().toString();
		strFinishDate = finishDate.getText().toString();
		strFinishTime = finishTime.getText().toString();
		strDesc = desc.getText().toString();
		if(HttpUtility.isNullOrEmpty(strTitle, strStartDate, strFinishDate, strDesc)){
			return false;
		}
		else{
			return true;
		}

	}
	private void createEvent (){
		if(validateAndCheck() == false)
		{
			HttpUtility.toastMessage(CreateEventActivity.this, "Please fill required fields");
		}
		else{
			new createEventAsyncTask(HttpUtility.WaitMessage).execute();
		}
	
	}
	private void findComponents(){
		title = (EditText) findViewById(R.id.ceTitleEdit);
		startTime = (EditText) findViewById(R.id.ceStartTimeDateEdit);
		startDate = (EditText) findViewById(R.id.ceStartDateEdit);
		finishTime = (EditText) findViewById(R.id.ceFinishTimeEdit);
		finishDate = (EditText) findViewById(R.id.ceFinishDateEdit);
		desc = (EditText) findViewById(R.id.ceDescriptionEdit);
		clear = (Button) findViewById(R.id.ceClearButton);
		clear.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				title.setText("");
				startTime.setText("");
				startDate.setText("");
				finishTime.setText("");
				finishDate.setText("");
				desc.setText("");
			}
		});
		
		create = (Button) findViewById(R.id.ceCreateButton);
		create.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				createEvent();
				
			}
		});

		
	
	}
	private class createEventAsyncTask extends AsyncTask<Void, Void, Void> {
		String modalMesaj;
		ProgressDialog dialog;
		List<NameValuePair> pairs = new ArrayList <NameValuePair>(0);
		

        public createEventAsyncTask(String mMesaj) {
            this.modalMesaj = mMesaj;
            this.dialog = new ProgressDialog(CreateEventActivity.this);
        }

        @Override
        protected void onPreExecute() {
            
        	pairs.add(new BasicNameValuePair("Title", strTitle));
        	pairs.add(new BasicNameValuePair("StartTime", strStartTime));
        	//pairs.add(new BasicNameValuePair("StartDate", strStartDate));
        	//pairs.add(new BasicNameValuePair("FinishDate", strFinishDate));
        	pairs.add(new BasicNameValuePair("FinishTime",strFinishTime));
        	pairs.add(new BasicNameValuePair("Description", strDesc));
        	int ids=-1;
        	int sSize = selectedTags.size();
        	if(sSize>0 ){
        		
        	}
        	for (int i = 0; i < selectedTags.size(); i++) {
				ids= selectedTags.get(i).getId();
				pairs.add(new BasicNameValuePair("tag[]",String.valueOf(ids)));
			}
        	dialog.setMessage(modalMesaj);
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            dialog.show();
            
        }
        
		@Override
		protected Void doInBackground(Void... params) {
			
			jsonObject = HttpUtility.createPostRequest(HttpUtility.POST_CREATE_EVENT_URL, pairs);
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
	            		HttpUtility.toastMessage(CreateEventActivity.this, "Event created, notifications sent.");

		            	return;
	            	}
	            	HttpUtility.toastMessage(CreateEventActivity.this, "Problem occured in creating event");
	            	
	               // String id = jsonObject.getString("Telefon").toString();
	              
	                	                
	 
	            } catch (JSONException e) {
	            	HttpUtility.toastMessage(CreateEventActivity.this, "Exception:Problem occured in creating event");
	                e.printStackTrace();
	            }
	 
	           // text.setText(responseBody);
	 
	        }
	    }
	
}
