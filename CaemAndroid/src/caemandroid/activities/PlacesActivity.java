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
import android.support.v4.widget.ListViewAutoScrollHelper;
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
		myContext = PlacesActivity.this;
		//final ListView listView1 = (ListView) findViewById(R.id.listView1);
		view1 =  (ListView) findViewById(R.id.pListView1);
		arrangePlaces();
		
		
		PlaceListAdapter placeListAdapter = new PlaceListAdapter();
	     view1.setAdapter(placeListAdapter);
	    // adapteri listview a ba�lama
	}

	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.places, menu);
		return true;
	}
	private void arrangePlaces(){
		JSONArray cast = HttpUtility.passedPlaces;
		if(cast == null || cast.length()<1){
			HttpUtility.toastMessage(PlacesActivity.this, "No places.");
		}
		else{
			placesList = new ArrayList<PlacesListObject>();

			
			try {
				
				for (int i=0; i<cast.length(); i++) {
				    JSONObject pla = cast.getJSONObject(i);
				    PlacesListObject h = new PlacesListObject(pla.getString("Id"), pla.getString("Name"), pla.getString("Address"), R.drawable.ic_launcher);
				    placesList.add(h);
				    //hotelArray[i] =h;
				    
				}
			}catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
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
	 
	            // sat�r g�rseli olu�turma
	            convertView = myContext.getLayoutInflater().inflate(
	                    R.layout.list_place_layout, parent, false);
	 
	            // view holder� s�f�rdan olu�turma
	            placeViewHolder = new PlaceViewHolder();
	            placeViewHolder.name = (TextView) convertView
	                    .findViewById(R.id.lyPlace_name);
	            placeViewHolder.category = (TextView) convertView
	                    .findViewById(R.id.lyPlace_category);
	            placeViewHolder.img = (ImageView) convertView
	                    .findViewById(R.id.lyPlace_image);
	 
	            convertView.setTag(placeViewHolder);
	        } else {
	 
	            // view holder� zaten daha �nceden olu�turdu�umuz
	            // view holdera e�itleme
	        	placeViewHolder = (PlaceViewHolder) convertView.getTag();
	        }
	 
	        // g�rsel elemanlar�n de�erlerini verme
	        placeViewHolder.name.setText(placesList.get(position).getName());
	        placeViewHolder.category.setText(placesList.get(position).getCategory());
	        placeViewHolder.img.setImageDrawable(myContext.getResources()
	                .getDrawable(placesList.get(position).getImgId()));
	 
	        // meyvenin durumuna g�re isminin rengini de�i�tirme
	        /*if (hotelList.get(position).isSelected)
	            fruitViewHolder.name.setTextColor(Color.RED);
	        else
	            fruitViewHolder.name.setTextColor(Color.BLACK);*/
	 
	        convertView.setOnClickListener(new View.OnClickListener() {
	 
	            @Override
	            public void onClick(View v) {
	            	String id = placesList.get(position).getId();
	            	HttpUtility.startIntent(PlacesActivity.this, PlaceDetailActivity.class, "Id",id);
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
