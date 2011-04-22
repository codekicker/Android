package de.codekicker.app.android.business;

public interface IAuthenticator {
	boolean verify(String username, String password);
}