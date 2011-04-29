package de.codekicker.app.android.widget;

import roboguice.config.AbstractAndroidModule;
import android.content.Context;
import android.view.LayoutInflater;

import com.google.inject.Provides;

import de.codekicker.app.android.R;

public class BindingModule extends AbstractAndroidModule {

	@Override
	protected void configure() {}
	
	@Provides
	private IQuestionsListAdapter provideQuestionsListAdapter(Context context, LayoutInflater layoutInflater) {
		return new QuestionsListAdapter(context, R.layout.questions_list_item, layoutInflater);
	}
}