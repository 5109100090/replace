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
import com.amca.android.replace.review.PlaceReviews;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

public class PlaceDetail extends SherlockActivity {

	private Integer userId, placeId;
	private String placeNameValue;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_place_detail);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		Intent intent = getIntent();
		this.userId = intent.getIntExtra("userId", 0);
		this.placeId = intent.getIntExtra("placeId", 0);
		this.placeNameValue = intent.getStringExtra("placeName");
		setTitle(this.placeNameValue);
		
		DecimalFormat df = new DecimalFormat("#0.##");
		((TextView) findViewById(R.id.placeName)).setText(intent.getStringExtra("placeName"));
		((TextView) findViewById(R.id.placeAddress)).setText(intent.getStringExtra("placeAddress"));
		((TextView) findViewById(R.id.placeDesc)).setText(intent.getStringExtra("placeDesc"));
		((TextView) findViewById(R.id.placeReviews)).setText(intent.getStringExtra("placeReviews") + " review(s) | " + df.format(intent.getDoubleExtra("placeDistance", 0)/1000) + " km");
		((TextView) findViewById(R.id.placePoint)).setText(df.format(intent.getDoubleExtra("averagePoint", 0)));

		ImageView placeIcon = (ImageView) findViewById(R.id.placeIcon);
		
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		String serverUrl = preferences.getString("serverUrl", "");
		
		UrlImageViewHelper.setUrlDrawable(placeIcon, serverUrl + "static/place/" + this.placeId + ".jpg", R.drawable.gif_loader);
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
				intent.putExtra("placeName", placeNameValue);
				startActivity(intent);
				return true;
			}
		});

		return true;
	}
}
