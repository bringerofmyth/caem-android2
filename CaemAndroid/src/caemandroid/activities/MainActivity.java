package caemandroid.activities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import caemandroid.http.HttpUtility;

import com.example.caemandroid.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;


@SuppressLint({ "ShowToast", "NewApi" })
public class MainActivity extends Activity {

	private String SENDER_ID = "142649902484";
	private GoogleCloudMessaging gcm;
	private String array_spinner[];
	private Button login, register;
	private EditText userName , pass;
	private String userNameStr="", passStr ="" ;
	private JSONObject jsonObjectU = null;
	private String jsonObjectUstr ="";
	private JSONArray arr = null;
	private Spinner s;
	int posit = -1;
	public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    static final String TAG = "GCMDemo";
    String regid;
    private Context context ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();
        /*
        NotificationsManager.handleNotifications(this, SENDER_ID, MyHandler.class);

        gcm = GoogleCloudMessaging.getInstance(this);

        String connectionString =
        		"Endpoint=sb://caemnotificationhub-ns.servicebus.windows.net/;SharedAccessKeyName=DefaultListenSharedAccessSignature;SharedAccessKey=Ppy9qJ3aoQ/N+O8kH4L1fM07D5qHAEUMI4vYZdwKKz4=";
        hub = new NotificationHub("caemnotificationhub", connectionString, MainActivity.this);

        registerWithNotificationHubs();*/
        
		 if (checkPlayServices()) {
		      
		       gcm = GoogleCloudMessaging.getInstance(this);
	            regid = getRegistrationId(context);
	            
	            if (regid.isEmpty()) {
	                registerInBackground();
	            }
	            else{
	            	HttpUtility.toastMessage(this, "regid is"+regid);
	            }
		        
		    }
		 else{
			 Toast.makeText(this,"no ava", Toast.LENGTH_LONG).show();
		 }
		
	
        array_spinner=new String[2];
        array_spinner[0]="Standard User";
        array_spinner[1]="Event Notifier";

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

			private void toRegisterActivity() {
				// TODO Auto-generated method stub
				
			}
        });
    }

