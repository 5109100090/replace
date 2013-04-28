package com.amca.android.replace;

import com.amca.android.replace.http.HTTPTransfer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.Bundle;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Toast;

public class PlaceDetail extends ListActivity {

	private Integer userId, placeId;
	private ProgressBar progressBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_place_detail);
		
		Intent intent = getIntent();
		this.userId = intent.getIntExtra("userId", 0);
		this.placeId = intent.getIntExtra("placeId", 0);
		
		progressBar = (ProgressBar) findViewById(R.id.progressBar1);
		
		HashMap<String, String> data = new HashMap<String, String>();
		data.put("placeId", placeId.toString());
		
		HTTPPlaceDetail http = new HTTPPlaceDetail();
		http.setCtx(PlaceDetail.this);
		http.setData(data);
		http.execute("place/getDetail");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.place_detail, menu);
		return true;
	}
	
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
    	switch (item.getItemId()) {
			case R.id.action_reviews:
				Intent intent = new Intent(PlaceDetail.this, PlaceReviews.class);
		    	intent.putExtra("placeId", this.placeId);
		    	startActivity(intent);
				return true;
			case R.id.action_rating:
				final AlertDialog.Builder popDialog = new AlertDialog.Builder(this);
				final RatingBar rating = new RatingBar(this);
				rating.setMax(5);
				
				popDialog.setTitle("Give Rating");
				popDialog.setView(rating);
				
				popDialog.setPositiveButton(android.R.string.ok,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							System.out.println(String.valueOf(rating.getProgress()));
							HashMap<String, String> data = new HashMap<String, String>();
							data.put("userId", userId.toString());
							data.put("placeId", placeId.toString());
							data.put("reviewPoint", String.valueOf(rating.getProgress()));
							
							HTTPTransfer rating = new HTTPTransfer();
							rating.setCtx(PlaceDetail.this);
							rating.setData(data);
							rating.setMode(2);
							rating.execute("review/setRating");
							dialog.dismiss();
						}
					}
				)
				.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
						}
					}
				);
				
				popDialog.create();
				popDialog.show();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
    }
    
	class HTTPPlaceDetail extends HTTPTransfer{
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			//parse json data
			try{
				List<String> list = new ArrayList<String>();
				JSONArray jArray = new JSONArray(result);
				JSONObject json_data = jArray.getJSONObject(0);
				list.add("ID : "+json_data.getString("placeId"));
				list.add("Name :\n"+json_data.getString("placeName"));
				list.add("Description :\n"+json_data.getString("placeDesc"));
				list.add("Latitude :\n"+json_data.getString("placeLat"));
				list.add("Longitude :\n"+json_data.getString("placeLng"));
				list.add("Type :\n"+json_data.getString("typeName"));
				setTitle(json_data.getString("placeName"));
				
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, list);
				setListAdapter(adapter);
			}catch(JSONException e){
				Toast.makeText(getApplicationContext(), "Error parsing data "+e.toString(), Toast.LENGTH_SHORT).show();
			}
			progressBar.setVisibility(View.INVISIBLE);
		}
	}
}
