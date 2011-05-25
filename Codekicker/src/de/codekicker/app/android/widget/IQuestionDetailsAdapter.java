package de.codekicker.app.android.widget;

import android.widget.ListAdapter;
import de.codekicker.app.android.model.Answer;

public interface IQuestionDetailsAdapter extends ListAdapter {
	void setNotifyOnChange(boolean notifyOnChange);
	void notifyDataSetChanged();
	void add(Answer answer);
	void clear();
}