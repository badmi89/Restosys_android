package com.milos.restosys;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.milos.restosys.beans.Bill;
import com.milos.restosys.beans.User;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class BillsActivity extends Activity implements OnClickListener {

	private ImageButton logOffButton;
	private ImageButton settingsButton;
	private Button testNetworkButton;
	
	private User user;
	private List<Bill> bills = new ArrayList<Bill>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bills);

		initLoginButton();
		initSettingsButton();
		initTestNetworkButton();
		
		Bundle infos = getIntent().getExtras();
		try {
			user = new User(new JSONObject(infos.getString("user")));
			JSONArray billsJson = new JSONArray(infos.getString("bills"));
			bills.clear();
			for (int i = 0; i < billsJson.length(); i++) {
				bills.add(new Bill(billsJson.getJSONObject(i)));
			}
			Toast.makeText(this, "Welcome " + user.getFirstname() + " " + user.getLastname(), Toast.LENGTH_SHORT).show();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_bills, menu);
		return true;
	}
	
	private ImageButton initLoginButton() {
		if (logOffButton == null) {
			logOffButton = (ImageButton) findViewById(R.id.powerOffButton);
			logOffButton.setOnClickListener(this);
		}
		return logOffButton;
	}

	private ImageButton initSettingsButton() {
		if (settingsButton == null) {
			settingsButton = (ImageButton) findViewById(R.id.settingButton);
			settingsButton.setOnClickListener(this);
		}
		return settingsButton;
	}

	private Button initTestNetworkButton() {
		if (testNetworkButton == null) {
			testNetworkButton = (Button) findViewById(R.id.testNetworkButton);
			testNetworkButton.setOnClickListener(this);
		}
		return testNetworkButton;
	}

	public void onClick(View v) {
		if (v == settingsButton) {
			Toast.makeText(this, R.string.comming_soon, Toast.LENGTH_LONG)
					.show();
		} else if (v == logOffButton) {
			finish();
		} else if (v == testNetworkButton) {
			ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
			if (networkInfo != null && networkInfo.isConnected()) {
				testNetworkButton.setText("Connected!");
			} else {
				testNetworkButton.setText("Network unavailable!");
			}
		}
		
	}
}
