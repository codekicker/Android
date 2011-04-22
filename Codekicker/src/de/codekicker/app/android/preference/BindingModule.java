package de.codekicker.app.android.preference;

import roboguice.config.AbstractAndroidModule;

public class BindingModule extends AbstractAndroidModule {

	@Override
	protected void configure() {
		bind(IPreferenceManager.class).to(PreferenceManager.class).asEagerSingleton();
	}
}