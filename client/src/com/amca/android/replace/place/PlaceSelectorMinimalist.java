package com.amca.android.replace.place;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.amca.android.replace.R;
import com.amca.android.replace.Setting;
import com.amca.android.replace.http.HTTPTransfer;
import com.amca.android.replace.model.Place;
import com.amca.android.replace.review.PlaceReviewsMinimalist;

import android.os.Bundle;
import android.app.ListActivity;
import android.content.Intent;
import android.view.View;
import android.widget.ListView;

public class PlaceSelectorMinimalist extends ListActivity {

	private Integer userId, typeId;
	private String range, currentLat, currentLng, jsonValue, typeName, title;
	private List<Place> placesList = new ArrayList<Place>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_place_selector_minimalist);
		
		Intent intent = getIntent();
		this.userId = intent.getIntExtra("userId", 0);
		this.typeId = intent.getIntExtra("typeId", 0);
		this.range = intent.getStringExtra("range");
		this.currentLat = intent.getStringExtra("currentLat");
		this.currentLng = intent.getStringExtra("currentLng");
		typeName = intent.getStringExtra("typeName");

		HashMap<String, String> data = new HashMap<String, String>();
		data.put("userId", userId.toString());
		data.put("typeId", typeId.toString());
		data.put("range", range);
		data.put("currentLat", this.currentLat);
		data.put("currentLng", this.currentLng);

		HTTPPlaceSelector http = new HTTPPlaceSelector();
		http.setContext(PlaceSelectorMinimalist.this);
		http.setData(data);
		http.execute("place/process/");
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Place place = placesList.get(position);
		
		Intent intent = new Intent(PlaceSelectorMinimalist.this, PlaceReviewsMinimalist.class);
		intent.putExtra("userId", userId);
		intent.putExtra("placeId", place.getPlaceId());
		intent.putExtra("placeName", place.getPlaceName());
		startActivity(intent);
	}

	class HTTPPlaceSelector extends HTTPTransfer {
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			jsonValue = result;
			// parse json data
			try {
				JSONArray jArray = new JSONArray(result);
				int nPlaces = jArray.length();
				title = "showing " + nPlaces + " result(s)"; 
				setTitle(title);
				for (int i = 0; i < nPlaces; i++) {
					JSONObject json_data = jArray.getJSONObject(i);

					Place place = new Place();
					place.setPlaceId(json_data.getInt("placeId"));
					place.setPlaceName(json_data.getString("placeName"));
					place.setPlaceAddress(json_data.getString("placeAddress"));
					place.setPlaceDesc(json_data.getString("placeDesc"));
					place.setPlaceReviews(json_data.getInt("placeReviews"));
					place.setPlaceDistance(json_data.getDouble("placeDistance"));
					place.setAveragePoint(json_data.getDouble("averagePoint"));
					placesList.add(place);
				}

				PlaceSelectorArrayAdapter adapter = new PlaceSelectorArrayAdapter(
						PlaceSelectorMinimalist.this, placesList, R.layout.activity_place_selector_row_minimalist);
				setListAdapter(adapter);
			} catch (JSONException e) {
				System.out.println("Error parsing data " + e.toString());
			}
		}
	}

}
