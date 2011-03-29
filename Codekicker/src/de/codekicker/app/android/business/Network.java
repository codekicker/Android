package de.codekicker.app.android.business;

import android.content.Context;
import android.net.ConnectivityManager;

public class Network {
	private final Context context;
	
	public Network(Context context) {
		this.context = context;
	}
	
	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		return cm.getActiveNetworkInfo().isConnected();
	}
}