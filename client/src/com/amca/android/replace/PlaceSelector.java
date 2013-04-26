package com.amca.android.replace;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class PlaceSelector extends ListActivity {

	private LocationManager locationMangaer = null;  
	private MyLocationListener locationListener = null;   
	private String userId, typeId, range, currentLat, currentLng, jsonValue;
	private ProgressBar progressBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_place_selector);
		
		Intent intent = getIntent();
		this.userId = intent.getStringExtra("userId");
		this.typeId = intent.getStringExtra("typeId");
		this.range = intent.getStringExtra("range");
		String typeName = intent.getStringExtra("typeName");
		setTitle(typeName);
		
		progressBar = (ProgressBar) findViewById(R.id.progressBar1);
		
		/*/
		locationMangaer = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationListener = new MyLocationListener();  
		locationMangaer.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener); 
		//*/
		this.currentLat = "-7.27957";
		this.currentLng = "112.79751";
		
		HashMap<String, String> data = new HashMap<String, String>();
		data.put("userId", userId);
		data.put("typeId", typeId);
		data.put("range", range);
		/*/
		data.put("currentLat", locationListener.getLatitude());
		data.put("currentLng", locationListener.getLongitude());
		System.out.println("lat : "+locationListener.getLatitude()+" | lon : "+locationListener.getLongitude());
		/*/
		data.put("currentLat", this.currentLat);
		data.put("currentLng", this.currentLng);
		//*/
		
		HTTPPlaceSelector http = new HTTPPlaceSelector(data);
		http.execute("place/process");
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		String item = (String) getListAdapter().getItem(position);
		String[] s = item.split(" - ");
		//Toast.makeText(this, s[1] + " selected, go ahead", Toast.LENGTH_SHORT).show();
		
		Intent intent = new Intent(PlaceSelector.this, PlaceDetail.class);
    	intent.putExtra("placeId", s[0]);
    	startActivity(intent);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.place_selector, menu);
		return true;
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item){
    	switch (item.getItemId()) {
			case R.id.action_map:
				Intent intent = new Intent(PlaceSelector.this, PlaceSelectorMap.class);
				intent.putExtra("currentLat", Float.parseFloat(this.currentLat));
				intent.putExtra("currentLng", Float.parseFloat(this.currentLng));
				intent.putExtra("jsonValue", this.jsonValue);
		    	startActivity(intent);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
    }
	
	class HTTPPlaceSelector extends AsyncTask<String, String, String>{
		
		private HashMap<String, String> mData = null;
		
		public HTTPPlaceSelector(HashMap<String, String> data){
			mData = data;
		}
		
		@Override
		protected String doInBackground(String... params) {
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(PlaceSelector.this);
			String serverUrl = preferences.getString("serverUrl", "") + params[0];
			
			ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
            Iterator<String> it = mData.keySet().iterator();
            while (it.hasNext()) {
                String key = it.next();
                nameValuePair.add(new BasicNameValuePair(key, mData.get(key)));
            }
            
			try {
				HttpClient client = new DefaultHttpClient();				
				HttpPost post = new HttpPost(serverUrl);
				post.setEntity(new UrlEncodedFormEntity(nameValuePair));
				HttpResponse responsePost = client.execute(post);
				HttpEntity httpEntity = responsePost.getEntity();
				InputStream in = httpEntity.getContent();
				BufferedReader read = new BufferedReader(new InputStreamReader(in));

				String isi= "";
				String baris= "";
				while((baris = read.readLine())!=null){
					isi+= baris;
				} 

				return isi;

			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			jsonValue = result;
			//parse json data
			try{
				List<String> list = new ArrayList<String>();
				JSONArray jArray = new JSONArray(result);
				
				if(jArray.length() == 0){
					list.add("no data available");
				}
				
				for(int i=0;i<jArray.length();i++){
					JSONObject json_data = jArray.getJSONObject(i);
					list.add(json_data.getString("placeId") + " - " + json_data.getString("placeName") + " - " + json_data.getString("placeDistance"));
				}
				
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, list);
				setListAdapter(adapter);
			}catch(JSONException e){
				Toast.makeText(getApplicationContext(), "Error parsing data "+e.toString(), Toast.LENGTH_SHORT).show();
			}
			progressBar.setVisibility(View.INVISIBLE);
		}
	}
	
	/*----------Listener class to get coordinates ------------- */  
	 class MyLocationListener implements LocationListener {  
		 
		 private String longitude;
		 private String latitude;
		 
		 public String getLongitude() {
			 return longitude;
		 }
		 
		 public void setLongitude(String longitude) {
			 this.longitude = longitude;
		 }
		 
		 public String getLatitude() {
			 return latitude;
		 }
		 
		 public void setLatitude(String latitude) {
			 this.latitude = latitude;
		 }

		 @Override  
	     public void onLocationChanged(Location loc) {  
			 Toast.makeText(getBaseContext(),"Location changed : Lat: " +
					 loc.getLatitude()+ " Lng: " + loc.getLongitude(),
					 Toast.LENGTH_SHORT).show();
			 
			 Double lat = loc.getLatitude();
			 Double lng = loc.getLongitude();
			 
			 this.setLatitude(lat.toString());
			 this.setLongitude(lng.toString());  
		 }
		 
		 @Override
		 public void onProviderDisabled(String provider) {
			 // TODO Auto-generated method stub
		 }

		 @Override
		 public void onProviderEnabled(String provider) {
			 // TODO Auto-generated method stub           
		 }  
	  
		 @Override  
		 public void onStatusChanged(String provider, int status, Bundle extras) {  
			 // TODO Auto-generated method stub           
		 }
	 }  
}
