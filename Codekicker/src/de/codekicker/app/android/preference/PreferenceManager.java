package de.codekicker.app.android.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PreferenceManager {
	private static PreferenceManager instance = new PreferenceManager();
	private static SharedPreferences sharedPreferences;
	
	private PreferenceManager() {}
	
	public static PreferenceManager getInstance(Context context) {
		if (sharedPreferences == null) {
			sharedPreferences = android.preference.PreferenceManager.getDefaultSharedPreferences(context);
		}
		return instance;
	}
	
	public String getApiBaseUrl() {
		return "http://dev.codekicker.de/api/v1/";
	}
	
	public String getAppIdKey() {
		return "CK-App-ID";
	}
	
	public String getAppId() {
		return "codekicker.official-app.v1.0";
	}
	
	public String getUsername() {
		return sharedPreferences.getString("username", "");
	}
	
	public String getPassword() {
		return sharedPreferences.getString("password", "");
	}
	
	public boolean getIsUserAuthenticated() {
		return sharedPreferences.getBoolean("UserAuthenticated", false);
	}
	
	public void setIsUserAuthenticated(boolean value) {
		Editor editor = sharedPreferences.edit();
		editor.putBoolean("UserAuthenticated", value);
		editor.commit();
	}
}