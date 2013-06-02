package com.amca.android.replace.place;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.actionbarsherlock.app.SherlockListActivity;
import android.content.Intent;
import android.os.Bundle;
import com.amca.android.replace.R;
import com.amca.android.replace.review.PlaceReviews;

public class PlaceDetail extends SherlockListActivity {

	private Integer userId, placeId;
	private String placeName;

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
}
