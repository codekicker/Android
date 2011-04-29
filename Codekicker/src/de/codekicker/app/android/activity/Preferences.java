package de.codekicker.app.android.activity;

import roboguice.activity.RoboPreferenceActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.inject.Inject;

import de.codekicker.app.android.R;
import de.codekicker.app.android.business.IAuthenticator;
import de.codekicker.app.android.preference.IPreferenceManager;

public class Preferences extends RoboPreferenceActivity {
	private static final String TAG = "PreferenceActivity";
	@Inject private IPreferenceManager preferenceManager;
	@Inject private IAuthenticator authenticator;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.layout.preferences);
		// Username and Password will be automatically saved in global DefaultSharedPreferences
	}
	
	@Override
	public void onBackPressed() {
		Log.v(TAG, "Back pressed. Now checking the entered credentials");
		String username = preferenceManager.getUsername();
		String password = preferenceManager.getPassword();
		new CredentialsChecker().execute(username, password);
		super.onBackPressed();
	}
	
	private class CredentialsChecker extends AsyncTask<String, Void, Boolean> {
		@Override
		protected Boolean doInBackground(String... params) {
			boolean valid = authenticator.verify(params[0], params[1]);
			Log.v(TAG, "Username/Password valid: " + valid);
			preferenceManager.setIsUserAuthenticated(valid);
			return valid;
		}
		
		@Override
		protected void onPostExecute(Boolean credentialsValid) {
			int resText = credentialsValid ? R.string.successfulLoggedIn : R.string.errorCredentials;
			Toast.makeText(getApplicationContext(), getString(resText), Toast.LENGTH_LONG).show();
		}
	}
}