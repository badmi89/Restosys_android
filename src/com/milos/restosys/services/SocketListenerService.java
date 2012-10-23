package com.milos.restosys.services;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.milos.restosys.BillsActivity;
import com.milos.restosys.beans.Bill;
import com.milos.restosys.beans.User;
import com.milos.restosys.utils.Key;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

public class SocketListenerService extends Service {

	public static final String TAG = "SocketListenerService";

	private Thread listener;
	private ServerSocket server;
	private Socket socket;
	
	public SharedPreferences prefs;
	private String sessionId;
	
	private BroadcastReceiver notifier = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.e("BroadcastReceiver", "Broadcast received message");
			Bundle extras = intent.getExtras();
			if (extras != null) {
				String not = extras.getString("notification-message");
				if (not != null) {
					Toast.makeText(context, not, Toast.LENGTH_LONG).show();
					Log.e("BroadcastReceiver", "Toasted: " + not);
				} else {
					Log.e("BroadcastReceiver", "notification-message was empty");
				}
				
				String messageToSend = extras.getString("message-to-server");
				if (messageToSend != null) {
					JSONObject json;
					try {
						json = new JSONObject(messageToSend);
						new MessageSender().execute(json);
					} catch (JSONException e) {
						Log.e(TAG, "JSON format not valid");
						Toast.makeText(context, "JSON format not valid", Toast.LENGTH_LONG).show();
					}
				}
			}
		}
	};
	
	private BroadcastReceiver serverSlave = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context arg0, Intent arg1) {
			Bundle extras = arg1.getExtras();
			if(extras != null) {
				try {
					JSONObject receiver = new JSONObject(extras.getString("received-from-server"));
					String action = receiver.getString("ACTION");
					if(action.equals(Key.SERVER_LOGIN_APPROVE)) {
						sessionId = receiver.getString("SESSIONID");
						JSONObject user = receiver.getJSONObject("USER");
						JSONArray billsJson = receiver.getJSONArray("BILLS");
						
						Intent billsActivity = new Intent(arg0, BillsActivity.class);
						billsActivity.putExtra("user", user.toString());
						billsActivity.putExtra("bills", billsJson.toString());
						billsActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						
						startActivity(billsActivity);
						
					}
					
					if(action.equals(Key.SERVER_LOGIN_REJECTED)) {
						Toast.makeText(arg0, "User doesn't exist", Toast.LENGTH_LONG).show();
					}
					
				} catch (JSONException e) {
					Log.e(TAG, "Received invalid JSON format, ignoring...");
				}
			}
		}
	};
	

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(notifier, new IntentFilter("notifier-broadcast"));
		LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(serverSlave, new IntentFilter("slave-broadcast"));

		try {
			server = new ServerSocket(4343);
		} catch (IOException e) {
			e.printStackTrace();
		}
		listener = new Thread(new Runnable() {

			public void run() {

				while (true) {
					try {
						Log.e(TAG, "Inside RUN");
						socket = server.accept();
						BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
						String message = in.readLine();
//						sendToNotifier(message);
						sendToSlave(message);
						Log.e(TAG, "Received: " + message);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});

		listener.start();

		Toast.makeText(this, "Server Listener started", Toast.LENGTH_SHORT).show();
		Log.e(TAG, "Server Listener started");
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		Toast.makeText(this, "Server Listener stoped", Toast.LENGTH_SHORT).show();
		Log.e(TAG, "Server listener stoped!");
		JSONObject json = new JSONObject();
		try {
			if (sessionId==null) sessionId = "";
			json.put(Key.HOST_CHECKOUT, sessionId);
			sendToServer(json.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		super.onDestroy();
	}

	public void sendToNotifier(String message) {
		Intent broadcast = new Intent("notifier-broadcast");
		broadcast.putExtra("notification-message", message);
		LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcast);
		Log.e(TAG, "Broadcast sent to notifier: " + message);
	}
	
	public void sendToServer(String message) {
		Intent broadcast = new Intent("notifier-broadcast");
		broadcast.putExtra("message-to-server", message);
		LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcast);
		Log.e(TAG, "Broadcast sent to server: " + message);
	}
	
	public void sendToSlave(String message) {
		
		Intent broadcast = new Intent("slave-broadcast");
		broadcast.putExtra("received-from-server", message);
		LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcast);
		Log.e(TAG, "Broadcast sent to slave: " + message);
	}
	

	public class MessageSender extends AsyncTask<JSONObject, Void, Socket> {

		private String serverIp = prefs.getString("server_ip", "192.168.1.1");
		private int serverPort = Integer.parseInt(prefs.getString("server_port", "3434"));
		
		private void send(JSONObject json) {
			try {
				Socket socket = new Socket(serverIp, serverPort);
				DataOutputStream out = new DataOutputStream(socket.getOutputStream());
				out.writeBytes(json.toString() + "\n");
				out.close();
				socket.close();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		protected Socket doInBackground(JSONObject... params) {
			try {
				for (int i = 0; i < params.length; i++) {
					send(params[i]);
				}
			} catch (NumberFormatException e) {
				System.err.println("Specified port format is invalid!");
				e.printStackTrace();
			}

			return null;
		}
	}

}
