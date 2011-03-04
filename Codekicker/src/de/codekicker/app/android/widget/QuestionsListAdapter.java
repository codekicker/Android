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
		TextView textViewHeadline = (TextView) listItemView.findViewById(R.id.textViewHeadline);
		TextView textViewQuestion = (TextView) listItemView.findViewById(R.id.textViewQuestion);
		TextView textViewTime = (TextView) listItemView.findViewById(R.id.textViewTime);
		LinearLayout linearLayoutTags = (LinearLayout) listItemView.findViewById(R.id.linearLayoutTags);
		linearLayoutTags.removeAllViews();
		for (String tag : question.getTags()) {
			FrameLayout frameLayoutTag = (FrameLayout) inflater.inflate(R.layout.questions_list_tag, null);
			TextView textViewTag = (TextView) frameLayoutTag.getChildAt(0);
			textViewTag.setText(tag);
			linearLayoutTags.addView(frameLayoutTag);
		}
		textViewHeadline.setText(question.getHeadline());
		textViewQuestion.setText(question.getQuestion().substring(0, 170));
		textViewTime.setText(String.format(resources.getString(R.string.time), "15", "m"));
		return listItemView;
	}
}