package caemandroid.activities;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import caemandroid.activities.PlacesActivity.PlaceListAdapter;
import caemandroid.entity.EventsListObject;
import caemandroid.http.HttpUtility;

import com.example.caemandroid.R;
import com.example.caemandroid.R.layout;
import com.example.caemandroid.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class EventsActivity extends Activity {

	private JSONArray events;
	Activity myContext;
	private EventsListObject[] eventArray;

	private ListView view1;
	private JSONObject json2;
	private ArrayList<EventsListObject> eventsList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_events);
		myContext = EventsActivity.this;
		view1 = (ListView) findViewById(R.id.eListView1);
		arrangeEvents();
		
		
		EventListAdapter eventListAdapter = new EventListAdapter();
	     view1.setAdapter(eventListAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.events, menu);
		return true;
	}
	private void arrangeEvents(){
		JSONArray cast = HttpUtility.passedUserEvents;
		if(cast ==null || cast.length()<1 ){
			HttpUtility.toastMessage(EventsActivity.this, "No event found.");
		}
		eventsList = new ArrayList<EventsListObject>();

		try {
			
			for (int i=0; i<cast.length(); i++) {
			    JSONObject pla = cast.getJSONObject(i);
			    String eventType= "Outdoor";
			    if(pla.getInt("EventType")==0){
			    	eventType = "Indoor";
			    }
			    EventsListObject h = new EventsListObject(pla.getString("Id"), pla.getString("Title"), eventType, R.drawable.ic_launcher);
			    eventsList.add(h);
			    //hotelArray[i] =h;
			    
			}
		}catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	class EventListAdapter extends BaseAdapter {
		 
	    @Override
	    public int getCount() {
	        return eventsList.size();
	    }
	 
	    @Override
	    public Object getItem(int position) {
	        return null;
	    }
	 
	    @Override
	    public long getItemId(int position) {
	        return 0;
	    }

		class EventViewHolder {
		    TextView name;
		    TextView category;
		    ImageView img;
		}
	    @Override
	    public View getView(final int position, View convertView,
	            ViewGroup parent) {
	 
	        // view holder kurulumu
	        final EventViewHolder eventViewHolder;
	 
	        if (convertView == null) {
	 
	            // sat�r g�rseli olu�turma
	            convertView = (EventsActivity.this).getLayoutInflater().inflate(
	                    R.layout.list_event_layout, parent, false);
	 
	            // view holder� s�f�rdan olu�turma
	            eventViewHolder = new EventViewHolder();
	            eventViewHolder.name = (TextView) convertView
	                    .findViewById(R.id.event_name);
	            eventViewHolder.category = (TextView) convertView
	                    .findViewById(R.id.event_category);
	            eventViewHolder.img = (ImageView) convertView
	                    .findViewById(R.id.event_image);
	 
	            convertView.setTag(eventViewHolder);
	        } else {
	 
	            // view holder� zaten daha �nceden olu�turdu�umuz
	            // view holdera e�itleme
	        	eventViewHolder = (EventViewHolder) convertView.getTag();
	        }
	 
	        // g�rsel elemanlar�n de�erlerini verme
	        eventViewHolder.name.setText(eventsList.get(position).getName());
	        eventViewHolder.category.setText(eventsList.get(position).getCategory());
	        eventViewHolder.img.setImageDrawable(myContext.getResources()
	                .getDrawable(eventsList.get(position).getImgId()));
	 
	        // meyvenin durumuna g�re isminin rengini de�i�tirme
	        /*if (hotelList.get(position).isSelected)
	            fruitViewHolder.name.setTextColor(Color.RED);
	        else
	            fruitViewHolder.name.setTextColor(Color.BLACK);*/
	 
	        convertView.setOnClickListener(new View.OnClickListener() {
	 
	            @Override
	            public void onClick(View v) {
	            	String id = eventsList.get(position).getId();
	            	HttpUtility.startIntent(EventsActivity.this, EventDetailActivity.class, "Id",id);
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
