package com.amca.android.replace;

import java.text.DecimalFormat;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PlaceSelectorArrayAdapter extends ArrayAdapter<String>{
	private final Context context;
	private final List<String> values;

	public PlaceSelectorArrayAdapter(Context context, List<String> values) {
		super(context, R.layout.activity_place_selector_row, values);
		this.context = context;
		this.values = values;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.activity_place_selector_row, parent, false);
		TextView place_name = (TextView) rowView.findViewById(R.id.place_name);
		TextView place_desc = (TextView) rowView.findViewById(R.id.place_desc);
		TextView place_rating = (TextView) rowView.findViewById(R.id.place_rating);
		TextView place_distance = (TextView) rowView.findViewById(R.id.place_distance);
		
		DecimalFormat df = new DecimalFormat("#.00");
		
		String[] value = values.get(position).split("#");
		ImageView imageView = (ImageView) rowView.findViewById(R.id.place_icon);
		place_name.setText(value[0]);
		place_desc.setText(value[1]);
		place_rating.setText(value[2] + " review(s)");
		place_distance.setText( df.format(Float.parseFloat(value[3]) / 1000) + " km");
		// Change the icon for Windows and iPhone
		String s = values.get(position);
		imageView.setImageResource(R.drawable.place_icon);

		return rowView;
	}
}
