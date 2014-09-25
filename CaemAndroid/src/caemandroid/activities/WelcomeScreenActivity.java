package caemandroid.activities;

import java.io.IOException;
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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class WelcomeScreenActivity extends Activity {
	private String SENDER_ID = "142649902484";
	private GoogleCloudMessaging gcm;
	private Button bPlaces, bInterests, bEvents, bLocations, bMessages,
			bRegistrations, bPreferences;
	private JSONArray jsonArrayU,jsonArrayA, jsonArrayP,jsonArrayE, jsonArrayRegs;
	private JSONObject jsonDeviceCheck = null;
    private String regid;
    private static boolean regToBeSent = false;
    private Context context ;
	public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    static final String TAG = "GCMDemo";
    private boolean deviceRegistered = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome_screen);
		context = getApplicationContext();
		new interestsAsyncTask(HttpUtility.WaitMessage).execute();
		
		bRegistrations = (Button) findViewById(R.id.wRegistrationsButton);
		bRegistrations.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				bringRegistrations();
			}
		});
		bPlaces = (Button) findViewById(R.id.wPlacesButton);
		bPlaces.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				new placesAsyncTask(HttpUtility.WaitMessage).execute();
			}
		});

		bInterests = (Button) findViewById(R.id.wInterestsButton);
		bInterests.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				//new interestsAsyncTask(HttpUtility.WaitMessage).execute();
				
					new userInterestsAsyncTask(HttpUtility.WaitMessage).execute();

			
			}
		});
		bPreferences = (Button) findViewById(R.id.wPreferencesButton);
		bPreferences.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				//new interestsAsyncTask(HttpUtility.WaitMessage).execute();
				
					HttpUtility.startIntent(WelcomeScreenActivity.this, PreferencesActivity.class);

			
			}
		});
		bEvents = (Button) findViewById(R.id.wEventsButton);
		bEvents.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				new eventsAsyncTask(HttpUtility.WaitMessage).execute();
			}
		});

		bMessages = (Button) findViewById(R.id.wMessagesButton);
		bMessages.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				pushMe();
			}

		});
		validateOrRegisterGCM();
		

	}

	private void pushMe() {
		new pushAsyncTask(HttpUtility.WaitMessage).execute();
		
	}
	@SuppressLint("NewApi")
	private void validateOrRegisterGCM(){
		 
		 if (checkPlayServices()) {
		      
		       gcm = GoogleCloudMessaging.getInstance(this);
	            regid = getRegistrationId(context);
	            userDeviceCheck(HttpUtility.passedUser, regid);
	          
	         
	            
		    }
		 else{
			 Toast.makeText(this,"no ava", Toast.LENGTH_LONG).show();
		 }
	}
	
	@SuppressLint("NewApi")
	private void userDeviceCheck(int userId, String registId){
		if(registId==null || registId.isEmpty()){
			
			registerInBackground();
		}
		else{
			new deviceCheckAsyncTask(HttpUtility.WaitMessage).execute();
			
		}
	}
	private boolean checkPlayServices() {
	    int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
	    if (resultCode != ConnectionResult.SUCCESS) {
	        if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
	            GooglePlayServicesUtil.getErrorDialog(resultCode, this,
	                    PLAY_SERVICES_RESOLUTION_REQUEST).show();
	        } else {
	            Log.i("TAG1", "This device is not supported.");
	            HttpUtility.toastMessage(WelcomeScreenActivity.this, "This device is not supported.");
	           // finish();
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
		               // Toast.makeText(WelcomeScreenActivity.this,msg, Toast.LENGTH_LONG).show();
		                // You should send the registration ID to your server over HTTP,
		                // so it can use GCM/HTTP or CCS to send messages to your app.
		                // The request to your server should be authenticated if your app
		                // is using accounts.
		                regToBeSent= true;
		                
		                // For this demo: we don't need to send it because the device
		                // will send upstream messages to a server that echo back the
		                // message using the 'from' address in the message.

		                // Persist the regID - no need to register again.
		                storeRegistrationId(context, regid);
		            } catch (IOException ex) {
		               // msg = "Error :" + ex.getMessage();
		               
		                // If there is an error, don't just keep trying to register.
		                // Require the user to click a button again, or perform
		                // exponential back-off.
		            }
		            return msg;
				}
				


				@Override
			        protected void onPostExecute(String msg) {
					
					Toast.makeText(WelcomeScreenActivity.this, msg,5).show();
					if(regToBeSent)
						sendRegistrationIdToBackend();
			         //   mDisplay.append(msg + "\n");
			        }
		    }.execute(null, null, null);
		    
		}
		 private void sendRegistrationIdToBackend() {
			 new registerDeviceAsyncTask(HttpUtility.WaitMessage).execute();
			
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.welcome_screen, menu);
		return true;
	}

	protected void bringRegistrations() {
		new bringRegistrationsAsyncTask(HttpUtility.WaitMessage).execute();
	}

	private void findComponents() {
		
	}

	private class placesAsyncTask extends AsyncTask<Void, Void, Void> {
		String modalMesaj;
		ProgressDialog dialog;
		String paramid = null;

		public placesAsyncTask(String mMesaj) {
			this.modalMesaj = mMesaj;
			this.dialog = new ProgressDialog(WelcomeScreenActivity.this);
		}

		@Override
		protected void onPreExecute() {

			paramid = String.valueOf(HttpUtility.passedUser);
			dialog.setMessage(modalMesaj);
			dialog.setIndeterminate(true);
			dialog.setCancelable(false);
			dialog.show();

		}

		@Override
		protected Void doInBackground(Void... params) {

			jsonArrayP = HttpUtility.createGetListSingle(
					HttpUtility.GET_RECOMM_PLACES_URL, paramid);
			return null;
		}

		@Override
		protected void onPostExecute(Void str) {

			if (dialog.isShowing())
				dialog.dismiss();
			try {

				if (jsonArrayP != null && jsonArrayP.length() > 0) {
					HttpUtility.passedPlaces = jsonArrayP;
					HttpUtility.startIntent(WelcomeScreenActivity.this,
							PlacesActivity.class);

				} else {
					HttpUtility.toastMessage(WelcomeScreenActivity.this,
							"No  place found.");
					// finish();
				}

				// String id = jsonObject.getString("Telefon").toString();

			} catch (Exception e) {
				HttpUtility.toastMessage(WelcomeScreenActivity.this,
						"Exception: No place found");
				e.printStackTrace();
			}

			// text.setText(responseBody);

		}
	}

	private class userInterestsAsyncTask extends AsyncTask<Void, Void, Void> {
		String modalMesaj;
		ProgressDialog dialog;
		//((List<NameValuePair> pairs = new ArrayList<NameValuePair>(1);

		public userInterestsAsyncTask(String mMesaj) {
			this.modalMesaj = mMesaj;
			this.dialog = new ProgressDialog(WelcomeScreenActivity.this);
		}

		@Override
		protected void onPreExecute() {

			//pairs.add(new BasicNameValuePair("Id", String.valueOf(HttpUtility.passedUser)));
			dialog.setMessage(modalMesaj);
			dialog.setIndeterminate(true);
			dialog.setCancelable(false);
			dialog.show();

		}

		@Override
		protected Void doInBackground(Void... params) {

			jsonArrayU = HttpUtility.createGetListSingle(
					HttpUtility.GET_USER_TAGS_URL, String
					.valueOf(HttpUtility.passedUser));
			
			return null;
		}

		@Override
		protected void onPostExecute(Void str) {

			if (dialog.isShowing())
				dialog.dismiss();
			try {

				if(jsonArrayU == null ||jsonArrayU.length()<1  ){
					HttpUtility.toastMessage(WelcomeScreenActivity.this,
							"User interests not added yet.");
				}
				else{
					HttpUtility.passedUserTags = jsonArrayU;
				}
				
				HttpUtility.startIntent(WelcomeScreenActivity.this,
						UserInterestsActivity.class);
				

			} catch (Exception e) {
				HttpUtility.toastMessage(WelcomeScreenActivity.this,
						"Exception: No place found");
				e.printStackTrace();
			}

			// text.setText(responseBody);

		}
	}

	private class interestsAsyncTask extends AsyncTask<Void, Void, Void> {
		String modalMesaj;
		ProgressDialog dialog;

		public interestsAsyncTask(String mMesaj) {
			this.modalMesaj = mMesaj;
			this.dialog = new ProgressDialog(WelcomeScreenActivity.this);
		}

		@Override
		protected void onPreExecute() {

			/*pairs.add(new BasicNameValuePair("Id", String
					.valueOf(HttpUtility.passedUser)));*/
			dialog.setMessage(modalMesaj);
			dialog.setIndeterminate(true);
			dialog.setCancelable(false);
			dialog.show();

		}

		@Override
		protected Void doInBackground(Void... params) {

			jsonArrayA = HttpUtility.createGetList(
					HttpUtility.GET_TAGS_URL, null);
			return null;
		}

		@Override
		protected void onPostExecute(Void str) {

			if (dialog.isShowing())
				dialog.dismiss();
			try {

				if(jsonArrayA!=null && jsonArrayA.length()>0){
					//HttpUtility.toastMessage(WelcomeScreenActivity.this, "Tags received...");
				}
				else{
					HttpUtility.toastMessage(WelcomeScreenActivity.this, "No tags found...");
				}
				HttpUtility.passedTags = jsonArrayA;


			} catch (Exception e) {
				HttpUtility.toastMessage(WelcomeScreenActivity.this,
						"Exception: No total interests found");
				e.printStackTrace();
			}

			// text.setText(responseBody);

		}
	}
	private class deviceCheckAsyncTask extends AsyncTask<Void, Void, Void> {
		String modalMesaj;
		ProgressDialog dialog;
		JSONObject j = new JSONObject ();
		//List<NameValuePair> pairs = new ArrayList <NameValuePair>(0);
		public deviceCheckAsyncTask(String mMesaj) {
			this.modalMesaj = mMesaj;
			this.dialog = new ProgressDialog(WelcomeScreenActivity.this);
		}

		@Override
		protected void onPreExecute() {	
			dialog.setMessage(modalMesaj);
			dialog.setIndeterminate(true);
			dialog.setCancelable(false);
			dialog.show();
			try {
				j.put("UserId", String.valueOf(HttpUtility.passedUser));
				j.put("DeviceId", regid);
				jsonDeviceCheck = new JSONObject();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}

		@Override
		protected Void doInBackground(Void... params) {
			jsonDeviceCheck = HttpUtility.createPostRequest(
					HttpUtility.POST_USER_DEVICE_CHECK_URL, j);
			return null;
		}

		@Override
		protected void onPostExecute(Void str) {

			if (dialog.isShowing())
				dialog.dismiss();
			try {

				if (jsonDeviceCheck != null ) {
					deviceRegistered = jsonDeviceCheck.getBoolean("RegisteredDevice");
					if(!deviceRegistered)
						registerInBackground();
					else
						HttpUtility.toastMessage(WelcomeScreenActivity.this, "Device / User matches");
					
				} else {
					HttpUtility.toastMessage(WelcomeScreenActivity.this,
							"No  user found.");
					// finish();
				}

				// String id = jsonObject.getString("Telefon").toString();

			} catch (Exception e) {
				HttpUtility.toastMessage(WelcomeScreenActivity.this,
						"Exception: No device/user found");
				e.printStackTrace();
			}

			// text.setText(responseBody);

		}
	}

	private class eventsAsyncTask extends AsyncTask<Void, Void, Void> {
		String modalMesaj;
		ProgressDialog dialog;

		public eventsAsyncTask(String mMesaj) {
			this.modalMesaj = mMesaj;
			this.dialog = new ProgressDialog(WelcomeScreenActivity.this);
		}

		@Override
		protected void onPreExecute() {

			
			dialog.setMessage(modalMesaj);
			dialog.setIndeterminate(true);
			dialog.setCancelable(false);
			dialog.show();

		}

		@Override
		protected Void doInBackground(Void... params) {

			jsonArrayE = HttpUtility.createGetListSingle(
					HttpUtility.GET_RECOMM_EVENTS_URL, String.valueOf(HttpUtility.passedUser));
			return null;
		}

		@Override
		protected void onPostExecute(Void str) {

			if (dialog.isShowing())
				dialog.dismiss();
			try {

				if (jsonArrayE != null && jsonArrayE.length() > 0) {
					HttpUtility.passedUserEvents = jsonArrayE;
					HttpUtility.startIntent(WelcomeScreenActivity.this,
							EventsActivity.class);

				} else {
					HttpUtility.toastMessage(WelcomeScreenActivity.this,
							"No  event found.");
					// finish();
				}

				// String id = jsonObject.getString("Telefon").toString();

			} catch (Exception e) {
				HttpUtility.toastMessage(WelcomeScreenActivity.this,
						"Exception: No place found");
				e.printStackTrace();
			}

			// text.setText(responseBody);

		}
	}

	private class registeredEventsAsyncTask extends AsyncTask<Void, Void, Void> {
		String modalMesaj;
		ProgressDialog dialog;
		List<NameValuePair> pairs = new ArrayList<NameValuePair>(1);

		public registeredEventsAsyncTask(String mMesaj) {
			this.modalMesaj = mMesaj;
			this.dialog = new ProgressDialog(WelcomeScreenActivity.this);
		}

		@Override
		protected void onPreExecute() {

			pairs.add(new BasicNameValuePair("Id", String
					.valueOf(HttpUtility.passedUser)));
			dialog.setMessage(modalMesaj);
			dialog.setIndeterminate(true);
			dialog.setCancelable(false);
			dialog.show();

		}

		@Override
		protected Void doInBackground(Void... params) {

			jsonArrayE = HttpUtility.createGetList(
					HttpUtility.GET_USER_REGISTERED_EVENTS_URL, pairs);
			return null;
		}

		@Override
		protected void onPostExecute(Void str) {

			if (dialog.isShowing())
				dialog.dismiss();
			try {

				if (jsonArrayE != null && jsonArrayE.length() > 0) {
					HttpUtility.passedUserEvents = jsonArrayE;

				} else {
					HttpUtility.toastMessage(WelcomeScreenActivity.this,
							"No user event found.");
					// finish();
				}

				// String id = jsonObject.getString("Telefon").toString();

			} catch (Exception e) {
				HttpUtility.toastMessage(WelcomeScreenActivity.this,
						"Exception: No event found");
				e.printStackTrace();
			}

			// text.setText(responseBody);

		}
	}

	private class bringRegistrationsAsyncTask extends
			AsyncTask<Void, Void, Void> {
		String modalMesaj;
		ProgressDialog dialog;
		List<NameValuePair> pairs = new ArrayList<NameValuePair>(1);

		public bringRegistrationsAsyncTask(String mMesaj) {
			this.modalMesaj = mMesaj;
			this.dialog = new ProgressDialog(WelcomeScreenActivity.this);
		}

		@Override
		protected void onPreExecute() {

			pairs.add(new BasicNameValuePair("Id", String
					.valueOf(HttpUtility.passedUser)));
			dialog.setMessage(modalMesaj);
			dialog.setIndeterminate(true);
			dialog.setCancelable(false);
			dialog.show();

		}

		@Override
		protected Void doInBackground(Void... params) {

			jsonArrayRegs = HttpUtility.createGetList(
					HttpUtility.GET_USER_REGISTERED_EVENTS_URL, pairs);
			return null;
		}

		@Override
		protected void onPostExecute(Void str) {

			if (dialog.isShowing())
				dialog.dismiss();
			try {

				if (jsonArrayRegs != null && jsonArrayRegs.length() > 0) {
					HttpUtility.passedUserRegistrations = jsonArrayRegs;
					HttpUtility.startIntent(WelcomeScreenActivity.this,
							RegistrationsListActivity.class);

				} else {
					HttpUtility.toastMessage(WelcomeScreenActivity.this,
							"No user registrations found.");
					// finish();
				}

				// String id = jsonObject.getString("Telefon").toString();

			} catch (Exception e) {
				HttpUtility.toastMessage(WelcomeScreenActivity.this,
						"Exception: No registration found");
				e.printStackTrace();
			}

			// text.setText(responseBody);

		}
	}

	private class registerDeviceAsyncTask extends AsyncTask<Void, Void, Void> {
		String modalMesaj;
		ProgressDialog dialog;
		JSONObject jsonObjectInner = new JSONObject();
        public registerDeviceAsyncTask(String mMesaj) {
            this.modalMesaj = mMesaj;
            this.dialog = new ProgressDialog(WelcomeScreenActivity.this);
        }

        @Override
        protected void onPreExecute() {
        	
        	try{
        		 jsonObjectInner.put("UserId", String.valueOf(HttpUtility.passedUser));
        		 jsonObjectInner.put("DeviceId",regid);
        	}catch (JSONException e){
        		
        	}
        	dialog.setMessage(modalMesaj);
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            dialog.show();
        	
        }
  
		@Override
		protected Void doInBackground(Void... params) {
			
			jsonObjectInner = HttpUtility.createPostRequest(HttpUtility.POST_REGISTER_DEVICE_URL, jsonObjectInner);
			return null;
		}
		 @SuppressLint("NewApi")
		@Override
	        protected void onPostExecute(Void str) {
	 
	            if (dialog.isShowing())
	                dialog.dismiss();
	            try {
		           	 
	            	if(jsonObjectInner !=null && jsonObjectInner.getBoolean("RegisteredDevice")!=false){
	            		HttpUtility.toastMessage(WelcomeScreenActivity.this, "Device Id sent");
		            	return;
	            	}
	            	else{
	            		HttpUtility.toastMessage(WelcomeScreenActivity.this, "Problem in Device Id sending");
	            	}
	            	
	            } catch (JSONException e) {
	            	HttpUtility.toastMessage(WelcomeScreenActivity.this, "Exception:Problem occured in sending device id");
	                e.printStackTrace();
	            }

	        }
	    }
	private class pushAsyncTask extends AsyncTask<Void, Void, Void> {
		String modalMesaj;
		ProgressDialog dialog;
		JSONObject jsonObjectInner = new JSONObject();
        public pushAsyncTask(String mMesaj) {
            this.modalMesaj = mMesaj;
            this.dialog = new ProgressDialog(WelcomeScreenActivity.this);
        }

        @Override
        protected void onPreExecute() {
        	
        	try{
        		 jsonObjectInner.put("UserId", String.valueOf(HttpUtility.passedUser));
        		 jsonObjectInner.put("Title","You received a push!");
        	}catch (JSONException e){
        		
        	}
        	dialog.setMessage(modalMesaj);
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            dialog.show();
        	
        }
  
		@Override
		protected Void doInBackground(Void... params) {
			
			jsonObjectInner = HttpUtility.createPostRequest(HttpUtility.POST_SEND_PUSH_URL, jsonObjectInner);
			return null;
		}
		 @SuppressLint("NewApi")
		@Override
	        protected void onPostExecute(Void str) {
	 
	            if (dialog.isShowing())
	                dialog.dismiss();
	            try {
		           	 
	            	if(jsonObjectInner !=null){
	            		HttpUtility.toastMessage(WelcomeScreenActivity.this, "Push request sent");
		            	return;
	            	}
	            	else{
	            		HttpUtility.toastMessage(WelcomeScreenActivity.this, "Problem in push request sending");
	            	}
	            	
	            } catch (Exception e) {
	            	HttpUtility.toastMessage(WelcomeScreenActivity.this, "Exception:Problem occured in sending push request");
	                e.printStackTrace();
	            }

	        }
	    }


}
