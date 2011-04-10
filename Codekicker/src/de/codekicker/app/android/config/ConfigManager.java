package de.codekicker.app.android.config;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class ConfigManager {
	private static ConfigManager instance = new ConfigManager();
	private static final String SHARED_PREFERENCE_NAME = "GlobalConfiguration";
	private static SharedPreferences sharedPreferences;
	private static boolean isConfigured;
	
	private ConfigManager() {}
	
	public static ConfigManager getInstance(Context context) {
		if (!isConfigured)
			sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, context.MODE_PRIVATE);
		return instance;
	}
	
	public void configure() {
		Editor editor = sharedPreferences.edit();
		editor.putString("apiBaseUrl", "http://codekicker.de/api/v1/");
		editor.putString("appIdKey", "CK-App-ID");
		editor.putString("appId", "codekicker.official-app.v1.0");
		editor.commit();
		isConfigured = true;
	}
	
	public boolean isConfigured() {
		return isConfigured;
	}
	
	public String getApiBaseUrl() throws NotConfiguredException {
		if (!isConfigured) {
			throw new NotConfiguredException("Call configure() first");
		}
		return sharedPreferences.getString("apiBaseUrl", "");
	}
	
	public String getAppIdKey() throws NotConfiguredException {
		if (!isConfigured) {
			throw new NotConfiguredException("Call configure() first");
		}
		return sharedPreferences.getString("appIdKey", "");
	}
	
	public String getAppId() throws NotConfiguredException {
		if (!isConfigured) {
			throw new NotConfiguredException("Call configure() first");
		}
		return sharedPreferences.getString("appId", "");
	}
}