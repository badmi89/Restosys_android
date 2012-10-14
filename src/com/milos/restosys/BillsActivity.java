package com.milos.restosys;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.Toast;

public class BillsActivity extends Activity implements OnTouchListener {
	
	private ImageButton logOffButton;
	private ImageButton settingsButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bills);
        
        sayHello();
        initLoginButton();
        initSettingsButton();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_bills, menu);
        return true;
    }
    
    private ImageButton initLoginButton() {
    	if (logOffButton == null) {
    		logOffButton = (ImageButton) findViewById(R.id.powerOffButton);
    		logOffButton.setOnTouchListener(this);
    	}
    	return logOffButton;
    }
    
    private ImageButton initSettingsButton() {
    	if (settingsButton == null) {
    		settingsButton = (ImageButton) findViewById(R.id.settingButton);
    		settingsButton.setOnTouchListener(this);
    	}
    	return settingsButton;
    }

	public boolean onTouch(View v, MotionEvent event) {
		if(v == settingsButton) {
			Toast.makeText(this, R.string.comming_soon, Toast.LENGTH_LONG).show();
		}
		else if (v == logOffButton) {
			finish();
		}
		
		return false;
	}
	
	private void sayHello() {
		String user = getIntent().getExtras().getString("passcode");
		Toast.makeText(this, "Hello " + user, Toast.LENGTH_SHORT).show();
	}
}
