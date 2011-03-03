package de.codekicker.app.android.widget;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import de.codekicker.app.android.R;
import de.codekicker.app.android.model.Question;

public class QuestionsListAdapter extends ArrayAdapter<Question> {
	private final Context context;
	private final int listItemResourceId;
	private final List<Question> questions;
	
	public QuestionsListAdapter(Context context, int listItemResourceId) {
		this(context, listItemResourceId, new ArrayList<Question>());
	}

	public QuestionsListAdapter(Context context, int listItemResourceId, List<Question> questions) {
		super(context, listItemResourceId, questions);
		this.context = context;
		this.listItemResourceId = listItemResourceId;
		this.questions = questions;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Resources resources = context.getResources();
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View listItemView = convertView;
		if (listItemView == null) {
			listItemView = inflater.inflate(listItemResourceId, null);
		}
		Question question = questions.get(position);
		TextView headlineTextView = (TextView) listItemView.findViewById(R.id.headlineTextView);
		TextView questionTextView = (TextView) listItemView.findViewById(R.id.questionTextView);
		TextView timeTextView = (TextView) listItemView.findViewById(R.id.timeTextView);
		LinearLayout tagsLinearLayout = (LinearLayout) listItemView.findViewById(R.id.tagsLinearLayout);
		tagsLinearLayout.removeAllViews();
		for (String tag : question.getTags()) {
			FrameLayout tagFrameLayout = (FrameLayout) inflater.inflate(R.layout.questions_list_tag, null);
			TextView tagTextView = (TextView) tagFrameLayout.getChildAt(0);
			tagTextView.setText(tag);
			tagsLinearLayout.addView(tagFrameLayout);
		}
		headlineTextView.setText(question.getHeadline());
		questionTextView.setText(question.getQuestion().substring(0, 170));
		timeTextView.setText(String.format(resources.getString(R.string.time), "15", "m"));
		return listItemView;
	}
}