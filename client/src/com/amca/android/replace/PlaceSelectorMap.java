package com.amca.android.replace;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;

public class PlaceSelectorMap extends FragmentActivity  {

	private GoogleMap map = null;
	private Float currentLat, currentLng;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_place_selector_map);
		Intent intent = getIntent();
		this.currentLat = intent.getFloatExtra("currentLat", 0);
		this.currentLng = intent.getFloatExtra("currentLng", 0);
		System.out.println(this.currentLat+" : "+this.currentLng);
		
		setUpMapIfNeeded();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.place_selector_map, menu);
		return true;
	}

	private void setUpMapIfNeeded() {
	    // Do a null check to confirm that we have not already instantiated the map.
	    if (map == null) {
	    	map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
	                            .getMap();
	        // Check if we were successful in obtaining the map.
	        if (map != null) {
	            // The Map is verified. It is now safe to manipulate the map.
	        	map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(this.currentLat, this.currentLng), 15));
	    		map.animateCamera(CameraUpdateFactory.zoomIn());
	        }
	    }
	}

}
