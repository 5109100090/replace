package com.amca.android.replace.place;

import java.text.DecimalFormat;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.actionbarsherlock.app.SherlockActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.amca.android.replace.R;
import com.amca.android.replace.model.Place;
import com.amca.android.replace.review.PlaceReviews;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

public class PlaceDetail extends SherlockActivity {

	private Integer userId;
	private Place place;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_place_detail);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		Intent intent = getIntent();
		userId = intent.getIntExtra("userId", 0);
		place = (Place) getIntent().getSerializableExtra("place");
		setTitle(place.getPlaceName());
		
		DecimalFormat df = new DecimalFormat("#0.##");
		((TextView) findViewById(R.id.placeName)).setText(place.getPlaceName());
		((TextView) findViewById(R.id.placeAddress)).setText(place.getPlaceAddress());
		((TextView) findViewById(R.id.placeDesc)).setText(place.getPlaceDesc());
		((TextView) findViewById(R.id.placeReviews)).setText(place.getPlaceReviews() + " review(s) | " + df.format(place.getPlaceDistance()/1000) + " km");
		((TextView) findViewById(R.id.placePoint)).setText(df.format(place.getAveragePoint()));

		ImageView placeIcon = (ImageView) findViewById(R.id.placeIcon);
		
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		String serverUrl = preferences.getString("serverUrl", "");
		
		UrlImageViewHelper.setUrlDrawable(placeIcon, serverUrl + "static/place/" + place.getPlaceId() + ".jpg", R.drawable.gif_loader);
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
				intent.putExtra("placeId", place.getPlaceId());
				intent.putExtra("placeName", place.getPlaceName());
				startActivity(intent);
				return true;
			}
		});

		return true;
	}
}
