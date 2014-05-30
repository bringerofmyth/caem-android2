package com.example.caemandroid;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import caemandroid.activities.PlaceDetailActivity;
import caemandroid.activities.PlacesActivity;
import caemandroid.entity.PlacesListObject;
import caemandroid.entity.RegistrationsListObject;
import caemandroid.http.HttpUtility;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class RegistrationsListActivity extends Activity {

	private JSONArray registrations;
	Activity myContext;
	private ListView view1;
	private JSONObject json2;
	private ArrayList<RegistrationsListObject> registrationsList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registrations_list);
		
		view1 =  (ListView) findViewById(R.id.rlListView1);
		arrangePlaces();
		

		RegistrationsListAdapter registrationsListAdapter = new RegistrationsListAdapter();
		view1.setAdapter(registrationsListAdapter);
	}
	
	private void arrangePlaces(){
		
		registrationsList = new ArrayList<RegistrationsListObject>();

		JSONArray cast = HttpUtility.passedUserRegistrations;
		if(cast ==null || cast.isNull(0)){
			HttpUtility.toastMessage(RegistrationsListActivity.this, "No registrations found!");
			finish();
		}
		else{
			try {
				
				for (int i=0; i<cast.length(); i++) {
				    JSONObject pla = cast.getJSONObject(i);
				    RegistrationsListObject h = new RegistrationsListObject(pla.getString("Id"), pla.getString("Name"), pla.getString("Date"), R.drawable.ic_launcher);
				    registrationsList.add(h);
				    //hotelArray[i] =h;
				    
				}
			}catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		
		
	}
	
	class RegistrationsListAdapter extends BaseAdapter {
		 
	    @Override
	    public int getCount() {
	        return registrationsList.size();
	    }
	 
	    @Override
	    public Object getItem(int position) {
	        return null;
	    }
	 
	    @Override
	    public long getItemId(int position) {
	        return 0;
	    }

		class RegistrationViewHolder {
		    TextView name;
		    TextView startDate;
		    ImageView img;
		}
	    @Override
	    public View getView(final int position, View convertView,
	            ViewGroup parent) {
	 
	        // view holder kurulumu
	        final RegistrationViewHolder registrationViewHolder;
	 
	        if (convertView == null) {
	 
	            // satýr görseli oluþturma
	            convertView = myContext.getLayoutInflater().inflate(
	                    R.layout.list_registrations_layout, parent, false);
	 
	            // view holderý sýfýrdan oluþturma
	            registrationViewHolder = new RegistrationViewHolder();
	            registrationViewHolder.name = (TextView) convertView
	                    .findViewById(R.id.lyRegistration_name);
	            registrationViewHolder.startDate = (TextView) convertView
	                    .findViewById(R.id.lyRegistration_date);
	            registrationViewHolder.img = (ImageView) convertView
	                    .findViewById(R.id.lyRegistration_image);
	 
	            convertView.setTag(registrationViewHolder);
	        } else {
	 
	            // view holderý zaten daha önceden oluþturduðumuz
	            // view holdera eþitleme
	        	registrationViewHolder = (RegistrationViewHolder) convertView.getTag();
	        }
	 
	        // görsel elemanlarýn deðerlerini verme
	        registrationViewHolder.name.setText(registrationsList.get(position).getName());
	        registrationViewHolder.startDate.setText(registrationsList.get(position).getDate());
	        registrationViewHolder.img.setImageDrawable(myContext.getResources()
	                .getDrawable(registrationsList.get(position).getImgId()));
	 
	        // meyvenin durumuna göre isminin rengini deðiþtirme
	        /*if (hotelList.get(position).isSelected)
	            fruitViewHolder.name.setTextColor(Color.RED);
	        else
	            fruitViewHolder.name.setTextColor(Color.BLACK);*/
	 
	        convertView.setOnClickListener(new View.OnClickListener() {
	 
	            @Override
	            public void onClick(View v) {
	            	String id = registrationsList.get(position).getId();
	            	//HttpUtility.createIntent(RegistrationsListActivity.this, PlaceDetailActivity.class, "Id",id);
	            	/*placesList.get(position).setSelected(!placesList
	                        .get(position).isSelected());
	                if (placesList.get(position).isSelected())
	                	placeViewHolder.name.setTextColor(Color.RED);
	                else
	                	placeViewHolder.name.setTextColor(Color.BLACK);*/
	            	
	            }
	        });
	 
	        return convertView;
	    }
	}
}
