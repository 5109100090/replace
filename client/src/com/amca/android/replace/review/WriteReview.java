package com.amca.android.replace.review;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.amca.android.replace.R;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

public class WriteReview extends SherlockActivity {

	private TextView textViewPointPrice, textViewPointService, textViewPointLocation, textViewPointCondition, textViewPointComfort;
	private SeekBar seekBarPointPrice, seekBarPointService,
			seekBarPointLocation, seekBarPointCondition, seekBarPointComfort;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_write_review);

		textViewPointPrice = (TextView) findViewById(R.id.textViewPointPrice);
		textViewPointService = (TextView) findViewById(R.id.textViewPointService);
		textViewPointLocation = (TextView) findViewById(R.id.textViewPointLocation);
		textViewPointCondition = (TextView) findViewById(R.id.textViewPointCondition);
		textViewPointComfort = (TextView) findViewById(R.id.textViewPointComfort);
		
		seekBarPointPrice = (SeekBar) findViewById(R.id.seekBarPointPrice);
		seekBarPointService = (SeekBar) findViewById(R.id.seekBarPointService);
		seekBarPointLocation = (SeekBar) findViewById(R.id.seekBarPointLocation);
		seekBarPointCondition = (SeekBar) findViewById(R.id.seekBarPointCondition);
		seekBarPointComfort = (SeekBar) findViewById(R.id.seekBarPointComfort);

		seekBarPointPrice.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {
						// TODO Auto-generated method stub
						String[] s = textViewPointPrice.getText().toString().split(" : ");
						textViewPointPrice.setText(s[0] + " : " + progress);
					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
						// TODO Auto-generated method stub
					}

					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
						// TODO Auto-generated method stub
					}
				});
		
		seekBarPointService.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar,
					int progress, boolean fromUser) {
				// TODO Auto-generated method stub
				String[] s = textViewPointService.getText().toString().split(" : ");
				textViewPointService.setText(s[0] + " : " + progress);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}
		});
		
		seekBarPointLocation.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar,
					int progress, boolean fromUser) {
				// TODO Auto-generated method stub
				String[] s = textViewPointLocation.getText().toString().split(" : ");
				textViewPointLocation.setText(s[0] + " : " + progress);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}
		});
		
		seekBarPointCondition.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar,
					int progress, boolean fromUser) {
				// TODO Auto-generated method stub
				String[] s = textViewPointCondition.getText().toString().split(" : ");
				textViewPointCondition.setText(s[0] + " : " + progress);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}
		});
		
		seekBarPointComfort.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar,
					int progress, boolean fromUser) {
				// TODO Auto-generated method stub
				String[] s = textViewPointComfort.getText().toString().split(" : ");
				textViewPointComfort.setText(s[0] + " : " + progress);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem menuSave = menu.add("Save");
		menuSave.setIcon(R.drawable.content_save);
		menuSave.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM
				| MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		menuSave.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(final MenuItem item) {
				return true;
			}
		});
		return true;
	}

}
