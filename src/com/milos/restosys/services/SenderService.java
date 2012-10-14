package com.milos.restosys.services;

import android.app.IntentService;
import android.content.Intent;

public class SenderService extends IntentService {

	public SenderService() {
		super("Sender service");
	}

	@Override
	protected void onHandleIntent(Intent arg0) {
		String message = arg0.getExtras().getString("message-to-server");
	}

}
