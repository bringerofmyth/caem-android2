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
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

	public static String POST_USER_URL = "";
	public static String GET_PLACE_URL = "";
	public static String PostRegisterUser ="";
	public static String WaitMessage ="Please wait...";
	public static JSONObject passedJson = null;
	public static JSONArray passedHotels = null;
	public static String ReservationUrl = "";


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

	public static void createIntent(Context ctx, Class<?> cls) {
		Intent intent = new Intent(ctx, cls);
		ctx.startActivity(intent);
	}

	public static void createIntent(Context ctx, Class<?> cls, String putName,
			String putValue) {
		Intent intent = new Intent(ctx, cls);
		intent.putExtra(putName, putValue);
		ctx.startActivity(intent);
	}

	public static void createIntent(Context ctx, Class<?> cls, String putName,
			Serializable putValue) {
		Intent intent = new Intent(ctx, cls);
		intent.putExtra(putName, putValue);
		ctx.startActivity(intent);
	}

}
