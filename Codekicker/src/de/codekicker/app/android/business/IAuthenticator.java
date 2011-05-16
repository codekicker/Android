package de.codekicker.app.android.business;

public interface IAuthenticator {
	public interface SuccessCallback {
		void authenticationDone(boolean credentialsValid);
	};
	void verifyCredentials(String username, String password, SuccessCallback callback);
}