package de.codekicker.app.android.business;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.inject.Inject;

class Network implements INetwork {
	private final ConnectivityManager connectivityManager;
	
	@Inject
	public Network(ConnectivityManager connectivityManager) {
		this.connectivityManager = connectivityManager;
	}
	
	@Override
	public boolean isOnline() {
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		return networkInfo != null && networkInfo.isConnected();
	}
}