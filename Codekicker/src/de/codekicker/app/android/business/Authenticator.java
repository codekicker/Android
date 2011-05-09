package de.codekicker.app.android.business;

import android.util.Log;

import com.google.inject.Inject;

import de.codekicker.app.android.preference.IPreferenceManager;

class Authenticator implements IAuthenticator {
	private static final String TAG = "Authenticator";
	private static final String URL = "TestAuthentication.json";
	@Inject IPreferenceManager preferenceManager;
	@Inject IServerRequest serverRequest;
	
	@Override
	public boolean verify(String username, String password) {
		try {
			// If no Exception is thrown, the authentication is valid. So we ignore the JSON result
			serverRequest.downloadJSON(preferenceManager.getApiBaseUrl() + URL, "", username, password);
			return true;
		} catch (Exception e) {
			Log.e(TAG, e.getMessage(), e);
			return false;
		}
	}
}