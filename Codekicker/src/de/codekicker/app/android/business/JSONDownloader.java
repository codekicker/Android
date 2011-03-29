package de.codekicker.app.android.business;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.util.Log;

public class JSONDownloader {
	private static final String TAG = "JSONDownloader";

	public String downloadJSON(String spec, byte[] postParameters) throws MalformedURLException, IOException {
		Log.v(TAG, "Downloading JSON String");
		BufferedReader bufferedReader = null;
		DataOutputStream dataOutputStream = null;
		try {
			URL url = new URL(spec);
			HttpURLConnection httpUrlConnection = (HttpURLConnection) url.openConnection();
			httpUrlConnection.setRequestMethod("POST");
			httpUrlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			httpUrlConnection.setRequestProperty("Content-Length", Integer.toString(postParameters.length));
			httpUrlConnection.setRequestProperty("CK-App-ID", "codekicker.official-app.v1.0");
			httpUrlConnection.setDoOutput(true);
			dataOutputStream = new DataOutputStream(httpUrlConnection.getOutputStream());
			dataOutputStream.write(postParameters, 0, postParameters.length);
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