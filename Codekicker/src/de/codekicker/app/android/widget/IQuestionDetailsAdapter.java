package de.codekicker.app.android.widget;

import android.widget.ExpandableListAdapter;
import de.codekicker.app.android.model.Answer;

public interface IQuestionDetailsAdapter extends ExpandableListAdapter {
	void notifyDataSetChanged();
	void add(Answer answer);
	void clear();
}