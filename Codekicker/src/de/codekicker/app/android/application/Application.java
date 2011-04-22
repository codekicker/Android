package de.codekicker.app.android.application;

import java.util.List;

import roboguice.application.RoboApplication;

import com.google.inject.Module;

public class Application extends RoboApplication {
	@Override
	protected void addApplicationModules(List<Module> modules) {
		modules.add(new de.codekicker.app.android.business.BindingModule());
		modules.add(new de.codekicker.app.android.preference.BindingModule());
	}
}