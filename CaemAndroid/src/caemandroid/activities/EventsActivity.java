package caemandroid.activities;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.events, menu);
		return true;
	}
	private void arrangeEvents(){
		eventsList = new ArrayList<EventsListObject>();

		JSONArray cast = events;
		try {
			
			for (int i=0; i<cast.length(); i++) {
			    JSONObject pla = cast.getJSONObject(i);
			    EventsListObject h = new EventsListObject(pla.getString("Id"), pla.getString("Name"), pla.getString("Category"), R.drawable.ic_launcher);
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
	 
	            // satýr görseli oluþturma
	            convertView = myContext.getLayoutInflater().inflate(
	                    R.layout.list_event_layout, parent, false);
	 
	            // view holderý sýfýrdan oluþturma
	            eventViewHolder = new EventViewHolder();
	            eventViewHolder.name = (TextView) convertView
	                    .findViewById(R.id.event_name);
	            eventViewHolder.category = (TextView) convertView
	                    .findViewById(R.id.event_category);
	            eventViewHolder.img = (ImageView) convertView
	                    .findViewById(R.id.event_image);
	 
	            convertView.setTag(eventViewHolder);
	        } else {
	 
	            // view holderý zaten daha önceden oluþturduðumuz
	            // view holdera eþitleme
	        	eventViewHolder = (EventViewHolder) convertView.getTag();
	        }
	 
	        // görsel elemanlarýn deðerlerini verme
	        eventViewHolder.name.setText(eventsList.get(position).getName());
	        eventViewHolder.category.setText(eventsList.get(position).getCategory());
	        eventViewHolder.img.setImageDrawable(myContext.getResources()
	                .getDrawable(eventsList.get(position).getImgId()));
	 
	        // meyvenin durumuna göre isminin rengini deðiþtirme
	        /*if (hotelList.get(position).isSelected)
	            fruitViewHolder.name.setTextColor(Color.RED);
	        else
	            fruitViewHolder.name.setTextColor(Color.BLACK);*/
	 
	        convertView.setOnClickListener(new View.OnClickListener() {
	 
	            @Override
	            public void onClick(View v) {
	            	String id = eventsList.get(position).getId();
	            	HttpUtility.createIntent(EventsActivity.this, EventDetailActivity.class, "Id",id);
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
