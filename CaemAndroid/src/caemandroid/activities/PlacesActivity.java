package caemandroid.activities;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import caemandroid.entity.PlacesListObject;
import caemandroid.http.HttpUtility;

import com.example.caemandroid.R;


import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class PlacesActivity extends Activity {

	private JSONArray places;
	Activity myContext;
	private PlacesListObject[] placeArray;

	private ListView view1;
	private JSONObject json2;
	private ArrayList<PlacesListObject> placesList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_places);
		//final ListView listView1 = (ListView) findViewById(R.id.listView1);
		
		arrangePlaces();
		
		
		PlaceListAdapter placeListAdapter = new PlaceListAdapter();
	     view1.setAdapter(placeListAdapter);
	    // adapteri listview a baðlama
	}

	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.places, menu);
		return true;
	}
	private void arrangePlaces(){
		placesList = new ArrayList<PlacesListObject>();

		JSONArray cast = places;
		try {
			
			for (int i=0; i<cast.length(); i++) {
			    JSONObject pla = cast.getJSONObject(i);
			    PlacesListObject h = new PlacesListObject(pla.getString("Id"), pla.getString("Name"), pla.getString("Category"), R.drawable.ic_launcher);
			    placesList.add(h);
			    //hotelArray[i] =h;
			    
			}
		}catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	class PlaceListAdapter extends BaseAdapter {
		 
	    @Override
	    public int getCount() {
	        return placesList.size();
	    }
	 
	    @Override
	    public Object getItem(int position) {
	        return null;
	    }
	 
	    @Override
	    public long getItemId(int position) {
	        return 0;
	    }

		class PlaceViewHolder {
		    TextView name;
		    TextView category;
		    ImageView img;
		}
	    @Override
	    public View getView(final int position, View convertView,
	            ViewGroup parent) {
	 
	        // view holder kurulumu
	        final PlaceViewHolder placeViewHolder;
	 
	        if (convertView == null) {
	 
	            // satýr görseli oluþturma
	            convertView = myContext.getLayoutInflater().inflate(
	                    R.layout.list_place_layout, parent, false);
	 
	            // view holderý sýfýrdan oluþturma
	            placeViewHolder = new PlaceViewHolder();
	            placeViewHolder.name = (TextView) convertView
	                    .findViewById(R.id.place_name);
	            placeViewHolder.category = (TextView) convertView
	                    .findViewById(R.id.place_category);
	            placeViewHolder.img = (ImageView) convertView
	                    .findViewById(R.id.place_image);
	 
	            convertView.setTag(placeViewHolder);
	        } else {
	 
	            // view holderý zaten daha önceden oluþturduðumuz
	            // view holdera eþitleme
	        	placeViewHolder = (PlaceViewHolder) convertView.getTag();
	        }
	 
	        // görsel elemanlarýn deðerlerini verme
	        placeViewHolder.name.setText(placesList.get(position).getName());
	        placeViewHolder.category.setText(placesList.get(position).getCategory());
	        placeViewHolder.img.setImageDrawable(myContext.getResources()
	                .getDrawable(placesList.get(position).getImgId()));
	 
	        // meyvenin durumuna göre isminin rengini deðiþtirme
	        /*if (hotelList.get(position).isSelected)
	            fruitViewHolder.name.setTextColor(Color.RED);
	        else
	            fruitViewHolder.name.setTextColor(Color.BLACK);*/
	 
	        convertView.setOnClickListener(new View.OnClickListener() {
	 
	            @Override
	            public void onClick(View v) {
	            	String id = placesList.get(position).getId();
	            	HttpUtility.createIntent(PlacesActivity.this, PlaceDetailActivity.class, "Id",id);
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
