package com.amca.android.replace.review;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import com.amca.android.replace.R;
import com.amca.android.replace.http.HTTPTransfer;
import com.amca.android.replace.model.Review;
import com.amca.android.replace.model.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlaceReviewsMinimalist extends ListActivity {

	private Integer userId, placeId;
	private String placeName;
	private List<Review> list = new ArrayList<Review>();
	PlaceReviewsArrayAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_place_reviews_minimalist);
		
		Intent intent = getIntent();
		this.userId = intent.getIntExtra("userId", 0);
		this.placeId = intent.getIntExtra("placeId", 0);
		this.placeName = intent.getStringExtra("placeName");
		Integer typeId = intent.getIntExtra("typeId", 1);
		setTitle(this.placeName);

		HashMap<String, String> data = new HashMap<String, String>();
		data.put("userId", userId.toString());
		data.put("placeId", placeId.toString());
		data.put("typeId", typeId.toString());

		HTTPPlaceDetail http = new HTTPPlaceDetail();
		http.setContext(PlaceReviewsMinimalist.this);
		http.setData(data);
		http.execute("review/listReviews/");
	}

	class HTTPPlaceDetail extends HTTPTransfer {
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			// parse json data
			try {
				JSONArray jArray = new JSONArray(result);

				for (int i = 0; i < jArray.length(); i++) {
					JSONObject json_data = jArray.getJSONObject(i);

					User user = new User();
					user.setUserId(json_data.getInt("userId"));
					user.setUserAlias(json_data.getString("userAlias"));

					Review review = new Review();
					review.setReviewUser(user);
					review.setReviewPointPrice(json_data.getInt("reviewPointPrice"));
					review.setReviewPointService(json_data.getInt("reviewPointService"));
					review.setReviewPointLocation(json_data.getInt("reviewPointLocation"));
					review.setReviewPointCondition(json_data.getInt("reviewPointCondition"));
					review.setReviewPointComfort(json_data.getInt("reviewPointComfort"));
					review.setAveragePoint(json_data.getDouble("averagePoint"));
					review.setReviewText(json_data.getString("reviewText"));
					review.setSimilarity(json_data.getDouble("similarityValue"));
					review.setNewSimilarity(json_data.getDouble("newSimilarityValue"));
					review.setSimilarityFlag(json_data.getInt("similarityFlag"));

					list.add(review);
				}

				adapter = new PlaceReviewsArrayAdapter(
						PlaceReviewsMinimalist.this, list, R.layout.activity_place_reviews_row_minimalist);
				setListAdapter(adapter);
			} catch (JSONException e) {
				System.out.println("Error parsing data " + e.toString());
			}
		}
	}
}
