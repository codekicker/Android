package de.codekicker.app.android.widget;

import roboguice.config.AbstractAndroidModule;
import android.content.Context;
import android.view.LayoutInflater;

import com.google.inject.Inject;
import com.google.inject.Provides;

import de.codekicker.app.android.R;
import de.codekicker.app.android.activity.QuestionDetails;
import de.codekicker.app.android.preference.IPreferenceManager;

public class BindingModule extends AbstractAndroidModule {

	@Override
	protected void configure() {}
	
	@Provides
	@Inject
	private IQuestionsListAdapter provideQuestionsListAdapter(Context context, LayoutInflater layoutInflater) {
		return new QuestionsListAdapter(context, R.layout.questions_list_item, layoutInflater);
	}
	
	@Provides
	@Inject
	private IQuestionDetailsAdapter provideQuestionDetailsAdapter(Context context, IPreferenceManager preferenceManager, LayoutInflater inflater) {
		return new QuestionDetailsAdapter((QuestionDetails) context, preferenceManager, R.layout.question_details_list_item, inflater);
	}
}