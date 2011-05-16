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
		// Hack for PreferenceManager.getDefaultSharedPreferences(context)
		bindConstant().annotatedWith(SharedPreferencesName.class).to(packageName + "_preferences");
	}
}