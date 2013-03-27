package com.amca.android.replace;

import android.os.Bundle;
import android.app.ListActivity;
import android.view.Menu;

public class PlaceSelector extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_place_selector);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.place_selector, menu);
		return true;
	}

}
