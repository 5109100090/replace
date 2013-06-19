package com.amca.android.replace.review;

import java.util.HashMap;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.amca.android.replace.R;
import com.amca.android.replace.http.HTTPTransfer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class WriteReview extends SherlockActivity {

	private Integer userId, placeId;
	private TextView textViewPointPrice, textViewPointService,
			textViewPointLocation, textViewPointCondition,
			textViewPointComfort;
	private SeekBar seekBarPointPrice, seekBarPointService,
			seekBarPointLocation, seekBarPointCondition, seekBarPointComfort;
	private EditText editTextReviewText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_write_review);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		setSupportProgressBarIndeterminateVisibility(false);

		Intent intent = getIntent();
		this.userId = intent.getIntExtra("userId", 0);
		this.placeId = intent.getIntExtra("placeId", 0);
		setTitle("Write Review");

		editTextReviewText = (EditText) findViewById(R.id.editTextReviewText);

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

		seekBarPointPrice
				.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {
						// TODO Auto-generated method stub
						String[] s = textViewPointPrice.getText().toString()
								.split(" : ");
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

		seekBarPointService
				.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {
						// TODO Auto-generated method stub
						String[] s = textViewPointService.getText().toString()
								.split(" : ");
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

		seekBarPointLocation
				.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {
						// TODO Auto-generated method stub
						String[] s = textViewPointLocation.getText().toString()
								.split(" : ");
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

		seekBarPointCondition
				.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {
						// TODO Auto-generated method stub
						String[] s = textViewPointCondition.getText()
								.toString().split(" : ");
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

		seekBarPointComfort
				.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {
						// TODO Auto-generated method stub
						String[] s = textViewPointComfort.getText().toString()
								.split(" : ");
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
		final MenuItem menuSave = menu.add("Save");
		menuSave.setIcon(R.drawable.content_save);
		menuSave.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM
				| MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		menuSave.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(final MenuItem item) {
				menuSave.setVisible(false);
				setSupportProgressBarIndeterminateVisibility(true);
				HashMap<String, String> data = new HashMap<String, String>();
				data.put("userId", userId.toString());
				data.put("placeId", placeId.toString());
				data.put("reviewPointPrice",
						String.valueOf(seekBarPointPrice.getProgress()));
				data.put("reviewPointService",
						String.valueOf(seekBarPointService.getProgress()));
				data.put("reviewPointLocation",
						String.valueOf(seekBarPointLocation.getProgress()));
				data.put("reviewPointCondition",
						String.valueOf(seekBarPointCondition.getProgress()));
				data.put("reviewPointComfort",
						String.valueOf(seekBarPointComfort.getProgress()));
				data.put("reviewText", editTextReviewText.getText().toString());

				HTTPPWriteReview http = new HTTPPWriteReview();
				http.setContext(WriteReview.this);
				http.setData(data);
				http.execute("review/write/");
				return true;
			}
		});
		return true;
	}

	class HTTPPWriteReview extends HTTPTransfer {
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			setSupportProgressBarIndeterminateVisibility(false);
			if (result.equals("OK")) {
				Toast.makeText(getApplicationContext(),
						"review saved", Toast.LENGTH_SHORT).show();
				finish();
			} else {
				Toast.makeText(getApplicationContext(),
						"review already exist", Toast.LENGTH_SHORT).show();
			}
		}
	}
}
