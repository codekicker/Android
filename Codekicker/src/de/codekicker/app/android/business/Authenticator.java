package de.codekicker.app.android.business;

import org.json.JSONObject;

import com.google.inject.Inject;

import roboguice.util.RoboAsyncTask;
import android.util.Log;
import de.codekicker.app.android.preference.IPreferenceManager;

class Authenticator extends RoboAsyncTask<Boolean> implements IAuthenticator {	
	private static final String TAG = Authenticator.class.getSimpleName();
	private static final String URL = "TestAuthentication.json";
	private final IPreferenceManager preferenceManager;
	private final IServerRequest serverRequest;
	private String username, password;
	private SuccessCallback callback;
	
	@Inject
	public Authenticator(IPreferenceManager preferenceManager, IServerRequest serverRequest) {
		this.preferenceManager = preferenceManager;
		this.serverRequest = serverRequest;
	}
	
	@Override
	public void verifyCredentials(String username, String password, SuccessCallback callback) {
		this.username = username;
		this.password = password;
		this.callback = callback;
		execute();
	}
	
	@Override
	public Boolean call() throws Exception {
		try {
			// Could look like {"ErrorCode":"ClientFault.AuthenticationFailed","ErrorMessage":null}
			String json = serverRequest.downloadJSON(preferenceManager.getApiBaseUrl() + URL, "", username, password);
			JSONObject jsonObject = new JSONObject(json);
			String errorCode = jsonObject.getString("ErrorCode");
			return errorCode == null || !errorCode.contains("AuthenticationFailed");
		} catch (Exception e) {
			// If Exception is thrown => not valid
			Log.e(TAG, e.getMessage(), e);
			return false;
		}
	}
	
	@Override
	protected void onSuccess(Boolean credentialsValid) throws Exception {
		callback.authenticationDone(credentialsValid);
		super.onSuccess(credentialsValid);
	}
}