package de.codekicker.app.android.preference;

import roboguice.config.AbstractAndroidModule;
import roboguice.inject.SharedPreferencesName;

public class BindingModule extends AbstractAndroidModule {
	private final String packageName;
	
	public BindingModule(String packageName) {
		this.packageName = packageName;
	}
	
	@Override
	protected void configure() {
		bind(IPreferenceManager.class).to(PreferenceManager.class).asEagerSingleton();
		// Hack for PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		// A PreferenceActivity saves all fields in DefaultSharedPreferences. The ILocalConfig however
		// gets a "normal" SharedPreferences injected. With this line we make sure that ILocalConfig and
		// a PreferenceActivity read and write in the *same* SharedPreferences.
		bindConstant().annotatedWith(SharedPreferencesName.class).to(packageName + "_preferences");
	}
}