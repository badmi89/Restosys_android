package com.milos.restosys.preferences;

import com.milos.restosys.R;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceFragment;

public class LoginPreferences extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getFragmentManager().beginTransaction()
		.replace(android.R.id.content, new Preferences())
		.commit();
	}
	
	private class Preferences extends PreferenceFragment {
		@Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        addPreferencesFromResource(R.xml.login_preference);
	    }
	}

}
