package com.amca.android.replace.review;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.actionbarsherlock.app.SherlockListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
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

public class PlaceReviews extends SherlockListActivity {

	private Integer userId, placeId;
	private String placeName;
	private ProgressBar progressBar;
	private List<Review> list = new ArrayList<Review>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_place_reviews);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		Intent intent = getIntent();
		this.userId = intent.getIntExtra("userId", 0);
		this.placeId = intent.getIntExtra("placeId", 0);
		this.placeName = intent.getStringExtra("placeName");
		setTitle(this.placeName);
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
		MenuItem menuReview = menu.add("Review");
		menuReview.setIcon(R.drawable.content_new);
		menuReview.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM
				| MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		menuReview.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(final MenuItem item) {
				Intent intent = new Intent(PlaceReviews.this, WriteReview.class);
				intent.putExtra("mode", "update");
				intent.putExtra("userId", userId.toString());
				startActivity(intent);
				return true;
			}
		});
		
        SubMenu subMenu1 = menu.addSubMenu(0, Menu.FIRST, Menu.NONE, "Sort");
        subMenu1.add(0, 10, Menu.NONE, "by Star");
        subMenu1.add(0, 20, Menu.NONE, "by Similarity");
        subMenu1.add(0, 30, Menu.NONE, "by New Similarity");

        MenuItem subMenu1Item = subMenu1.getItem();
        subMenu1Item.setIcon(R.drawable.collections_sort_by_size);
        subMenu1Item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        
        return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int key = item.getItemId();
		setListAdapter(null);
		PlaceReviewsArrayAdapter adapter = new PlaceReviewsArrayAdapter(
				PlaceReviews.this, list);
		adapter.sort(key);
		setListAdapter(adapter);
		adapter.notifyDataSetChanged();
		return super.onOptionsItemSelected(item);
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

					Double newSimilarity = json_data.getDouble("reviewPoint")
							* json_data.getDouble("similarityValue");
					User user = new User();
					user.setUserAlias(json_data.getString("userAlias"));

					Review review = new Review();
					review.setReviewUser(user);
					review.setReviewPoint(json_data.getInt("reviewPoint"));
					review.setReviewText("bagus sekali");
					review.setSimilarity(json_data.getDouble("similarityValue"));
					review.setNewSimilarity(newSimilarity);

					list.add(review);
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
