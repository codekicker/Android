package de.codekicker.app.android.activity;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;
import de.codekicker.app.android.R;
import de.codekicker.app.android.business.Authenticator;

public class Preferences extends PreferenceActivity {
	private static final String TAG = "PreferenceActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.layout.preferences);
	}
	
	@Override
	public void onBackPressed() {
		Log.v(TAG, "Back pressed. Now checking the entered credentials");
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		String username = preferences.getString("username", "");
		String password = preferences.getString("password", "");
		new CredentialsChecker().execute(username, password);
		super.onBackPressed();
	}
	
	private class CredentialsChecker extends AsyncTask<String, Void, Boolean> {
		@Override
		protected Boolean doInBackground(String... params) {
			Authenticator authenticator = new Authenticator();
			boolean valid = authenticator.verify(params[0], params[1]);
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Preferences.this);
			Editor editor = preferences.edit();
			editor.putBoolean("UserAuthenticated", valid);
			return valid;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			int resText = result ? R.string.successfulLoggedIn : R.string.errorCredentials;
			Toast.makeText(getApplicationContext(), getString(resText), Toast.LENGTH_LONG).show();
		}
	}
}