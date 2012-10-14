package com.milos.restosys;

import java.net.URI;
import java.net.URISyntaxException;

import de.roderick.weberknecht.WebSocket;
import de.roderick.weberknecht.WebSocketConnection;
import de.roderick.weberknecht.WebSocketEventHandler;
import de.roderick.weberknecht.WebSocketException;
import de.roderick.weberknecht.WebSocketMessage;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnTouchListener {

	private Button loginButton;
	private ImageButton barcodeButton;
	private EditText loginText;
	private TextView connectionStatusText;

	private WebSocket socket;

	private String serverIp = "192.168.1.87";
	private String serverPort = "8080";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		loginText = (EditText) findViewById(R.id.editTextLogin);
		connectionStatusText = (TextView) findViewById(R.id.connectionStatusText);
		initLoginButton();
		initBarcodeLoginButton();
		connectToServer();
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
			// TODO na ovu akciju se pokrece barcode reader za citanje konobarovog ID-a
			// Za testne svrhe sada sluzi da se posalje poruka serveru
			try {
				socket.send(loginText.getText().toString());
			} catch (WebSocketException e) {
				Toast.makeText(getBaseContext(), "Error sending message to server", Toast.LENGTH_LONG).show();
			}
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

	private void connectToServer() {
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			new SocketConnectorTask().execute(serverIp, serverPort);
		} else {
			connectionStatusText.setText("Network unavailable!");
		}
	}

	private class SocketConnectorTask extends
			AsyncTask<String, Void, WebSocket> {

		@Override
		protected WebSocket doInBackground(String... params) {
			try {
				socket = new WebSocketConnection(new URI("ws://" + params[0] + ":" + params[1] + "/server"));
				socket.setEventHandler(new WebSocketEventHandler() {

					public void onOpen() {
						try {
							socket.send("Cheking in!");
							Toast.makeText(getBaseContext(), "Connected to server!", Toast.LENGTH_LONG).show();
						} catch (WebSocketException e) {
							Toast.makeText(getBaseContext(), "Error sending message to server", Toast.LENGTH_LONG).show();
						}
					}

					public void onMessage(WebSocketMessage message) {
						Toast.makeText(getBaseContext(), "SERVER: " + message.getText(), Toast.LENGTH_LONG).show();
					}

					public void onClose() {
						try {
							socket.send("Checking out!");
							socket.close();
						} catch (WebSocketException e) {
							
						}
					}
				});
				socket.connect();
			} catch (WebSocketException e) {
				e.printStackTrace();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
			return null;
		}

	}
}
