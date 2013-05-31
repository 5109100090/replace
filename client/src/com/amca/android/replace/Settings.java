package com.amca.android.replace;

import com.actionbarsherlock.app.SherlockPreferenceActivity;
import android.os.Bundle;

public class Settings extends SherlockPreferenceActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
	}
}
