package com.amca.android.replace.place;

import java.text.DecimalFormat;
import java.util.List;
import com.amca.android.replace.R;
import com.amca.android.replace.model.Place;
import com.fedorvlasov.lazylist.ImageLoader;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PlaceSelectorArrayAdapter extends
		ArrayAdapter<Place> {
	private final Context context;
	private final List<Place> values;

	public PlaceSelectorArrayAdapter(Context context,
			List<Place> values) {
		super(context, R.layout.activity_place_selector_row, values);
		this.context = context;
		this.values = values;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.activity_place_selector_row,
				parent, false);
		TextView placeName = (TextView) rowView.findViewById(R.id.placeName);
		TextView placeAddress = (TextView) rowView.findViewById(R.id.placeAddress);
		TextView placeDesc = (TextView) rowView.findViewById(R.id.placeDesc);
		TextView placePoint = (TextView) rowView
				.findViewById(R.id.placePoint);
		TextView placeDistance = (TextView) rowView
				.findViewById(R.id.placeDistance);
		ImageView placeIcon = (ImageView) rowView.findViewById(R.id.placeIcon);
		
		DecimalFormat df = new DecimalFormat("#0.##");

		Place place = values.get(position);
		placeName.setText(place.getPlaceName());
		placeAddress.setText(place.getPlaceAddress());
		placeDesc.setText(place.getPlaceDesc());
		placePoint.setText(df.format(place.getAveragePoint()));
		placeDistance.setText(place.getPlaceReviews() + " review(s) | " + df.format(place.getPlaceDistance() / 1000) + " km");
		
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
		String serverUrl = preferences.getString("serverUrl", "");
		ImageLoader imageLoader = new ImageLoader(context);
		imageLoader.DisplayImage(serverUrl + "static/place/" + place.getPlaceId() + ".jpg", placeIcon);

		return rowView;
	}
}
