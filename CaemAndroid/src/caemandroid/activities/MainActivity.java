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
import org.json.JSONArray;
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
	private JSONObject jsonObjectU = null;
	private String jsonObjectUstr ="";
	private JSONArray arr = null;
	private Spinner s;
	int posit = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        array_spinner=new String[2];
        array_spinner[0]="Standard User";
        array_spinner[1]="Place/Event Owner";

         s = (Spinner) findViewById(R.id.mUsertypeSpinner);
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
		JSONObject jsonObject = new JSONObject();
		
		List<NameValuePair> pairs = new ArrayList <NameValuePair>(2);

        public loginAsyncTask(String mMesaj) {
            this.modalMesaj = mMesaj;
            this.dialog = new ProgressDialog(MainActivity.this);
        }

        @Override
        protected void onPreExecute() {
        	try {
				jsonObject.put("Username", userNameStr);
				jsonObject.put("Password", passStr);
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
        
        	posit = s.getSelectedItemPosition();
        	
        	
        	//pairs.add(new BasicNameValuePair("Id", String.valueOf(2)));
        	//pairs.add(new BasicNameValuePair("UserType", String.valueOf(posit)));
        	if(posit == -1){
        		HttpUtility.toastMessage(MainActivity.this, "User Type not applicable");
        		this.cancel(true);
        	}
        	dialog.setMessage(modalMesaj);
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            dialog.show();
            
        }
        
		@Override
		protected Void doInBackground(Void... params) {
			//arr = HttpUtility.createGetList(HttpUtility.POST_USER_URL, pairs);
			jsonObjectU = HttpUtility.createPostRequest(HttpUtility.POST_LOGIN_USER_URL, jsonObject);
			return null;
		}
		 @Override
	        protected void onPostExecute(Void str) {
	 
	            if (dialog.isShowing())
	                dialog.dismiss();
	            try {
		           	 
	            	if(jsonObjectU !=null && jsonObjectU.getString("Id")!=null && !jsonObjectU.getString("Id").isEmpty() ){
	            		HttpUtility.passedUser = jsonObjectU.getInt("Id");/*
	            		if(posit==0){
	            			HttpUtility.createIntent(MainActivity.this, WelcomeScreenActivity.class );
	            		}
	            		else if(posit == 1){
	            			HttpUtility.createIntent(MainActivity.this, OwnerActivity.class );
	            		}
	            		else{
	            			HttpUtility.toastMessage(MainActivity.this, "Response: User Type not applicable");
	            		}*/
	            		HttpUtility.toastMessage(MainActivity.this, "Found with id: "+jsonObjectU.getString("Id")+" and name: "+ jsonObjectU.getString("Name") );
	            		HttpUtility.createIntent(MainActivity.this, WelcomeScreenActivity.class );
	            	}
	            		/*JSONObject js = new JSONObject(jsonObjectUstr);
	            		String gett = js.getString("Password");
	            	*/
	            	//	HttpUtility.toastMessage(MainActivity.this, arr.toString());
	            	else{
	            		HttpUtility.toastMessage(MainActivity.this, "not found: "+ jsonObjectU.toString() );
	            	}

	            	
	            	//HttpUtility.toastMessage(MainActivity.this, "Username/password combination is not matched.");
	            	
	               // String id = jsonObject.getString("Telefon").toString();
	              
	                	                
	 
	            } catch (Exception e) {
	            	HttpUtility.toastMessage(MainActivity.this, e.toString());
	            	//HttpUtility.toastMessage(MainActivity.this, "Exception:Username/password combination is not matched.");
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
