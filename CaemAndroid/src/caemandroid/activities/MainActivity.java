package caemandroid.activities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import caemandroid.entity.User;
import caemandroid.http.HttpConnectionObject;
import caemandroid.http.HttpUtility;

import com.example.caemandroid.R;

import android.os.AsyncTask;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

@SuppressLint("ShowToast")
public class MainActivity extends Activity {

	private String array_spinner[];
	private Button login, register;
	private EditText userName , pass;
	private String userNameStr="", passStr ="" ;
	private JSONObject jsonObject = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        array_spinner=new String[2];
        array_spinner[0]="Standard User";
        array_spinner[1]="Place/Event Owner";

        Spinner s = (Spinner) findViewById(R.id.mUsertypeSpinner);
        ArrayAdapter adapter = new ArrayAdapter(this,
        android.R.layout.simple_spinner_item, array_spinner);
        s.setAdapter(adapter);
 
        userName = (EditText) findViewById(R.id.mUsernameEdit);
        pass = (EditText) findViewById(R.id.mPasswordEdit);
        
        
        login = (Button) findViewById(R.id.mLoginButton);
        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               toLoginActivity();
            }
        });
        register = (Button) findViewById(R.id.mRegisterButton);
        register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               toRegisterActivity();
            }
        });
    }


    protected void toRegisterActivity() {
    	Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
		
	}


    private boolean validationCheck (){
    	if(userName.getText() == null || userName.getText().toString().equals("") || pass.getText() ==null
    			|| pass.getText().toString().equals("")  ){
    		return false;
    	}
    	else{
    		userNameStr = userName.getText().toString();
    		passStr = pass.getText().toString();
    		return true;
    	}
    	
    	
    }
	protected void toLoginActivity() {
		if(!validationCheck()){
			Toast.makeText(this, "Please fill all fields.",Toast.LENGTH_SHORT ).show();
		}
		else{
			new loginAsyncTask(HttpUtility.WaitMessage).execute();
		}
		
	}
	@SuppressLint("NewApi")
	private class loginAsyncTask extends AsyncTask<Void, Void, Void> {
		String modalMesaj;
		ProgressDialog dialog;
		String productName = "";
		List<NameValuePair> pairs = new ArrayList <NameValuePair>(2);

        public loginAsyncTask(String mMesaj) {
            this.modalMesaj = mMesaj;
            this.dialog = new ProgressDialog(MainActivity.this);
        }

        @Override
        protected void onPreExecute() {
            
        	pairs.add(new BasicNameValuePair("Username", userNameStr));
        	pairs.add(new BasicNameValuePair("Password", passStr));
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
		 @Override
	        protected void onPostExecute(Void str) {
	 
	            if (dialog.isShowing())
	                dialog.dismiss();
	            try {
		           	 
	            	if(jsonObject !=null && jsonObject.getString("Username")!=null && !jsonObject.getString("Username").isEmpty() ){
	            		HttpUtility.passedJson = jsonObject;
	            		HttpUtility.createIntent(MainActivity.this, WelcomeScreenActivity.class );

		            	return;
	            	}
	            	HttpUtility.toastMessage(MainActivity.this, "Username/password combination is not matched.");
	            	
	               // String id = jsonObject.getString("Telefon").toString();
	              
	                	                
	 
	            } catch (JSONException e) {
	            	HttpUtility.toastMessage(MainActivity.this, "Exception:Username/password combination is not matched.");
	                e.printStackTrace();
	            }
	 
	           // text.setText(responseBody);
	 
	        }
	    }
	

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
