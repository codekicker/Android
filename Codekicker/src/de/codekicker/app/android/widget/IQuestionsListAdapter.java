package de.codekicker.app.android.widget;

import android.widget.ListAdapter;
import de.codekicker.app.android.model.Question;

public interface IQuestionsListAdapter extends ListAdapter {
	void setNotifyOnChange(boolean notifyOnChange);
	void notifyDataSetChanged();
	void add(Question question);
	void clear();
}