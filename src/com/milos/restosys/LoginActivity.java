package com.milos.restosys;

import com.milos.restosys.preferences.LoginPreferences;
import com.milos.restosys.services.SocketListenerService;
import com.milos.restosys.tasks.MessageSender;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnClickListener {

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
		
		Intent listenerService = new Intent(getApplicationContext(), SocketListenerService.class);
		this.startService(listenerService);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_login, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.menu_settings:
	        	Intent prefIntent = new Intent(this, LoginPreferences.class);
	    		startActivityForResult(prefIntent, 0);
	            return true;
	        case R.id.menu_about:
	        	Toast.makeText(this, "Comming soon", Toast.LENGTH_SHORT).show();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	private Button initLoginButton() {
		if (loginButton == null) {
			loginButton = (Button) findViewById(R.id.buttonLogin);
			loginButton.setOnClickListener(this);
		}
		return loginButton;
	}

	private ImageButton initBarcodeLoginButton() {
		if (barcodeButton == null) {
			barcodeButton = (ImageButton) findViewById(R.id.barcodeButton);
			barcodeButton.setOnClickListener(this);
		}
		return barcodeButton;
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

	public void onClick(View v) {
		if (v == loginButton) {
			checkForUser(loginText.getText().toString());
		} else if (v == barcodeButton) {
			// TODO na ovu akciju se pokrece barcode reader za citanje konobarovog ID-a
			sendToServer(loginText.getText().toString());
		}
	}

	public void sendToServer(String message) {
		Intent broadcast = new Intent("notifier-broadcast");
		broadcast.putExtra("message-to-server", message);
		LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcast);
		Log.e("LoginActivity", "Broadcast sent to server: " + message);
	}
	
}
