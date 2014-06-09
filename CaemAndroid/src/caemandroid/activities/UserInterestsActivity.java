package caemandroid.activities;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import caemandroid.activities.EventsActivity.EventListAdapter;
import caemandroid.activities.PlacesActivity.PlaceListAdapter.PlaceViewHolder;
import caemandroid.entity.EventsListObject;
import caemandroid.entity.InterestsListObject;
import caemandroid.entity.PlacesListObject;
import caemandroid.http.HttpUtility;

import com.example.caemandroid.R;
import com.example.caemandroid.R.layout;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class UserInterestsActivity extends Activity {

	private JSONArray interests;
	Activity myContext;


	private ListView view1;
	private JSONObject json3;
	private ArrayList<InterestsListObject> interestList;
	private ArrayList<InterestsListObject> userInterestList;
	private Button saveButton, createButton;
    private JSONArray jsonArray ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_interests);
		
		view1 = (ListView) findViewById(R.id.inListView1);
		arrangeInterests();
		
		
		InterestListAdapter interestListAdapter = new InterestListAdapter();
	     view1.setAdapter(interestListAdapter);
	     saveButton = (Button) findViewById(R.id.intSaveButton);
	     saveButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new saveInterestsAsyncTask(HttpUtility.WaitMessage);
				
			}
		});
	     
	     createButton = (Button) findViewById(R.id.intCreateButton);
	     createButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				
			}
		});
	     
	}
	private void arrangeInterests(){
		JSONArray cast = HttpUtility.passedTags;
		JSONArray userCast = HttpUtility.passedUserTags;
		if(cast ==null || cast.length()<1 ){
			HttpUtility.toastMessage(UserInterestsActivity.this, "No interests found.");
		}
		else{
			interestList = new ArrayList<InterestsListObject>();
			userInterestList = new ArrayList<InterestsListObject>();
			
			try {
				
				
				for (int i=0; i<cast.length(); i++) {
				    JSONObject pla = cast.getJSONObject(i);
				    InterestsListObject h = new InterestsListObject(pla.getString("Id"), pla.getString("Name"));
				    interestList.add(h);

				    //hotelArray[i] =h;
				}
				for (int i=0; i<userCast.length(); i++) {
				    JSONObject pla = userCast.getJSONObject(i);
				    InterestsListObject h = new InterestsListObject(pla.getString("Id"), pla.getString("Name"));
				    userInterestList.add(h);				    
				}
				for (InterestsListObject inl : userInterestList) {
					for (InterestsListObject all : interestList) {
						if(all.getId().equals(inl)){
							all.setSelected(true);
							break;
						}
					}
				}
				
			}catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		
	}
	
	private class saveInterestsAsyncTask extends AsyncTask<Void, Void, Void> {
		String modalMesaj;
		ProgressDialog dialog;
		List<NameValuePair> pairs = new ArrayList<NameValuePair>(1);
		JSONObject jo = new JSONObject();
		public saveInterestsAsyncTask(String mMesaj) {
			this.modalMesaj = mMesaj;
			this.dialog = new ProgressDialog(UserInterestsActivity.this);
		}

		@Override
		protected void onPreExecute() {
			try {
				jo.put("userTagList", userInterestList);
				jo.put("id", HttpUtility.passedUser);
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

			jsonArray = HttpUtility.createPostList(HttpUtility.POST_UPDATE_USER_TAGS_URL, jo);
			return null;
		}

		@Override
		protected void onPostExecute(Void str) {

			if (dialog.isShowing())
				dialog.dismiss();
			try {

				if (jsonArray != null && jsonArray.length() > 0) {

				HttpUtility.toastMessage(UserInterestsActivity.this, "Interests saved.");

				} else {
					HttpUtility.toastMessage(UserInterestsActivity.this,
							"Interest could not be save found. "+jsonArray.toString());
					// finish();
				}

				// String id = jsonObject.getString("Telefon").toString();

			} catch (Exception e) {
				HttpUtility.toastMessage(UserInterestsActivity.this,
						"Exception: Interests exception");
				e.printStackTrace();
			}

			// text.setText(responseBody);

		}
	}
	
	class InterestListAdapter extends BaseAdapter {
		 
	    @Override
	    public int getCount() {
	        return interestList.size();
	    }
	 
	    @Override
	    public Object getItem(int position) {
	        return null;
	    }
	 
	    @Override
	    public long getItemId(int position) {
	        return 0;
	    }

		class InterestViewHolder {
		    TextView name;
		}
	    @Override
	    public View getView(final int position, View convertView,
	            ViewGroup parent) {
	 
	        // view holder kurulumu
	        final InterestViewHolder interestViewHolder;
	 
	        if (convertView == null) {
	 
	            // satýr görseli oluþturma
	            convertView = myContext.getLayoutInflater().inflate(
	                    R.layout.list_interest_layout, parent, false);
	 
	            // view holderý sýfýrdan oluþturma
	            interestViewHolder = new InterestViewHolder();
	            interestViewHolder.name = (TextView) convertView
	                    .findViewById(R.id.ly_interest_name);
	        
	 
	            convertView.setTag(interestViewHolder);
	        } else {
	 
	            // view holderý zaten daha önceden oluþturduðumuz
	            // view holdera eþitleme
	        	interestViewHolder = (InterestViewHolder) convertView.getTag();
	        }
	 
	        // görsel elemanlarýn deðerlerini verme
	        interestViewHolder.name.setText(interestList.get(position).getName());
	        if (interestList.get(position).isSelected())
            	interestViewHolder.name.setTextColor(Color.RED);

	 
	        // meyvenin durumuna göre isminin rengini deðiþtirme
	        /*if (hotelList.get(position).isSelected)
	            fruitViewHolder.name.setTextColor(Color.RED);
	        else
	            fruitViewHolder.name.setTextColor(Color.BLACK);*/
	 
	        convertView.setOnClickListener(new View.OnClickListener() {
	 
	            @Override
	            public void onClick(View v) {
	            	String id = interestList.get(position).getId();
	            	//HttpUtility.createIntent(UserInterestsActivity.this, PlaceDetailActivity.class, "Id",id);
	            	interestList.get(position).setSelected(!interestList
	                        .get(position).isSelected());
	                if (interestList.get(position).isSelected())
	                	interestViewHolder.name.setTextColor(Color.RED);
	                else
	                	interestViewHolder.name.setTextColor(Color.BLACK);
	            	
	            }
	        });
	 
	        return convertView;
	    }
	}
	
	
}
