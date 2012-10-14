package com.milos.restosys;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class LoginActivity extends Activity {
	
	private Button loginButton;
	private ImageButton barcodeButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initLoginButton();
        initBarcodeLoginButton();
    }

    private Button initLoginButton() {
    	if (loginButton == null) {
    		loginButton = (Button) findViewById(R.id.buttonLogin);
    		loginButton.setOnTouchListener(new OnTouchListener() {
				
				public boolean onTouch(View v, MotionEvent event) {
					Intent billActivity = new Intent("com.milos.restosys.BILLS_ACTIVITY");
					startActivity(billActivity);
					finish();
					return true;
				}
			});
    	}
    	return loginButton;
    }
    
    private ImageButton initBarcodeLoginButton() {
    	if (barcodeButton == null) {
    		barcodeButton = (ImageButton) findViewById(R.id.barcodeButton);
    		barcodeButton.setOnTouchListener(new OnTouchListener() {
				
				public boolean onTouch(View v, MotionEvent event) {
					Toast.makeText(getApplicationContext(), R.string.comming_soon, Toast.LENGTH_LONG).show();
					return true;
				}
			});
    	}
    	return barcodeButton;
    }
}
