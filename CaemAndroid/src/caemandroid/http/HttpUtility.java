package caemandroid.http;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import caemandroid.entity.Event;
import caemandroid.entity.EventDTO;
import caemandroid.entity.InterestsListObject;
import caemandroid.entity.Place;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

@SuppressLint("NewApi")
public class HttpUtility {

	public static final String GET_RECOMM_EVENTS_URL = "http://caemwepapi.azurewebsites.net/api/events/recommend";
	public static final String POST_LOGIN_USER_URL = "http://caemwepapi.azurewebsites.net/api/users/login";
	public static final String POST_REGISTER_USER_URL = "http://caemwepapi.azurewebsites.net/api/users/register";
	public static final String POST_CREATE_EVENT_URL = "http://caemwepapi.azurewebsites.net/api/events/create";
	public static final String POST_CREATE_PLACE_URL = "http://caemwepapi.azurewebsites.net/api/places/create";
	public static final String GET_PLACE_URL = "http://caemwepapi.azurewebsites.net/api/places";
	public static final String GET_EVENT_URL = "http://caemwepapi.azurewebsites.net/api/events";
	public static final String GET_EVENT_INFO_URL = "http://caemwepapi.azurewebsites.net/api/events/info";
	public static final String GET_RECOMM_PLACES_URL = "http://caemwepapi.azurewebsites.net/api/places/recommend";
	public static final String POST_UPDATE_USER_TAGS_URL = "http://caemwepapi.azurewebsites.net/api/tags/updateuser";
	public static final String POST_CREATE_USER_TAG_URL = "http://caemwepapi.azurewebsites.net/api/tags/create";
	
	public static final String GET_TAGS_URL = "http://caemwepapi.azurewebsites.net/api/tags/list";
	public static final String GET_USER_TAGS_URL = "http://caemwepapi.azurewebsites.net/api/tags/getuserlist";
	public static final String POST_REGISTER_USERTOEVENT_URL = "http://caemwepapi.azurewebsites.net/api/registrations/register";
	public static final String GET_USER_REGISTERED_EVENTS_URL = "http://caemwepapi.azurewebsites.net/api/events/userregistrations";
	public static final String GET_USER_REGISTRATIONS_URL = "";
	public static final String PostRegisterUser ="";
	public static final String WaitMessage ="Please wait...";
	public static JSONObject passedJson = null;
	public static JSONObject passedRegisterInfoJson = null;
	public static Integer passedUser = null;
	public static JSONArray passedPlaces = null;
	public static JSONArray passedUserEvents = null;
	public static JSONArray passedUserTags = null;
	public static JSONArray passedUserRegistrations = null;
	public static String ReservationUrl = "";
	public static JSONArray passedTags  = null;


	public static JSONObject createPostRequest(String url,
			List<NameValuePair> pairs) {
		JSONObject jsonObject = null;
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		try {

			post.setEntity(new UrlEncodedFormEntity(pairs));
			HttpResponse response = client.execute(post);
			HttpEntity entity = response.getEntity();
			// responseBody = EntityUtils.toString(response.getEntity());
			if (entity != null) {
				InputStream instream = entity.getContent();
				String result = convertStreamToString(instream);

				jsonObject = new JSONObject(result);

				instream.close();

			}

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
		} catch (IOException e) {
			// TODO Auto-generated catch block
		} catch (JSONException e) {
			// Mesaj(e.getMessage());
		}
		return jsonObject;
	}



	private static String arrangeUrl(String url, List<NameValuePair> pairs) {

		if (pairs == null || pairs.isEmpty()) {
			return url;
		} else {
			String newUrl = "";
			int len = pairs.size();
			if (len == 1) {
				newUrl = pairs.get(0).getName() + "=" + pairs.get(0).getValue();
				return url + "?" + newUrl;
			} else {

				for (int i = 0; i < len - 1; i++) {
					newUrl = newUrl + pairs.get(i).getName() + "="
							+ pairs.get(i).getValue() + "&";
				}
				newUrl = newUrl + pairs.get(len - 1).getName() + "="
						+ pairs.get(len - 1).getValue();

				return url + "?" + newUrl;
			}
		}
	}
	private static String arrangeSingleUrl(String url, String par) {

		if (par == null || par.isEmpty()) {
			return url;
		} else {
				
				return url + "/" + par;
		
		}
	}


