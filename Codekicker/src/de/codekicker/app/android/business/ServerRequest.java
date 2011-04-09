package de.codekicker.app.android.business;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.util.Base64;
import android.util.Log;
import de.codekicker.app.android.config.ConfigManager;
import de.codekicker.app.android.config.NotConfiguredException;

public class ServerRequest {
	private static final String TAG = "ServerRequest";
	private final ConfigManager configManager;
	
	public ServerRequest(Context context) {
		configManager = new ConfigManager(context);
		if (!configManager.isConfigured()) {
			configManager.configure();
		}
	}
	
	public String downloadJSON(String url, String postParameters) throws IOException, NotConfiguredException {
		Log.v(TAG, "Downloading JSON String");
		return makeRequest(url, postParameters, null, null);
	}
	
	public String downloadJSON(String url, String postParameters, String username, String password) throws IOException, NotConfiguredException {
		Log.v(TAG, "Downloading JSON String");
		return makeRequest(url, postParameters, username, password);
	}
	
	public String send(String url, String postParameters) throws IOException, NotConfiguredException {
		Log.v(TAG, "Sending data");
		return makeRequest(url, postParameters, null, null);
	}
	
	public String send(String url, String postParameters, String username, String password) throws IOException, NotConfiguredException {
		Log.v(TAG, "Sending data");
		return makeRequest(url, postParameters, username, password);
	}
	
	private String makeRequest(String spec, String postParameters, String username, String password) throws IOException, NotConfiguredException {
		BufferedReader bufferedReader = null;
		DataOutputStream dataOutputStream = null;
		try {
			byte[] params = postParameters.getBytes();
			URL url = new URL(spec);
			HttpURLConnection httpUrlConnection = (HttpURLConnection) url.openConnection();
			httpUrlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			httpUrlConnection.setRequestProperty("Content-Length", Integer.toString(params.length));
			httpUrlConnection.setRequestProperty(configManager.getAppIdKey(), configManager.getAppId());
			httpUrlConnection.setRequestMethod("POST");
			if (username != null && password != null) {
				String authorizationString = (username + ":" + password);
				authorizationString = Base64.encodeToString(authorizationString.getBytes(), Base64.NO_WRAP);
				httpUrlConnection.setRequestProperty("Authorization", "Basic " + authorizationString);
			}
			httpUrlConnection.setDoOutput(true);
			dataOutputStream = new DataOutputStream(httpUrlConnection.getOutputStream());
			dataOutputStream.write(params, 0, params.length);
			dataOutputStream.flush();
			InputStreamReader inputStreamReader = new InputStreamReader(httpUrlConnection.getInputStream());
			bufferedReader = new BufferedReader(inputStreamReader);
			StringBuilder stringBuilder = new StringBuilder();
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				stringBuilder.append(line);
			}
			String json = stringBuilder.toString();
			Log.v(TAG, json);
			return json;
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
					dataOutputStream.close();
				} catch (IOException e) {
					Log.e(TAG, e.getMessage(), e);
				}
			}
		}
	}
}