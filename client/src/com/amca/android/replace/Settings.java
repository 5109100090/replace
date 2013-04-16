package com.amca.android.replace;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

public class Settings extends Activity {

	private RadioButton radioButtonServer, radioButtonServer1, radioButtonServer2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		String serverUrl = this.getPref("serverUrl");
		radioButtonServer1 = (RadioButton) findViewById(R.id.server1);
		radioButtonServer2 = (RadioButton) findViewById(R.id.server2);
		radioButtonServer1.setChecked(serverUrl.equals(radioButtonServer1.getText()));
		radioButtonServer2.setChecked(serverUrl.equals(radioButtonServer2.getText()));
	}
	
	public void onRadioButtonClicked(View view) {
		radioButtonServer = (RadioButton) findViewById(view.getId());
		this.setPref("serverUrl", radioButtonServer.getText().toString());
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
