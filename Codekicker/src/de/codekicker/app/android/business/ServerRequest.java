package de.codekicker.app.android.business;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Base64;
import android.util.Log;

import com.google.inject.Inject;

import de.codekicker.app.android.preference.IPreferenceManager;

class ServerRequest implements IServerRequest {
	private static final String TAG = ServerRequest.class.getSimpleName();
	private final String appIdKey;
	private final String appId;
	
	@Inject
	public ServerRequest(IPreferenceManager preferenceManager) {
		appIdKey = preferenceManager.getAppIdKey();
		appId = preferenceManager.getAppId();
	}
	
	@Override
	public String downloadJSON(String url, String postParameters) throws URISyntaxException, ClientProtocolException, IOException {
		Log.v(TAG, "Downloading JSON String from URL " + url);
		return makeRequest(url, postParameters, null, null);
	}
	
	@Override
	public String downloadJSON(String url, String postParameters, String username, String password) throws URISyntaxException, ClientProtocolException, IOException {
		Log.v(TAG, "Downloading JSON String from URL " + url);
		return makeRequest(url, postParameters, username, password);
	}
	
	@Override
	public String send(String url, String postParameters) throws URISyntaxException, ClientProtocolException, IOException {
		Log.v(TAG, "Sending data to URL " + url);
		return makeRequest(url, postParameters, null, null);
	}
	
	@Override
	public String send(String url, String postParameters, String username, String password) throws URISyntaxException, ClientProtocolException, IOException {
		Log.v(TAG, "Sending data to URL " + url);
		return makeRequest(url, postParameters, username, password);
	}
	
	private String makeRequest(String spec, String postParameters, String username, String password) throws URISyntaxException, ClientProtocolException, IOException {
		BufferedReader bufferedReader = null;
		try {
			URI uri = new URI(spec);
			byte[] params = postParameters.getBytes();
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(uri);
			httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
			httpPost.addHeader(appIdKey, appId);
			httpPost.setEntity(new ByteArrayEntity(params));
			if (username != null && password != null) {
				String auth = username + ":" + password;
				httpPost.addHeader("Authorization", "Basic " + Base64.encodeToString(auth.getBytes(), Base64.NO_WRAP));
				// TODO: Codekicker does not always return HTTP 401 so this doesn't work all the time
//				httpClient.getCredentialsProvider().setCredentials(
//						new AuthScope(uri.getHost(), uri.getPort()),
//						new UsernamePasswordCredentials(username, password));
			}
			HttpResponse response = httpClient.execute(httpPost);
			InputStreamReader inputStreamReader = new InputStreamReader(response.getEntity().getContent());
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
				} catch (IOException e) {
					Log.e(TAG, e.getMessage(), e);
				}
			}
		}
	}
}