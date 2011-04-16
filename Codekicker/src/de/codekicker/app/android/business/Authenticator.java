package de.codekicker.app.android.business;

import android.content.Context;
import android.util.Log;
import de.codekicker.app.android.preference.PreferenceManager;

public class Authenticator {
	private static final String TAG = "Authenticator";
	private static final String URL = "TestAuthentication.json";
	private final Context context;
	
	public Authenticator(Context context) {
		this.context = context;
	}
	
	public boolean verify(String username, String password) {
		try {
			PreferenceManager preferenceManager = PreferenceManager.getInstance(context);
			ServerRequest serverRequest = new ServerRequest(preferenceManager.getAppIdKey(), preferenceManager.getAppId());
			// If no Exception is thrown, the authentication is valid. So we ignore the JSON result
			serverRequest.downloadJSON(preferenceManager.getApiBaseUrl() + URL, "", username, password);
			return true;
		} catch (Exception e) {
			Log.e(TAG, e.getMessage(), e);
			return false;
		}
	}
}