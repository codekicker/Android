package de.codekicker.app.android.preference;

public interface IPreferenceManager {
	public abstract String getApiBaseUrl();

	public abstract String getAppIdKey();

	public abstract String getAppId();

	public abstract String getUsername();

	public abstract String getPassword();

	public abstract boolean isUserAuthenticated();

	public abstract void setIsUserAuthenticated(boolean value);
}