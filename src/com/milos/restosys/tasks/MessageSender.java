package com.milos.restosys.tasks;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
	
	public class MessageSender extends AsyncTask<String, Void, Socket> {
		
		private String serverIp = "192.168.1.78";
		private int serverPort = 3434;
		
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
