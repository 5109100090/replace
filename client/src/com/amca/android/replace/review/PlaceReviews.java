package com.amca.android.replace.review;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.amca.android.replace.R;
import com.amca.android.replace.http.HTTPTransfer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;

public class PlaceReviews extends ListActivity {

	private Integer userId, placeId;
	private ProgressBar progressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_place_reviews);

		Intent intent = getIntent();
		this.userId = intent.getIntExtra("userId", 0);
		this.placeId = intent.getIntExtra("placeId", 0);

		progressBar = (ProgressBar) findViewById(R.id.progressBar1);

		HashMap<String, String> data = new HashMap<String, String>();
		data.put("userId", userId.toString());
		data.put("placeId", placeId.toString());

		HTTPPlaceDetail http = new HTTPPlaceDetail();
		http.setContext(PlaceReviews.this);
		http.setData(data);
		http.execute("review/listReviews/");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.place_reviews, menu);
		return true;
	}

	class HTTPPlaceDetail extends HTTPTransfer {
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			// parse json data
			try {
				ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
				JSONArray jArray = new JSONArray(result);

				for (int i = 0; i < jArray.length(); i++) {
					JSONObject json_data = jArray.getJSONObject(i);

					HashMap<String, String> map = new HashMap<String, String>();
					map.put("reviewUser", json_data.getString("userAlias"));
					map.put("reviewPoint", json_data.getString("reviewPoint"));
					map.put("similarity",
							json_data.getString("similarityValue"));
					map.put("reviewText", "oke");
					list.add(map);
				}

				PlaceReviewsArrayAdapter adapter = new PlaceReviewsArrayAdapter(
						PlaceReviews.this, list);
				setListAdapter(adapter);
			} catch (JSONException e) {
				Toast.makeText(getApplicationContext(),
						"Error parsing data " + e.toString(),
						Toast.LENGTH_SHORT).show();
				System.out.println("Error parsing data " + e.toString());
			}
			progressBar.setVisibility(View.INVISIBLE);
		}
	}
}
