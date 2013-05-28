package com.amca.android.replace;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.amca.android.replace.http.HTTPTransfer;
import com.amca.android.replace.model.Place;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlaceSelector extends ListActivity {

	private Integer userId, typeId, nPlaces = 0;
	private String range, currentLat, currentLng, jsonValue, typeName;
	private List<Place> placesList = new ArrayList<Place>();
	private ProgressBar progressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_place_selector);

		Intent intent = getIntent();
		this.userId = intent.getIntExtra("userId", 0);
		this.typeId = intent.getIntExtra("typeId", 0);
		this.range = intent.getStringExtra("range");
		this.currentLat = intent.getStringExtra("currentLat");
		this.currentLng = intent.getStringExtra("currentLng");
		typeName = intent.getStringExtra("typeName");

		progressBar = (ProgressBar) findViewById(R.id.progressBar1);

		HashMap<String, String> data = new HashMap<String, String>();
		data.put("userId", userId.toString());
		data.put("typeId", typeId.toString());
		data.put("range", range);
		data.put("currentLat", this.currentLat);
		data.put("currentLng", this.currentLng);

		HTTPPlaceSelector http = new HTTPPlaceSelector();
		http.setContext(PlaceSelector.this);
		http.setData(data);
		http.execute("place/process/");
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Place selectedPlace = placesList.get(position);
		Intent intent = new Intent(PlaceSelector.this, PlaceReviews.class);
		intent.putExtra("placeId", selectedPlace.getPlaceId());
		intent.putExtra("userId", this.userId);
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.place_selector, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_map:
			Intent intent = new Intent(PlaceSelector.this,
					PlaceSelectorMap.class);
			intent.putExtra("currentLat", Float.parseFloat(this.currentLat));
			intent.putExtra("currentLng", Float.parseFloat(this.currentLng));
			intent.putExtra("jsonValue", this.jsonValue);
			intent.putExtra("userId", this.userId);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	class HTTPPlaceSelector extends HTTPTransfer {
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			jsonValue = result;
			// parse json data
			try {
				ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
				JSONArray jArray = new JSONArray(result);
				nPlaces = jArray.length();
				setTitle(nPlaces + " closest " + typeName + " in " + range + " meter");
				for (int i = 0; i < nPlaces; i++) {
					JSONObject json_data = jArray.getJSONObject(i);

					HashMap<String, String> map = new HashMap<String, String>();
					map.put("placeName", json_data.getString("placeName"));
					map.put("placeDesc", json_data.getString("placeDesc"));
					map.put("placeReviews", json_data.getString("placeReviews"));
					map.put("placeDistance", json_data.getString("placeDistance"));
					
					Place place = new Place();
					place.setPlaceId(json_data.getInt("placeId"));
					/*place.setPlaceName(map.get("placeName"));
					place.setPlaceDesc(map.get("placeDesc"));
					place.setPlaceLat(json_data.getString("placeLat"));
					place.setPlaceLng(json_data.getString("placeLng"));
					place.setPlaceType(json_data.getInt("placeType"));*/
					placesList.add(place);

					list.add(map);
				}

				PlaceSelectorArrayAdapter adapter = new PlaceSelectorArrayAdapter(PlaceSelector.this, list);
				setListAdapter(adapter);
			} catch (JSONException e) {
				Toast.makeText(getApplicationContext(),
						"Error parsing data " + e.toString(),
						Toast.LENGTH_SHORT).show();
			}
			progressBar.setVisibility(View.INVISIBLE);
		}
	}
}
