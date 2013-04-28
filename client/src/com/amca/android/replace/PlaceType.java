package com.amca.android.replace;

import com.amca.android.replace.http.HTTPTransfer;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class PlaceType extends Activity implements OnClickListener {
	
	private Integer userId;
	private String currentLat, currentLng, geolocation;
	private TextView textViewPlaceType, textViewRange;
	private Spinner spinnerPlaceType, spinnerRange;
	private Button buttonSubmit;
	private ProgressBar progressBar;
	private LocationManager locationManager = null;
	private MyLocationListener locationListener = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_place_type);
		
		Intent intent = getIntent();
		this.userId = intent.getIntExtra("userId", 0);
		
		textViewPlaceType = (TextView) findViewById(R.id.textViewPlaceType);
		textViewRange = (TextView) findViewById(R.id.textViewRange);
		spinnerPlaceType = (Spinner) findViewById(R.id.spinnerPlaceType);
		spinnerRange = (Spinner) findViewById(R.id.spinnerRange);
		progressBar = (ProgressBar) findViewById(R.id.progressBar1);
		buttonSubmit = (Button) findViewById(R.id.buttonSubmit);
		buttonSubmit.setOnClickListener(this);
		
		progressBar.setVisibility(View.VISIBLE);
		textViewPlaceType.setVisibility(View.INVISIBLE);
		textViewRange.setVisibility(View.INVISIBLE);
		spinnerPlaceType.setVisibility(View.INVISIBLE);
		spinnerRange.setVisibility(View.INVISIBLE);
		buttonSubmit.setVisibility(View.INVISIBLE);
		
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(PlaceType.this);
		geolocation = preferences.getString("geolocation", "");
		
		if(geolocation.equals("static")){
			this.currentLat = "-7.27957";
			this.currentLng = "112.79751";
		}else{
			locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			locationListener = new MyLocationListener();
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
		}
		
		HTTPPlaceType http = new HTTPPlaceType();
		http.setCtx(PlaceType.this);
		http.setMode(2);
		http.execute("type/listAll");
	}
	
	@Override
	public void onClick(View v) {
		String[] placeType = String.valueOf(spinnerPlaceType.getSelectedItem()).split(" - ");
		if(Integer.parseInt(placeType[2]) > 0){
			Intent intent = new Intent(PlaceType.this, PlaceSelector.class);
			String [] range = String.valueOf(spinnerRange.getSelectedItem()).split(" meter");
        	intent.putExtra("range", range[0]);
        	intent.putExtra("userId", this.userId);
        	intent.putExtra("typeId", Integer.parseInt(placeType[0]));
        	intent.putExtra("typeName", placeType[1]);
        	intent.putExtra("currentLat", this.currentLat);
        	intent.putExtra("currentLng", this.currentLng);
        	startActivity(intent);
		}else{
			Toast.makeText(PlaceType.this, "no data available for " + placeType[1], Toast.LENGTH_SHORT).show();
		}
	}
	
	class HTTPPlaceType extends HTTPTransfer{
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			//parse json data
			try{
				List<String> list = new ArrayList<String>();
				JSONArray jArray = new JSONArray(result);
				for(int i=0;i<jArray.length();i++){
					JSONObject json_data = jArray.getJSONObject(i);
					list.add(json_data.getString("typeId") + " - " + json_data.getString("typeName") + " - " + json_data.getString("typeTotal"));
				}
				ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, list);
				dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spinnerPlaceType.setAdapter(dataAdapter);
			}catch(JSONException e){
				Toast.makeText(getApplicationContext(), "Error parsing data "+e.toString(), Toast.LENGTH_SHORT).show();
			}
			
			if(geolocation.equals("static")){
				progressBar.setVisibility(View.INVISIBLE);
				textViewPlaceType.setVisibility(View.VISIBLE);
				textViewRange.setVisibility(View.VISIBLE);
				spinnerPlaceType.setVisibility(View.VISIBLE);
				spinnerRange.setVisibility(View.VISIBLE);
				buttonSubmit.setVisibility(View.VISIBLE);
			}
		}
	}
	
	class MyLocationListener implements LocationListener {
		@Override
		public void onLocationChanged(Location loc) {
			Toast.makeText(getBaseContext(),"Location changed : Lat: " +
					loc.getLatitude()+ " Lng: " + loc.getLongitude(),
					Toast.LENGTH_SHORT).show();
			
			Double lat = loc.getLatitude();
			Double lng = loc.getLongitude();
			currentLat = lat.toString();
			currentLng = lng.toString();

			if(geolocation.equals("auto")){
				progressBar.setVisibility(View.INVISIBLE);
				textViewPlaceType.setVisibility(View.VISIBLE);
				textViewRange.setVisibility(View.VISIBLE);
				spinnerPlaceType.setVisibility(View.VISIBLE);
				spinnerRange.setVisibility(View.VISIBLE);
				buttonSubmit.setVisibility(View.VISIBLE);
			}
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