	public static JSONObject createGetRequest(String url,
			List<NameValuePair> pairs) {
		// Integer productID = Integer.valueOf(2);
		JSONObject json = null;
		HttpClient httpclient = new DefaultHttpClient();
		String nUrl = arrangeUrl(url, pairs);
		HttpGet httpget = new HttpGet(nUrl);
		HttpResponse response;
		try {
			response = httpclient.execute(httpget);
			
			HttpEntity entity = response.getEntity();

			if (entity != null) {
				InputStream instream = entity.getContent();
				String result = convertStreamToString(instream);

				json = new JSONObject(result);

				instream.close();
			}

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json;
	}

	public static JSONObject createGetRequestSingle(String url,String singp) {
		// Integer productID = Integer.valueOf(2);
		JSONObject json = null;
		HttpClient httpclient = new DefaultHttpClient();
		String nUrl = arrangeSingleUrl(url, singp);
		HttpGet httpget = new HttpGet(nUrl);
		HttpResponse response;
		try {
			response = httpclient.execute(httpget);
			
			HttpEntity entity = response.getEntity();

			if (entity != null) {
				InputStream instream = entity.getContent();
				String result = convertStreamToString(instream);

				json = new JSONObject(result);

				instream.close();
			}

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json;
	}
	
	public static JSONArray createGetList(String url,
			List<NameValuePair> pairs) {
		// Integer productID = Integer.valueOf(2);
		JSONArray jarray = null;
		StringBuilder builder = new StringBuilder();
		HttpClient httpclient = new DefaultHttpClient();
		String nUrl = arrangeUrl(url, pairs);
		HttpGet httpget = new HttpGet(nUrl);
		HttpResponse response;
		try {
			response = httpclient.execute(httpget);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
			HttpEntity entity = response.getEntity();
			InputStream content = entity.getContent();
		
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(content));
			String line;
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}
		} else {
			Log.e("==>", "Failed to download file");
			return null;
		}
	} catch (ClientProtocolException e) {
		e.printStackTrace();
		return null;
	} catch (IOException e) {
		e.printStackTrace();
		return null;
	}

	// Parse String to JSON object
	try {
		
		jarray = new JSONArray(builder.toString());
		
	} catch (JSONException e) {
		Log.e("JSON Parser", "Error parsing data " + e.toString());
		return null;
	}

