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

import com.amca.android.replace.http.HTTPTransfer;

import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Toast;

public class PlaceReviews extends ListActivity {
	
	private Integer placeId;
	private ProgressBar progressBar; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_place_reviews);
		
		Intent intent = getIntent();
		this.placeId = intent.getIntExtra("placeId", 0);
		
		progressBar = (ProgressBar) findViewById(R.id.progressBar1);
		
		HashMap<String, String> data = new HashMap<String, String>();
		data.put("placeId", placeId.toString());
		
		HTTPPlaceDetail http = new HTTPPlaceDetail();
		http.setCtx(PlaceReviews.this);
		http.setData(data);
		http.execute("review/listReviews");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.place_reviews, menu);
		return true;
	}

	class HTTPPlaceDetail extends HTTPTransfer{
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			//parse json data
			try{
				List<String> list = new ArrayList<String>();
				JSONArray jArray = new JSONArray(result);
				
				for(int i=0;i<jArray.length();i++){
					JSONObject json_data = jArray.getJSONObject(i);
					list.add(json_data.getString("userAlias") + " : " + json_data.getString("reviewPoint") + " stars");
				}
				
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, list);
				setListAdapter(adapter);
			}catch(JSONException e){
				Toast.makeText(getApplicationContext(), "Error parsing data "+e.toString(), Toast.LENGTH_SHORT).show();
				System.out.println("Error parsing data "+e.toString());
			}
			progressBar.setVisibility(View.INVISIBLE);
		}
	}
}
