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

	private Button bPlaces, bInterests, bEvents, bLocations, bMessages,
			bRegistrations;
	private JSONArray jsonArrayU,jsonArrayA, jsonArrayP,jsonArrayE, jsonArrayRegs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome_screen);
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
				
					new userInterestsAsyncTask(HttpUtility.WaitMessage).execute();;

			
			}
		});
		bEvents = (Button) findViewById(R.id.wEventsButton);
		bEvents.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				new eventsAsyncTask(HttpUtility.WaitMessage).execute();;
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
		//new registeredEventsAsyncTask(HttpUtility.WaitMessage).execute();;

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.welcome_screen, menu);
		return true;
	}

	protected void bringRegistrations() {
		new bringRegistrationsAsyncTask(HttpUtility.WaitMessage).execute();;
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
					HttpUtility.toastMessage(WelcomeScreenActivity.this, "Tags received...");
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

}
