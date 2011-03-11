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
		TextView textViewTitle = (TextView) listItemView.findViewById(R.id.textViewTitle);
		TextView textViewQuestionBody = (TextView) listItemView.findViewById(R.id.textViewQuestionBody);
		TextView textViewVoteScore = (TextView) listItemView.findViewById(R.id.textViewVoteScore);
		TextView textViewAnswerCount = (TextView) listItemView.findViewById(R.id.textViewAnswerCount);
		TextView textViewViewCount = (TextView) listItemView.findViewById(R.id.textViewViewCount);
		TextView textViewAskDate = (TextView) listItemView.findViewById(R.id.textViewAskDate);
		LinearLayout linearLayoutTags = (LinearLayout) listItemView.findViewById(R.id.linearLayoutTags);
		linearLayoutTags.removeAllViews();
		for (String tag : question.getTags()) {
			FrameLayout frameLayoutTag = (FrameLayout) inflater.inflate(R.layout.questions_list_tag, null);
			TextView textViewTag = (TextView) frameLayoutTag.getChildAt(0);
			textViewTag.setText(tag);
			linearLayoutTags.addView(frameLayoutTag);
		}
		textViewTitle.setText(question.getTitle());
		String questionBody = question.getQuestionBody().replace("\r\n", " ").replace("\n", " ");
		if (questionBody.length() > 180) {
			String shortedQuestionBody = questionBody.substring(0, 180);
			int index = shortedQuestionBody.lastIndexOf(" ");
			questionBody = shortedQuestionBody.substring(0, index) + "...";
		}
		textViewQuestionBody.setText(questionBody);
		textViewVoteScore.setText(Integer.toString(question.getVoteScore()));
		textViewAnswerCount.setText(Integer.toString(question.getAnswerCount()));
		textViewViewCount.setText(Integer.toString(question.getViewCount()));
		textViewAskDate.setText(String.format(resources.getString(R.string.askDate), question.getAskDate()));
		return listItemView;
	}
}