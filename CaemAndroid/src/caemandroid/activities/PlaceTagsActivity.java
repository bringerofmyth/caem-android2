package caemandroid.activities;

import java.util.ArrayList;

import org.json.JSONArray;


import caemandroid.entity.InterestsListObject;

import com.example.caemandroid.R;
import com.example.caemandroid.R.layout;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

public class PlaceTagsActivity extends Activity {
	//InterestCustomAdapter dataAdapter = null;
	private Button saveButton, createButton;
    private JSONArray jsonArray ;
    ArrayList<InterestsListObject> selectedList = null;
    ArrayList<InterestsListObject> interestList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_place_tags);
	}
}
