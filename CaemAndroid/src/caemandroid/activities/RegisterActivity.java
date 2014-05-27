package caemandroid.activities;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;


import caemandroid.http.HttpUtility;

import com.example.caemandroid.R;

import android.os.AsyncTask;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class RegisterActivity extends Activity {

	private String array_spinner[];
	private Button cancel, register;
	private EditText username, pass, name, surname, email, phone;
	private String strUsernam, strPass, strName, strSurname, strEmail, strPhone, strUserType;
	private Spinner userType;
	private JSONObject jsonObject = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		userType = (Spinner) findViewById(R.id.rUsertypeSpinner);
	        ArrayAdapter adapter = new ArrayAdapter(this,
	        android.R.layout.simple_spinner_item, array_spinner);
	        userType.setAdapter(adapter);
	        username = (EditText) findViewById(R.id.rUsernameEdit);
	        pass = (EditText) findViewById(R.id.rPasswordEdit);
	        name = (EditText) findViewById(R.id.rNameEdit);
	        surname = (EditText) findViewById(R.id.rSurnameEdit);
	        email = (EditText) findViewById(R.id.rEmailEdit);
	        phone = (EditText) findViewById(R.id.rPhoneEdit);
	        
	       
	        register = (Button) findViewById(R.id.rRegisterButton);
	        register.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {
	            	 if(!validateAndRetrieve())
	     	        {
	     	        	HttpUtility.toastMessage(RegisterActivity.this,"Please fill username and password!");
	     	        }
	            	 else{
	            		 new registerAsyncTask(HttpUtility.WaitMessage).execute();
	            	 }
	            }
	        });
	        
	        cancel = (Button) findViewById(R.id.rCancelButton);
	        cancel.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {
	            	HttpUtility.createIntent(RegisterActivity.this, MainActivity.class);
	            }
	        });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}


	private boolean validateAndRetrieve(){
		if(username.getText() == null || username.getText().toString().equals("") || pass.getText() ==null
    			|| pass.getText().toString().equals("")  ){
    		return false;
    	}
    	else{
    		strUsernam = username.getText().toString();
    		strPass = pass.getText().toString();
    		strEmail = email.getText().toString();
    		strName = name.getText().toString();
    		strPhone = phone.getText().toString();
    		strSurname = surname.getText().toString();
    		strUserType = String.valueOf(userType.getSelectedItemPosition());
    		return true;
    	}
	}
	private class registerAsyncTask extends AsyncTask<Void, Void, Void> {
		String modalMesaj;
		ProgressDialog dialog;
		String productName = "";
		List<NameValuePair> pairs = new ArrayList <NameValuePair>(5);

        public registerAsyncTask(String mMesaj) {
            this.modalMesaj = mMesaj;
            this.dialog = new ProgressDialog(RegisterActivity.this);
        }

        @Override
        protected void onPreExecute() {
            
        	pairs.add(new BasicNameValuePair("Username", strUsernam));
        	pairs.add(new BasicNameValuePair("Password", strPass));
        	pairs.add(new BasicNameValuePair("Email", strEmail));
        	pairs.add(new BasicNameValuePair("Name", strName));
        	pairs.add(new BasicNameValuePair("Phone", strPhone));
        	pairs.add(new BasicNameValuePair("Surname", strSurname));
        	pairs.add(new BasicNameValuePair("UserType", strUserType));
        	dialog.setMessage(modalMesaj);
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            dialog.show();
            
        }
        
		@Override
		protected Void doInBackground(Void... params) {
			
			jsonObject = HttpUtility.createPostRequest(HttpUtility.POST_USER_URL, pairs);
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
	            		HttpUtility.createIntent(RegisterActivity.this, WelcomeScreenActivity.class );

		            	return;
	            	}
	            	HttpUtility.toastMessage(RegisterActivity.this, "Problem occured in registering user.");
	            	
	               // String id = jsonObject.getString("Telefon").toString();
	              
	                	                
	 
	            } catch (JSONException e) {
	            	HttpUtility.toastMessage(RegisterActivity.this, "Exception:Problem occured in registering user.");
	                e.printStackTrace();
	            }
	 
	           // text.setText(responseBody);
	 
	        }
	    }
	
}
