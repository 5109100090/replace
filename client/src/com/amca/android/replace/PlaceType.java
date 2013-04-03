package com.amca.android.replace;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

public class PlaceType extends Activity implements OnClickListener {
	
	private String userId;
	private Spinner spinnerPlaceType, spinnerRange;
	private Button buttonSubmit;
	private ProgressBar progressBar; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_place_type);
		
		Intent intent = getIntent();
		this.userId = intent.getStringExtra("userId");
		
		spinnerPlaceType = (Spinner) findViewById(R.id.spinnerPlaceType);
		spinnerRange = (Spinner) findViewById(R.id.spinnerRange);
		progressBar = (ProgressBar) findViewById(R.id.progressBar1);
		buttonSubmit = (Button) findViewById(R.id.buttonSubmit);
		buttonSubmit.setOnClickListener(this);
		
		progressBar.setVisibility(View.VISIBLE);
		spinnerPlaceType.setVisibility(View.INVISIBLE);
		spinnerRange.setVisibility(View.INVISIBLE);
		buttonSubmit.setVisibility(View.INVISIBLE);
		
		HTTPPlaceType http = new HTTPPlaceType();
		http.execute("type/listAll");
	}
	
	@Override
	public void onClick(View v) {
		String[] placeType = String.valueOf(spinnerPlaceType.getSelectedItem()).split(" - ");
		if(Integer.parseInt(placeType[2]) > 0){
			Intent intent = new Intent(PlaceType.this, PlaceSelector.class);
        	intent.putExtra("userId", this.userId);
        	intent.putExtra("typeId", placeType[0]);
        	intent.putExtra("typeName", placeType[1]);
        	String [] range = String.valueOf(spinnerRange.getSelectedItem()).split(" meter");
        	intent.putExtra("range", range[0]);
        	startActivity(intent);
		}else{
			Toast.makeText(PlaceType.this, "no data available for " + placeType[1], Toast.LENGTH_SHORT).show();
		}
	}
	
	class HTTPPlaceType extends AsyncTask<String, String, String>{
		
		@Override
		protected String doInBackground(String... params) {
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(PlaceType.this);
			String serverUrl = preferences.getString("serverUrl", "") + params[0];
			try {
				HttpClient httpClient = new DefaultHttpClient();
				HttpGet httpGet = new HttpGet(serverUrl);
				
				HttpResponse httpRespose = httpClient.execute(httpGet);
				HttpEntity httpEntity = httpRespose.getEntity();
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
			progressBar.setVisibility(View.INVISIBLE);
			spinnerPlaceType.setVisibility(View.VISIBLE);
			spinnerRange.setVisibility(View.VISIBLE);
			buttonSubmit.setVisibility(View.VISIBLE);
		}
	}
}