package com.amca.android.replace;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.actionbarsherlock.app.SherlockActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.amca.android.replace.gps.GPSTracker;
import com.amca.android.replace.http.HTTPTransfer;
import com.amca.android.replace.model.Type;
import com.amca.android.replace.place.PlaceSelector;
import com.amca.android.replace.user.UserForm;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PlaceType extends SherlockActivity implements OnClickListener {

	private Integer userId;
	private String currentLat, currentLng;
	private boolean autoLocation;
	private List<Type> typeList = new ArrayList<Type>();
	private TextView textViewPlaceType, textViewRange;
	private Spinner spinnerPlaceType, spinnerRange;
	private Button buttonFind;
	private ProgressBar progressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_place_type);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		Intent intent = getIntent();
		this.userId = intent.getIntExtra("userId", 0);
		setTitle("Welcome");

		textViewPlaceType = (TextView) findViewById(R.id.textViewPlaceType);
		textViewRange = (TextView) findViewById(R.id.textViewRange);
		spinnerPlaceType = (Spinner) findViewById(R.id.spinnerPlaceType);
		spinnerRange = (Spinner) findViewById(R.id.spinnerRange);
		progressBar = (ProgressBar) findViewById(R.id.progressBar1);
		buttonFind = (Button) findViewById(R.id.buttonFind);
		buttonFind.setOnClickListener(this);

		progressBar.setVisibility(View.VISIBLE);
		textViewPlaceType.setVisibility(View.INVISIBLE);
		textViewRange.setVisibility(View.INVISIBLE);
		spinnerPlaceType.setVisibility(View.INVISIBLE);
		spinnerRange.setVisibility(View.INVISIBLE);
		buttonFind.setVisibility(View.INVISIBLE);

		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(PlaceType.this);
		autoLocation = preferences.getBoolean("autoLocation", false);

		if (autoLocation) {
			GPSTracker gps = new GPSTracker(PlaceType.this);
			if (gps.canGetLocation()) {
				Double latitude = gps.getLatitude();
				Double longitude = gps.getLongitude();
				this.currentLat = latitude.toString();
				this.currentLng = longitude.toString();
				if (autoLocation) {
					progressBar.setVisibility(View.INVISIBLE);
					textViewPlaceType.setVisibility(View.VISIBLE);
					textViewRange.setVisibility(View.VISIBLE);
					spinnerPlaceType.setVisibility(View.VISIBLE);
					spinnerRange.setVisibility(View.VISIBLE);
					buttonFind.setVisibility(View.VISIBLE);
				}
			} else {
				gps.showSettingsAlert();
			}
		} else {
			this.currentLat = "-7.279241";
			this.currentLng = "112.790392";
		}

		HTTPPlaceType http = new HTTPPlaceType();
		http.setContext(PlaceType.this);
		http.setMode(2);
		http.execute("type/listAll/");
	}

	@Override
	public void onClick(View v) {
		int position = spinnerPlaceType.getSelectedItemPosition();
		Type type = typeList.get(position);
		if (type.getTypeTotal() > 0) {
			Intent intent = new Intent(PlaceType.this, PlaceSelector.class);
			String[] range = String.valueOf(spinnerRange.getSelectedItem())
					.split(" meter");
			intent.putExtra("range", range[0]);
			intent.putExtra("userId", this.userId);
			intent.putExtra("typeId", type.getTypeId());
			intent.putExtra("typeName", type.getTypeName());
			intent.putExtra("currentLat", this.currentLat);
			intent.putExtra("currentLng", this.currentLng);
			startActivity(intent);
		} else {
			Toast.makeText(PlaceType.this,
					"no data available for " + type.getTypeName(),
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem menuProfile = menu.add("Profile");
		menuProfile.setIcon(R.drawable.social_person);
		menuProfile.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM
				| MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		menuProfile.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(final MenuItem item) {
				Intent intent = new Intent(PlaceType.this, UserForm.class);
				intent.putExtra("mode", "update");
				intent.putExtra("userId", userId.toString());
				startActivity(intent);
				return true;
			}
		});
		return true;
	}

	class HTTPPlaceType extends HTTPTransfer {
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			// parse json data
			try {
				List<String> list = new ArrayList<String>();
				JSONArray jArray = new JSONArray(result);
				for (int i = 0; i < jArray.length(); i++) {
					JSONObject json_data = jArray.getJSONObject(i);

					Type type = new Type();
					type.setTypeId(json_data.getInt("typeId"));
					type.setTypeName(json_data.getString("typeName"));
					type.setTypeTotal(json_data.getInt("typeTotal"));
					list.add(type.getTypeName());
					typeList.add(type);
				}
				ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
						getApplicationContext(),
						android.R.layout.simple_spinner_item, list);
				dataAdapter
						.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spinnerPlaceType.setAdapter(dataAdapter);
			} catch (JSONException e) {
				Toast.makeText(getApplicationContext(),
						"Error parsing data " + e.toString(),
						Toast.LENGTH_SHORT).show();
			}

			if (!autoLocation) {
				progressBar.setVisibility(View.INVISIBLE);
				textViewPlaceType.setVisibility(View.VISIBLE);
				textViewRange.setVisibility(View.VISIBLE);
				spinnerPlaceType.setVisibility(View.VISIBLE);
				spinnerRange.setVisibility(View.VISIBLE);
				buttonFind.setVisibility(View.VISIBLE);
			}
		}
	}
}