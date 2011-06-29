package de.codekicker.app.android.activity;

import roboguice.activity.RoboPreferenceActivity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.inject.Inject;

import de.codekicker.app.android.R;
import de.codekicker.app.android.business.IAuthenticator;
import de.codekicker.app.android.business.IAuthenticator.SuccessCallback;
import de.codekicker.app.android.preference.IPreferenceManager;

public class Preferences extends RoboPreferenceActivity {
	private static final String TAG = "PreferenceActivity";
	@Inject private IPreferenceManager preferenceManager;
	@Inject private IAuthenticator authenticator;
	
	public Preferences() {}
	
	/**
	 * This constructor is <strong>only</strong> for unit testing!
	 */
	public Preferences(IPreferenceManager preferenceManager, IAuthenticator authenticator) {
		this.preferenceManager = preferenceManager;
		this.authenticator = authenticator;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		// Username and Password will be automatically saved in global DefaultSharedPreferences
	}
	
	@Override
	public void onBackPressed() {
		final ProgressDialog dialog = ProgressDialog.show(this, "", getString(R.string.checkingCredentials));
		Log.v(TAG, "Back pressed. Now checking the entered credentials");
		String username = preferenceManager.getUsername();
		String password = preferenceManager.getPassword();
		authenticator.verifyCredentials(username, password, new SuccessCallback() {
			@Override
			public void authenticationDone(boolean credentialsValid) {
				Log.v(TAG, "Username/Password valid: " + credentialsValid);
				preferenceManager.setIsUserAuthenticated(credentialsValid);
				int resText = credentialsValid ? R.string.successfulLoggedIn : R.string.errorCredentials;
				dialog.dismiss();
				Toast.makeText(getApplicationContext(), getString(resText), Toast.LENGTH_LONG).show();
				Preferences.super.onBackPressed();
			}
		});
	}
}