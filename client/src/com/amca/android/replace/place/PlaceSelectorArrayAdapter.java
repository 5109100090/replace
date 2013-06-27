package com.amca.android.replace.place;

import java.text.DecimalFormat;
import java.util.List;
import com.amca.android.replace.R;
import com.amca.android.replace.Setting;
import com.amca.android.replace.model.Place;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PlaceSelectorArrayAdapter extends ArrayAdapter<Place> {
	private int view;
	private final Context context;
	private final List<Place> values;

	public PlaceSelectorArrayAdapter(Context context, List<Place> values,
			int view) {
		super(context, view, values);

		this.context = context;
		this.values = values;
		this.view = view;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(view, parent, false);
		TextView placeName = (TextView) rowView.findViewById(R.id.placeName);
		TextView placeAddress = (TextView) rowView
				.findViewById(R.id.placeAddress);
		TextView placeDesc = (TextView) rowView.findViewById(R.id.placeDesc);
		TextView placePoint = (TextView) rowView.findViewById(R.id.placePoint);
		TextView placeDistance = (TextView) rowView
				.findViewById(R.id.placeDistance);

		DecimalFormat df = new DecimalFormat("#0.##");

		Place place = values.get(position);
		placeName.setText(place.getPlaceName());
		placeAddress.setText(place.getPlaceAddress());
		placeDesc.setText(place.getPlaceDesc());
		placePoint.setText(df.format(place.getAveragePoint()) + "/10");
		placeDistance.setText(place.getPlaceReviews() + " review(s) | "
				+ df.format(place.getPlaceDistance() / 1000) + " km");

		Setting setting = new Setting();
		if (!setting.getBoolean(getContext(), "minimalist")) {
			String serverUrl = setting.getString(getContext(), "serverUrl");

			ImageView placeIcon = (ImageView) rowView
					.findViewById(R.id.placeIcon);
			UrlImageViewHelper.setUrlDrawable(placeIcon, serverUrl
					+ "static/place/" + place.getPlaceId() + ".jpg",
					R.drawable.gif_loader);
		}

		return rowView;
	}
}
