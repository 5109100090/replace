package com.amca.android.replace;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Settings extends Activity implements OnClickListener {

	private EditText serverUrl;
	private Button buttonSetting;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		serverUrl = (EditText) findViewById(R.id.serverUrl);
		serverUrl.setText(this.getPref("serverUrl"));
		
		buttonSetting = (Button) findViewById(R.id.buttonSetting);
		buttonSetting.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		this.setPref("serverUrl", serverUrl.getText().toString());
		Toast.makeText(getApplicationContext(), "settings saved", Toast.LENGTH_SHORT).show();
	}
	
	private void setPref(String key, String value) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		Editor edit = preferences.edit();
		edit.putString(key, value);
		edit.commit();
	}
	
	private String getPref(String key) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		return preferences.getString(key, null);
	}
}