private boolean checkPlayServices() {
    int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
    if (resultCode != ConnectionResult.SUCCESS) {
        if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
            GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                    PLAY_SERVICES_RESOLUTION_REQUEST).show();
        } else {
            Log.i("TAG1", "This device is not supported.");
            finish();
        }
        return false;
    }
    return true;
}
    private void registerInBackground() {
	    new AsyncTask<Void, Object, String>() {
	       
	      

			@Override
			protected String doInBackground(Void... params) {
				String msg = "";
	            try {
	                if (gcm == null) {
	                    gcm = GoogleCloudMessaging.getInstance(context);
	                }
	                regid = gcm.register(SENDER_ID);
	                msg = "Device registered, registration ID=" + regid;
	               // Toast.makeText(MainActivity.this,msg, Toast.LENGTH_LONG).show();
	                // You should send the registration ID to your server over HTTP,
	                // so it can use GCM/HTTP or CCS to send messages to your app.
	                // The request to your server should be authenticated if your app
	                // is using accounts.
	                sendRegistrationIdToBackend();

	                // For this demo: we don't need to send it because the device
	                // will send upstream messages to a server that echo back the
	                // message using the 'from' address in the message.

	                // Persist the regID - no need to register again.
	                storeRegistrationId(context, regid);
	            } catch (IOException ex) {
	                msg = "Error :" + ex.getMessage();
	                // If there is an error, don't just keep trying to register.
	                // Require the user to click a button again, or perform
	                // exponential back-off.
	            }
	            return msg;
			}
			
			 private void sendRegistrationIdToBackend() {
				// TODO Auto-generated method stub
				
			}

			@Override
		        protected void onPostExecute(String msg) {
				 Toast.makeText(MainActivity.this, msg,5).show();
		         //   mDisplay.append(msg + "\n");
		        }
	    }.execute(null, null, null);
	    
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
		/*if(!validationCheck()){
			Toast.makeText(this, "Please fill all fields.",Toast.LENGTH_SHORT ).show();
		}
		else{*/
			new loginAsyncTask(HttpUtility.WaitMessage).execute();
		//}
		
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
        	posit = s.getSelectedItemPosition();
        	try {
				/*jsonObject.put("Username", userNameStr);
				jsonObject.put("Password", passStr);
				jsonObject.put("Role", (posit+1));
        		jsonObject.put("Username", "user1");
				jsonObject.put("Password", "q");
				jsonObject.put("Role", (2));*/
				if(posit==0){
					jsonObject.put("Username", "user1");
					jsonObject.put("Password", "q");
					jsonObject.put("Role", (0));
				}
				else{
					jsonObject.put("Username", "owner");
					jsonObject.put("Password", "q");
					jsonObject.put("Role", (1));
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
	            		HttpUtility.passedUser = jsonObjectU.getInt("Id");
	            		HttpUtility.passedUserObject = jsonObjectU;
	            		/*
	            		
	            		if(posit==0){
	            			HttpUtility.createIntent(MainActivity.this, WelcomeScreenActivity.class );
	            		}
	            		else if(posit == 1){
	            			HttpUtility.createIntent(MainActivity.this, OwnerActivity.class );
	            		}
	            		else{
	            			HttpUtility.toastMessage(MainActivity.this, "Response: User Type not applicable");
	            		}*/
	            		int role = Integer.parseInt(jsonObjectU.getString("Role"));
	            		HttpUtility.toastMessage(MainActivity.this, "Hi "+ jsonObjectU.getString("Username") );
	            		if(role ==0 )
	            		{
	            			//HttpUtility.startIntent(MainActivity.this, OwnerActivity.class );
	            			HttpUtility.startIntent(MainActivity.this, WelcomeScreenActivity.class );
	            		}
	            		else if(role == 1)
	            		{
	            			HttpUtility.startIntent(MainActivity.this, OwnerActivity.class );
	            		}
	            		else
	            		{
	            			HttpUtility.toastMessage(MainActivity.this, "User not found");
	            		}
	            		
	            		

	            	}
	            		/*JSONObject js = new JSONObject(jsonObjectUstr);
	            		String gett = js.getString("Password");
	            	*/
	            	//	HttpUtility.toastMessage(MainActivity.this, arr.toString());
	            	else if (jsonObject == null){
	            		HttpUtility.toastMessage(MainActivity.this, "User not found");
	            		
	            	}
	            	else {
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
	private void storeRegistrationId(Context context, String regId) {
	    final SharedPreferences prefs = getGCMPreferences(context);
	    int appVersion = getAppVersion(context);
	    Log.i(TAG, "Saving regId on app version " + appVersion);
	    SharedPreferences.Editor editor = prefs.edit();
	    editor.putString(PROPERTY_REG_ID, regId);
	    editor.putInt(PROPERTY_APP_VERSION, appVersion);
	    editor.commit();
	}
	
	@SuppressLint("NewApi")
	private String getRegistrationId(Context context) {
	    final SharedPreferences prefs = getGCMPreferences(context);
	    String registrationId = prefs.getString(PROPERTY_REG_ID, "");
	    if (registrationId.isEmpty()) {
	        Log.i(TAG, "Registration not found.");
	        return "";
	    }
	    // Check if app was updated; if so, it must clear the registration ID
	    // since the existing regID is not guaranteed to work with the new
	    // app version.
	    int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
	    int currentVersion = getAppVersion(context);
	    if (registeredVersion != currentVersion) {
	        Log.i(TAG, "App version changed.");
	        return "";
	    }
	    return registrationId;
	}
	private static int getAppVersion(Context context) {
	    try {
	        PackageInfo packageInfo = context.getPackageManager()
	                .getPackageInfo(context.getPackageName(), 0);
	        return packageInfo.versionCode;
	    } catch (NameNotFoundException e) {
	        // should never happen
	        throw new RuntimeException("Could not get package name: " + e);
	    }
	}
	private SharedPreferences getGCMPreferences(Context context) {
	    // This sample app persists the registration ID in shared preferences, but
	    // how you store the regID in your app is up to you.
	    return getSharedPreferences(MainActivity.class.getSimpleName(),
	            Context.MODE_PRIVATE);
	}
		// You need to do the Play Services APK check here too.
		@Override
		protected void onResume() {
		    super.onResume();
		    checkPlayServices();
		}

}
