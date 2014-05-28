package caemandroid.activities;

import org.json.JSONObject;

import com.example.caemandroid.R;
import com.example.caemandroid.R.layout;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class CreateEventActivity extends Activity {


	private Button clear, create;
	private EditText title, startTime, startDate, finishTime, finishDate, desc;
	
	private String strTitle, strStartTime, strStartDate, strFinishTime, strFinishDate, strDesc;
	private JSONObject jsonObject = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_event);
		findComponents();
		
		
	}
	private void createEvent (){
		
	}
	private void findComponents(){
		title = (EditText) findViewById(R.id.ceTitleEdit);
		startTime = (EditText) findViewById(R.id.ceStartTimeDateEdit);
		startDate = (EditText) findViewById(R.id.ceStartDateEdit);
		finishTime = (EditText) findViewById(R.id.ceFinishTimeEdit);
		finishDate = (EditText) findViewById(R.id.ceFinishDateEdit);
		desc = (EditText) findViewById(R.id.ceDescriptionEdit);
		clear = (Button) findViewById(R.id.ceClearButton);
		clear.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				title.setText("");
				startTime.setText("");
				startDate.setText("");
				finishTime.setText("");
				finishDate.setText("");
				desc.setText("");
			}
		});
		
		create = (Button) findViewById(R.id.ceCreateButton);
		create.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {

				
			}
		});

		
	
	}
}
