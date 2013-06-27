package com.amca.android.replace;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.amca.android.replace.gps.GPSTracker;
import com.amca.android.replace.http.HTTPTransfer;
import com.amca.android.replace.model.Type;
import com.amca.android.replace.place.PlaceSelector;
import com.amca.android.replace.place.PlaceSelectorMinimalist;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class PlaceTypeMinimalist extends Activity implements OnClickListener {
	
	private Integer userId;
	private String currentLat, currentLng;
	private boolean autoLocation;
	private List<Type> typeList = new ArrayList<Type>();
	private TextView textViewPlaceType, textViewRange;
	private Spinner spinnerPlaceType, spinnerRange;
	private Button buttonFind;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_place_type_minimalist);

		Intent intent = getIntent();
		this.userId = intent.getIntExtra("userId", 0);
		setTitle("Welcome");

		textViewPlaceType = (TextView) findViewById(R.id.textViewPlaceType);
		textViewRange = (TextView) findViewById(R.id.textViewRange);
		spinnerPlaceType = (Spinner) findViewById(R.id.spinnerPlaceType);
		spinnerRange = (Spinner) findViewById(R.id.spinnerRange);
		buttonFind = (Button) findViewById(R.id.buttonFind);
		buttonFind.setOnClickListener(this);
		textViewPlaceType.setVisibility(View.INVISIBLE);
		textViewRange.setVisibility(View.INVISIBLE);
		spinnerPlaceType.setVisibility(View.INVISIBLE);
		spinnerRange.setVisibility(View.INVISIBLE);
		buttonFind.setVisibility(View.INVISIBLE);

		Setting setting = new Setting();
		autoLocation = setting.getBoolean(PlaceTypeMinimalist.this, "autoLocation");

		if (autoLocation) {
			GPSTracker gps = new GPSTracker(PlaceTypeMinimalist.this);
			if (gps.canGetLocation()) {
				Double latitude = gps.getLatitude();
				Double longitude = gps.getLongitude();
				this.currentLat = latitude.toString();
				this.currentLng = longitude.toString();
				if (autoLocation) {
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
		http.setContext(PlaceTypeMinimalist.this);
		http.setMode(2);
		http.execute("type/listAll/");
	}

	@Override
	public void onClick(View v) {
		int position = spinnerPlaceType.getSelectedItemPosition();
		Type type = typeList.get(position);
		if (type.getTypeTotal() > 0) {
			Intent intent = new Intent(PlaceTypeMinimalist.this, PlaceSelectorMinimalist.class);
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
			Toast.makeText(PlaceTypeMinimalist.this,
					"no data available for " + type.getTypeName(),
					Toast.LENGTH_SHORT).show();
		}
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
				System.out.println("Error parsing data " + e.toString());
			}

			if (!autoLocation) {
				textViewPlaceType.setVisibility(View.VISIBLE);
				textViewRange.setVisibility(View.VISIBLE);
				spinnerPlaceType.setVisibility(View.VISIBLE);
				spinnerRange.setVisibility(View.VISIBLE);
				buttonFind.setVisibility(View.VISIBLE);
			}
		}
	}

}
