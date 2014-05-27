package caemandroid.activities;

import org.json.JSONObject;

import caemandroid.http.HttpUtility;

import com.example.caemandroid.R;
import com.example.caemandroid.R.layout;
import com.example.caemandroid.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class WelcomeScreenActivity extends Activity {

	private Button bPlaces, bInterests, bEvents, bLocations, bMessages;
	private JSONObject jsonObject = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome_screen);
		findComponents();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.welcome_screen, menu);
		return true;
	}

	private void findComponents(){
		bPlaces = (Button) findViewById(R.id.wPlacesButton);
		bPlaces.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {

	            }
	        });
		
		bInterests = (Button) findViewById(R.id.wInterestsButton);
		bInterests.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {

	            }
	        });
		bEvents = (Button) findViewById(R.id.wEventsButton);
		bEvents.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {

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
	}

}
