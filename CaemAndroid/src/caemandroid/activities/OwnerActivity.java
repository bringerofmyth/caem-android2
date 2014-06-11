package caemandroid.activities;

import caemandroid.http.HttpUtility;

import com.example.caemandroid.R;
import com.example.caemandroid.R.id;
import com.example.caemandroid.R.layout;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class OwnerActivity extends Activity {

	private Button cEvent, cPlace, cTag;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_owner);
		
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
}
