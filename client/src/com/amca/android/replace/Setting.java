package com.amca.android.replace;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Setting {
	public boolean getBoolean(Context context, String key){
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		return preferences.getBoolean(key, false);
	}
	
	public String getString(Context context, String key){
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		return preferences.getString(key, null);
	}
}
