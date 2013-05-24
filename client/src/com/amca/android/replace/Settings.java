package com.amca.android.replace;
import android.preference.PreferenceActivity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class Settings extends PreferenceActivity {

	private Spinner spinnerServer, spinnerGeolocation;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
	}
	
	private void setSelection(){
		String serverUrl = this.getPref("serverUrl");
		String geolocation = this.getPref("geolocation");
		
		ArrayAdapter<?> adapterServerUrl = (ArrayAdapter<?>) spinnerServer.getAdapter();
	    for (int position = 0; position < adapterServerUrl.getCount(); position++)
	    {
	        if(spinnerServer.getItemAtPosition(position).toString().equals(serverUrl))
	        {
	        	spinnerServer.setSelection(position);
	            break;
	        }
	    }
	    
	    ArrayAdapter<?> adapterGeolocation = (ArrayAdapter<?>) spinnerGeolocation.getAdapter();
	    for (int position = 0; position < adapterGeolocation.getCount(); position++)
	    {
	        if(spinnerGeolocation.getItemAtPosition(position).toString().equals(geolocation))
	        {
	        	spinnerGeolocation.setSelection(position);
	            break;
	        }
	    }
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
