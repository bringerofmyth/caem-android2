package caemandroid.activities;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import caemandroid.entity.InterestsListObject;
import caemandroid.http.HttpUtility;

import com.example.caemandroid.R;
import com.example.caemandroid.R.layout;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CreateInterestActivity extends Activity {

	private Button createBt;
	private EditText text ;
	String textS = null;
	JSONObject jsonObject = null;
	ArrayList<InterestsListObject> taglist = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_interest);
		text = (EditText) findViewById(R.id.ciEditText1);
		createBt = (Button) findViewById(R.id.ciCreateButton);
		createBt.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				textS = text.getText().toString();
				
				if(HttpUtility.isNullOrEmpty(textS))
				{
					HttpUtility.toastMessage(CreateInterestActivity.this, "Please write something");
					
				}
			
				else{
					new createInterestsAsyncTask (HttpUtility.WaitMessage).execute();
				}
				
			}
		});
	}
	
	private class createInterestsAsyncTask extends AsyncTask<Void, Void, Void> {
		String modalMesaj;
		ProgressDialog dialog;
		JSONObject jo = new JSONObject();
		public createInterestsAsyncTask(String mMesaj) {
			this.modalMesaj = mMesaj;
			this.dialog = new ProgressDialog(CreateInterestActivity.this);
		}

		@Override
		protected void onPreExecute() {
			try {
				jo.put("Title", textS);
				jsonObject = new JSONObject();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		
			dialog.setMessage(modalMesaj);
			dialog.setIndeterminate(true);
			dialog.setCancelable(false);
			dialog.show();

		}

		@Override
		protected Void doInBackground(Void... params) {

			jsonObject = HttpUtility.createPostRequest(HttpUtility.POST_CREATE_USER_TAG_URL, jo);
			return null;
		}

		@Override
		protected void onPostExecute(Void str) {

			if (dialog.isShowing())
				dialog.dismiss();
			try {

				if (jsonObject != null && jsonObject.getString("Id")!=null) {

				HttpUtility.toastMessage(CreateInterestActivity.this,""+jsonObject.getString("Title")+ " interest saved.");
				text.setText("");
				} else {
					HttpUtility.toastMessage(CreateInterestActivity.this,
							"Interest could not be save "+jsonObject.toString());
					// finish();
				}

				// String id = jsonObject.getString("Telefon").toString();

			} catch (Exception e) {
				HttpUtility.toastMessage(CreateInterestActivity.this,
						"Exception: Interests exception");
				e.printStackTrace();
			}

			// text.setText(responseBody);

		}
	}
}
