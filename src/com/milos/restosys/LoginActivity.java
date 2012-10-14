package com.milos.restosys;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnTouchListener {

	private Button loginButton;
	private ImageButton barcodeButton;
	private EditText loginText;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		loginText = (EditText) findViewById(R.id.editTextLogin);
		initLoginButton();
		initBarcodeLoginButton();
	}

	private Button initLoginButton() {
		if (loginButton == null) {
			loginButton = (Button) findViewById(R.id.buttonLogin);
			loginButton.setOnTouchListener(this);
		}
		return loginButton;
	}

	private ImageButton initBarcodeLoginButton() {
		if (barcodeButton == null) {
			barcodeButton = (ImageButton) findViewById(R.id.barcodeButton);
			barcodeButton.setOnTouchListener(this);
		}
		return barcodeButton;
	}

	public boolean onTouch(View v, MotionEvent event) {
		if (v == loginButton) {
			checkForUser(loginText.getText().toString());
		} else if (v == barcodeButton) {
			Toast.makeText(this, R.string.comming_soon, Toast.LENGTH_LONG).show();
		}
		return true;
	}
	
	private void checkForUser(String passcode) {
		// TODO here check in database for user
		// for now let it in every time
		if (passcode.equals("")) {
			passcode = "Stranger";
		}
		Intent billActivity = new Intent(this, BillsActivity.class);
		billActivity.putExtra("passcode", passcode);
		startActivity(billActivity);
		finish();
	}
}