	// return JSON Object
	return jarray;
	}
	public static JSONArray createGetListSingle(String url,String singleParam) {
		// Integer productID = Integer.valueOf(2);
		JSONArray jarray = null;
		StringBuilder builder = new StringBuilder();
		HttpClient httpclient = new DefaultHttpClient();
		String nUrl = arrangeSingleUrl(url, singleParam);
		HttpGet httpget = new HttpGet(nUrl);
		HttpResponse response;
		try {
			response = httpclient.execute(httpget);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
			HttpEntity entity = response.getEntity();
			InputStream content = entity.getContent();
		
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(content));
			String line;
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}
		} else {
			Log.e("==>", "Failed to download file");
			return null;
		}
	} catch (ClientProtocolException e) {
		e.printStackTrace();
		return null;
	} catch (IOException e) {
		e.printStackTrace();
		return null;
	}

	// Parse String to JSON object
	try {
		
		jarray = new JSONArray(builder.toString());
		
	} catch (JSONException e) {
		Log.e("JSON Parser", "Error parsing data " + e.toString());
		return null;
	}

	// return JSON Object
	return jarray;
	}
	
	public static Place parsePlace (JSONObject jPlace){
		try {
			if(jPlace == null || isNullOrEmpty(jPlace.getString("Id"))){
				return null;
			}
			else{
				Place newPlace = new Place();
				newPlace.setName(jPlace.getString("Name"));
				newPlace.setAddress(jPlace.getString("Address"));
				newPlace.setDescription(jPlace.getString("Description"));
				newPlace.setOpenHours(jPlace.getString("OpenHours"));
				newPlace.setPhone(jPlace.getString("Phone"));
				return newPlace;

				
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static Event parseEvent (JSONObject jEvent){
		try {
			if(jEvent == null || isNullOrEmpty(jEvent.getString("Id"))){
				return null;
			}
			else{
				Event event = new Event();
				event.setTitle(jEvent.getString("Title"));
				event.setStartTime(jEvent.getString("StartTime"));
				event.setFinishTime(jEvent.getString("FinishTime"));
				event.setEventType(jEvent.getInt("EventType"));
				event.setIsRecurrent(jEvent.getBoolean("IsRecurrent"));
				event.setRecurrentUntil(jEvent.getString("RecurrentUntil"));
				event.setId(jEvent.getInt("Id"));
				
				return event;

				
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();		return null;
		}

	}
	public static EventDTO parseEventDTO (JSONObject jEvent){
		try {
			if(jEvent == null || isNullOrEmpty(jEvent.getString("Id"))){
				return null;
			}
			else{
				EventDTO event = new EventDTO();
				event.setTitle(jEvent.getString("Title"));
				event.setStartTime(jEvent.getString("StartTime"));
				event.setFinishTime(jEvent.getString("FinishTime"));
				event.setEventType(jEvent.getString("EventType"));
				event.setRuleFireStatus(jEvent.getString("RuleFireStatus"));
				event.setMessage(jEvent.getString("Message"));
				event.setApproval(jEvent.getString("Approval"));
				event.setId(jEvent.getInt("Id"));
				event.setWeatherStatus(jEvent.getString("WeatherStatus"));
				return event;

				
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();		return null;
		}

	}
	private static String convertStreamToString(InputStream is) {

		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	public static void toastMessage(Context ctx, String msg) {
		Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();
	}
	public static void toastMessageShort(Context ctx, String msg) {
		Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
	}


	public static boolean isNullOrEmpty(String s) {
		if (s == null || s.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isNullOrEmpty(String s1, String s2, String s3,
			String s4) {
		if (s1 == null || s1.isEmpty() || s2 == null || s2.isEmpty()
				|| s3 == null || s3.isEmpty() || s4 == null || s4.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isNullOrEmpty(String s1, String s2, String s3) {
		if (s1 == null || s1.isEmpty() || s2 == null || s2.isEmpty()
				|| s3 == null || s3.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isNullOrEmpty(String s1, String s2) {
		if (s1 == null || s1.isEmpty() || s2 == null || s2.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}

	public static void startIntent(Context ctx, Class<?> cls) {
		Intent intent = new Intent(ctx, cls);
		ctx.startActivity(intent);
	}

	public static void startIntent(Context ctx, Class<?> cls, String putName,
			String putValue) {
		Intent intent = new Intent(ctx, cls);
		intent.putExtra(putName, putValue);
		ctx.startActivity(intent);
	}

	public static void startIntent(Context ctx, Class<?> cls, String putName,
			Serializable putValue) {
		Intent intent = new Intent(ctx, cls);
		intent.putExtra(putName, putValue);
		ctx.startActivity(intent);
	}
	public static JSONObject createPostRequest(String url, JSONObject entity) {
		
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		String result = "";
		JSONObject js = null;
		try {
			//HttpClient httpclient = new DefaultHttpClient();
			//HttpPost httppost = new HttpPost(urls[0]);
			StringEntity params = new StringEntity(entity.toString());

			params.setContentType("application/json;charset=UTF-8");
			params.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
					"application/json;charset=UTF-8"));
			post.setHeader("Accept", "application/json");

			post.setEntity(params);
			HttpResponse response = client.execute(post);
			result = EntityUtils.toString(response.getEntity());
				js = new JSONObject(result);
			//Sonuc = EntityUtils.toString(response.getEntity());
			

			// responseBody = EntityUtils.toString(response.getEntity());


		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
		} catch (IOException e) {
			// TODO Auto-generated catch block
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return js;
	}
	public static JSONArray createPostList(String url, List<NameValuePair> pairs) {
		JSONArray jarray = null;
		StringBuilder builder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		try {
			post.setEntity(new UrlEncodedFormEntity(pairs));
			HttpResponse response = client.execute(post);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(content));
				String line;
				while ((line = reader.readLine()) != null) {
					builder.append(line);
				}
			} else {
				Log.e("==>", "Failed to download file");
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Parse String to JSON object
		try {
			jarray = new JSONArray(builder.toString());
			
		} catch (JSONException e) {
			Log.e("JSON Parser", "Error parsing data " + e.toString());
		}

		// return JSON Object
		return jarray;

	}
public static JSONArray createPostList(String url, JSONObject entity) {
		
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		String result = "";
		JSONArray js = null;
		try {
			//HttpClient httpclient = new DefaultHttpClient();
			//HttpPost httppost = new HttpPost(urls[0]);
			StringEntity params = new StringEntity(entity.toString());

			params.setContentType("application/json;charset=UTF-8");
			params.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
					"application/json;charset=UTF-8"));
			post.setHeader("Accept", "application/json");

			post.setEntity(params);
			HttpResponse response = client.execute(post);
			result = EntityUtils.toString(response.getEntity());
				js = new JSONArray(result);
			//Sonuc = EntityUtils.toString(response.getEntity());
			

			// responseBody = EntityUtils.toString(response.getEntity());


		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
		} catch (IOException e) {
			// TODO Auto-generated catch block
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return js;
	}
public static JSONArray createPostList(String url, JSONObject entity,List<NameValuePair> pairs) {
	
	HttpClient client = new DefaultHttpClient();
	String nUrl = arrangeUrl(url, pairs);
	HttpPost post = new HttpPost(nUrl);
	String result = "";
	JSONArray js = null;
	try {
		//HttpClient httpclient = new DefaultHttpClient();
		//HttpPost httppost = new HttpPost(urls[0]);
		StringEntity params = new StringEntity(entity.toString());

		params.setContentType("application/json;charset=UTF-8");
		params.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
				"application/json;charset=UTF-8"));
		post.setHeader("Accept", "application/json");

		post.setEntity(params);
		HttpResponse response = client.execute(post);
		result = EntityUtils.toString(response.getEntity());
			js = new JSONArray(result);
		//Sonuc = EntityUtils.toString(response.getEntity());
		

		// responseBody = EntityUtils.toString(response.getEntity());


	} catch (ClientProtocolException e) {
		// TODO Auto-generated catch block
	} catch (IOException e) {
		// TODO Auto-generated catch block
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	return js;
}
public static JSONArray createPostList2(String url,List<NameValuePair> pairs) {
	
	HttpClient client = new DefaultHttpClient();
	String nUrl = arrangeUrl(url, pairs);
	HttpPost post = new HttpPost(nUrl);
	String result = "";
	JSONArray js = null;
	try {
		//HttpClient httpclient = new DefaultHttpClient();
		//HttpPost httppost = new HttpPost(urls[0]);
		StringEntity params = new StringEntity("");

		params.setContentType("application/json;charset=UTF-8");
		params.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
				"application/json;charset=UTF-8"));
		post.setHeader("Accept", "application/json");

		post.setEntity(params);
		HttpResponse response = client.execute(post);
		result = EntityUtils.toString(response.getEntity());
			js = new JSONArray(result);
		//Sonuc = EntityUtils.toString(response.getEntity());
		

		// responseBody = EntityUtils.toString(response.getEntity());


	} catch (ClientProtocolException e) {
		// TODO Auto-generated catch block
	} catch (IOException e) {
		// TODO Auto-generated catch block
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	return js;
}
public static String composeTagDTO(ArrayList<InterestsListObject> selectedList){
	
	if(selectedList != null && !selectedList.isEmpty()){
		StringBuffer bfr = new StringBuffer();
		for (int i = 0 ; i < selectedList.size(); i++) {

			bfr.append(selectedList.get(i).getId());
			if(i<selectedList.size()-1){
				bfr.append(",");
			}

		}
		return bfr.toString();
	}
	else{
		return null;
	}
	
}
public static String dotNetSubString(int index, int length, String str){
	if(isNullOrEmpty(str)){
		return str;
	}
	else{
		try {
			return str.substring(index, index+length);
		} catch (Exception e) {
			return null;
		}
	
	}
}
public static String toProperDate(String date){
	if(isNullOrEmpty(date)){
		return null;
	}
	else{
		try {
			String day = date.substring(0, 2);
			String month = date.substring(3, 5);
			String year = date.substring(6,10);
			String hour = date.substring(11,13);
			String minute = date.substring(14,16);
			return day+"/"+month+"/"+year+" "+hour+":"+minute;
		} catch (Exception e) {
			return null;
		}
	
	}
}

}
