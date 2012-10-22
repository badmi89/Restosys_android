package com.milos.restosys.services;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import com.milos.restosys.BillsActivity;
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
					new MessageSender().execute(messageToSend);
				}
			}
		}
	};
	
	private BroadcastReceiver serverSlave = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context arg0, Intent arg1) {
			Bundle extras = arg1.getExtras();
			if(extras != null) {
				String command = extras.getString("server-command");
				String value = extras.getString("server-value");
				Log.e(TAG, "INSIDE SLAVE: " + command + " " + value);
				
				if(command!=null && value!=null) {
					if(command.equals(Key.SERVER_LOGIN_APPROVE)) {
						Intent bills = new Intent();
						bills.setClass(arg0, BillsActivity.class);
						bills.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						arg0.startActivity(bills);
						Toast.makeText(arg0, "Welcome " + value, Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(arg0, value, Toast.LENGTH_LONG).show();
					}
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
		sendToServer(Key.HOST_CHECKIN + ":@:NA");
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		Toast.makeText(this, "Server Listener stoped", Toast.LENGTH_SHORT).show();
		Log.e(TAG, "Server listener stoped!");
		sendToServer(Key.HOST_CHECKOUT + ":@:NA");
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
		String command = message.split(":@:")[0];
		String value = message.split(":@:")[1];
		
		Log.e(TAG, "MESSAGE: " + message);
		Log.e(TAG, "COMMAND: " + command);
		Log.e(TAG, "VALUE: " + value);
		
		Intent broadcast = new Intent("slave-broadcast");
		broadcast.putExtra("server-command", command);
		broadcast.putExtra("server-value", value);
		LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcast);
		Log.e(TAG, "Broadcast sent to slave: " + message);
	}
	

	public class MessageSender extends AsyncTask<String, Void, Socket> {

		private String serverIp = prefs.getString("server_ip", "192.168.1.1");
		private int serverPort = Integer.parseInt(prefs.getString("server_port", "3434"));

		private void send(String message) {
			try {
				Socket socket = new Socket(serverIp, serverPort);
				DataOutputStream out = new DataOutputStream(socket.getOutputStream());
				out.writeBytes(message + "\n");
				out.close();
				socket.close();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		protected Socket doInBackground(String... params) {
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
