package com.milos.restosys;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.milos.restosys.beans.Bill;
import com.milos.restosys.beans.User;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class BillsActivity extends Activity implements OnClickListener {

	private ImageButton logOffButton;
	private ImageButton settingsButton;
	private LinearLayout billsContainer;
	
	private User user;
	private List<Bill> bills = new ArrayList<Bill>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bills);

		initLoginButton();
		initSettingsButton();
		billsContainer = (LinearLayout) findViewById(R.id.billsContainer);
		
		Bundle infos = getIntent().getExtras();
		try {
			user = new User(new JSONObject(infos.getString("user")));
			JSONArray billsJson = new JSONArray(infos.getString("bills"));
			bills.clear();
			for (int i = 0; i < billsJson.length(); i++) {
				Bill bill = new Bill(billsJson.getJSONObject(i));
				bills.add(bill);
				CheckBox billCheckBox = new CheckBox(this);
				billCheckBox.setText("Table (" + bill.getTotal() + ")");
				billsContainer.addView(billCheckBox);
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

	public void onClick(View v) {
		if (v == settingsButton) {
			Toast.makeText(this, R.string.comming_soon, Toast.LENGTH_LONG).show();
		} else if (v == logOffButton) {
			finish();
		}
		
	}



}
