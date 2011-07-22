package de.codekicker.app.android.preference;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.inject.Inject;

class PreferenceManager implements IPreferenceManager {
	private final SharedPreferences sharedPreferences;
	
	@Inject
	public PreferenceManager(SharedPreferences sharedPreferences) {
		this.sharedPreferences = sharedPreferences;
	}
	
	@Override
	public String getApiBaseUrl() {
		return "http://codekicker.de/api/v1/";
	}
	
	@Override
	public String getAppIdKey() {
		return "CK-App-ID";
	}
	
	@Override
	public String getAppId() {
		return "codekicker.official-app.v1.0";
	}
	
	@Override
	public String getUsername() {
		return sharedPreferences.getString("username", "");
	}
	
	@Override
	public String getPassword() {
		return sharedPreferences.getString("password", "");
	}
	
	@Override
	public boolean isUserAuthenticated() {
		return sharedPreferences.getBoolean("UserAuthenticated", false);
	}
	
	@Override
	public void setIsUserAuthenticated(boolean value) {
		Editor editor = sharedPreferences.edit();
		editor.putBoolean("UserAuthenticated", value);
		editor.commit();
	}
}