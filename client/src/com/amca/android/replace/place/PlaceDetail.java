package com.amca.android.replace.place;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.actionbarsherlock.app.SherlockListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.amca.android.replace.R;
import com.amca.android.replace.http.HTTPTransfer;
import com.amca.android.replace.review.PlaceReviews;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlaceDetail extends SherlockListActivity {

	private Integer userId, placeId;
	private String placeName;
	private ProgressBar progressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_place_detail);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		Intent intent = getIntent();
		this.userId = intent.getIntExtra("userId", 0);
		this.placeId = intent.getIntExtra("placeId", 0);
		this.placeName = intent.getStringExtra("placeName");
		setTitle(this.placeName);
		
		progressBar = (ProgressBar) findViewById(R.id.progressBar1);

		HashMap<String, String> data = new HashMap<String, String>();
		data.put("placeId", placeId.toString());

		HTTPPlaceDetail http = new HTTPPlaceDetail();
		http.setContext(PlaceDetail.this);
		http.setData(data);
		http.execute("place/getDetail/");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem menuReview = menu.add("Review");
		menuReview.setIcon(R.drawable.collections_view_as_list);
		menuReview.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM
				| MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		menuReview.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(final MenuItem item) {
				Intent intent = new Intent(PlaceDetail.this, PlaceReviews.class);
				intent.putExtra("userId", userId);
				intent.putExtra("placeId", placeId);
				intent.putExtra("placeName", placeName);
				startActivity(intent);
				return true;
			}
		});
		
		return true;
	}
	
/*
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_reviews:
			Intent intent = new Intent(PlaceDetail.this, PlaceReviews.class);
			intent.putExtra("userId", this.userId);
			intent.putExtra("placeId", this.placeId);
			startActivity(intent);
			return true;
		case R.id.action_rating:
			final AlertDialog.Builder popDialog = new AlertDialog.Builder(
					PlaceDetail.this);
			final RatingBar rating = new RatingBar(PlaceDetail.this);
			// rating.setMax(5);
			rating.setNumStars(5);
			rating.setStepSize(1);

			popDialog.setIcon(android.R.drawable.btn_star_big_on);
			popDialog.setTitle("Give Rating");
			popDialog.setView(rating);

			popDialog.setPositiveButton(android.R.string.ok,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							System.out.println(String.valueOf(rating
									.getProgress()));
							HashMap<String, String> data = new HashMap<String, String>();
							data.put("userId", userId.toString());
							data.put("placeId", placeId.toString());
							data.put("reviewPoint",
									String.valueOf(rating.getProgress()));

							HTTPTransfer rating = new HTTPTransfer();
							rating.setContext(PlaceDetail.this);
							rating.setData(data);
							rating.setMode(2);
							rating.execute("review/setRating");
							dialog.dismiss();
						}
					}).setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
						}
					});

			popDialog.create();
			popDialog.show();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
*/
	
	class HTTPPlaceDetail extends HTTPTransfer {
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			// parse json data
			try {
				List<String> list = new ArrayList<String>();
				JSONArray jArray = new JSONArray(result);
				JSONObject json_data = jArray.getJSONObject(0);
				list.add("ID : " + json_data.getString("placeId"));
				list.add("Name :\n" + json_data.getString("placeName"));
				list.add("Description :\n" + json_data.getString("placeDesc"));
				list.add("Latitude :\n" + json_data.getString("placeLat"));
				list.add("Longitude :\n" + json_data.getString("placeLng"));
				list.add("Type :\n" + json_data.getString("typeName"));
				setTitle(json_data.getString("placeName"));

				ArrayAdapter<String> adapter = new ArrayAdapter<String>(
						this.getContext(), android.R.layout.simple_list_item_1,
						list);
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
