package de.codekicker.app.android.widget;

import roboguice.config.AbstractAndroidModule;

public class BindingModule extends AbstractAndroidModule {

	@Override
	protected void configure() {
		bind(IQuestionsListAdapter.class).to(QuestionsListAdapter.class);
	}
}