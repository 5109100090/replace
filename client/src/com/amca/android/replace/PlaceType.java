package com.amca.android.replace;

import com.amca.android.replace.http.HTTPTransfer;
import com.amca.android.replace.model.Type;
import com.amca.android.replace.gps.GPSTracker;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
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
	private List<Type> typeList = new ArrayList<Type>();
	private TextView textViewPlaceType, textViewRange;
	private Spinner spinnerPlaceType, spinnerRange;
	private Button buttonSubmit;
	private ProgressBar progressBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_place_type);
		
		Intent intent = getIntent();
		this.userId = intent.getIntExtra("userId", 0);
		setTitle("Welcome " + intent.getStringExtra("userAlias"));
		
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
			GPSTracker gps = new GPSTracker(PlaceType.this);
			if(gps.canGetLocation()){
	        	Double latitude = gps.getLatitude();
	        	Double longitude = gps.getLongitude();
	        	this.currentLat = latitude.toString();
	        	this.currentLng = longitude.toString();
	        	Toast.makeText(getApplicationContext(), "Your Location is \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();	
	        }else{
	        	gps.showSettingsAlert();
	        }
		}
		
		HTTPPlaceType http = new HTTPPlaceType();
		http.setCtx(PlaceType.this);
		http.setMode(2);
		http.execute("type/listAll/");
	}
	
	@Override
	public void onClick(View v) {
		int position = spinnerPlaceType.getSelectedItemPosition();
		Type type = typeList.get(position);
		if(type.getTypeTotal() > 0){
			Intent intent = new Intent(PlaceType.this, PlaceSelector.class);
			String [] range = String.valueOf(spinnerRange.getSelectedItem()).split(" meter");
        	intent.putExtra("range", range[0]);
        	intent.putExtra("userId", this.userId);
        	intent.putExtra("typeId", type.getTypeId());
        	intent.putExtra("typeName", type.getTypeName());
        	intent.putExtra("currentLat", this.currentLat);
        	intent.putExtra("currentLng", this.currentLng);
        	startActivity(intent);
		}else{
			Toast.makeText(PlaceType.this, "no data available for " + type.getTypeName(), Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.place_type, menu);
		return true;
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item){
    	switch (item.getItemId()) {
			case R.id.action_profile:
				Intent intent = new Intent(PlaceType.this, UserForm.class);
				intent.putExtra("mode", "profile");
				intent.putExtra("userId", this.userId.toString());
		    	startActivity(intent);
				return true;
			default:
				return super.onOptionsItemSelected(item);
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
					
					Type type = new Type();
					type.setTypeId(json_data.getInt("typeId"));
					type.setTypeName(json_data.getString("typeName"));
					type.setTypeTotal(json_data.getInt("typeTotal"));
					list.add(type.getTypeName());
					typeList.add(type);
				}
				ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, list);
				dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spinnerPlaceType.setAdapter(dataAdapter);
			}catch(JSONException e){
				Toast.makeText(getApplicationContext(), "Error parsing data "+e.toString(), Toast.LENGTH_SHORT).show();
			}
			
			progressBar.setVisibility(View.INVISIBLE);
			textViewPlaceType.setVisibility(View.VISIBLE);
			textViewRange.setVisibility(View.VISIBLE);
			spinnerPlaceType.setVisibility(View.VISIBLE);
			spinnerRange.setVisibility(View.VISIBLE);
			buttonSubmit.setVisibility(View.VISIBLE);
		}
	}  
}