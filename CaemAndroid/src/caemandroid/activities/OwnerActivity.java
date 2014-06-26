package caemandroid.activities;

import org.json.JSONArray;


import caemandroid.http.HttpUtility;

import com.example.caemandroid.R;
import com.example.caemandroid.R.id;
import com.example.caemandroid.R.layout;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class OwnerActivity extends Activity {

	private Button cEvent, cPlace, cTag;
	private JSONArray jsonArrayA;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_owner);
		new interestsAsyncTask(HttpUtility.WaitMessage).execute();
		
		cEvent = (Button) findViewById(R.id.oCreateEventButton);
		cEvent.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {
	            	HttpUtility.startIntent(OwnerActivity.this, CreateEventActivity.class);
	            }
	        });
		cTag = (Button) findViewById(R.id.oCreateCategoryButton);
		cTag.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {
	            	HttpUtility.startIntent(OwnerActivity.this, CreateInterestActivity.class);
	            }
	        });
	}
	private class interestsAsyncTask extends AsyncTask<Void, Void, Void> {
		String modalMesaj;
		ProgressDialog dialog;
		

		public interestsAsyncTask(String mMesaj) {
			this.modalMesaj = mMesaj;
			this.dialog = new ProgressDialog(OwnerActivity.this);
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
					HttpUtility.toastMessage(OwnerActivity.this, "Tags received...");
				}
				else{
					HttpUtility.toastMessage(OwnerActivity.this, "No tags found...");
				}
				HttpUtility.passedTags = jsonArrayA;


			} catch (Exception e) {
				HttpUtility.toastMessage(OwnerActivity.this,
						"Exception: No total interests found");
				e.printStackTrace();
			}

			// text.setText(responseBody);

		}
	}
}
