package de.codekicker.app.android.widget;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import de.codekicker.app.android.R;
import de.codekicker.app.android.model.Answer;
import de.codekicker.app.android.model.User;

public class QuestionDetailsAdapter extends ArrayAdapter<Answer> {
	private final Context context;
	private final LayoutInflater inflater;
	private final int listItemResourceId;
	private final List<Answer> answers;

	public QuestionDetailsAdapter(Context context, int listItemResourceId, List<Answer> answers, LayoutInflater inflater) {
		super(context, listItemResourceId, answers);
		this.context = context;
		this.inflater = inflater;
		this.listItemResourceId = listItemResourceId;
		this.answers = answers;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View listItemView = convertView;
		if (listItemView == null) {
			listItemView = inflater.inflate(listItemResourceId, null);
		}
		Answer answer = answers.get(position);
		User user = answer.getUser();
		Date createDate = answer.getCreateDate();
		String createDateString = DateFormat.getDateInstance(DateFormat.SHORT).format(createDate);
		String createTimeString = DateFormat.getTimeInstance(DateFormat.SHORT).format(createDate);
		createDateString = String.format(context.getString(R.string.askDate), createDateString, createTimeString);
		TextView textViewVoteScore = (TextView) listItemView.findViewById(R.id.textViewVoteScore);
		TextView textViewQuestionBody = (TextView) listItemView.findViewById(R.id.textViewQuestionBody);
		TextView textViewAskDate = (TextView) listItemView.findViewById(R.id.textViewAskDate);
		ImageView imageViewGravatar = (ImageView) listItemView.findViewById(R.id.imageViewGravatar);
		TextView textViewUserName = (TextView) listItemView.findViewById(R.id.textViewUserName);
		TextView textViewReputation = (TextView) listItemView.findViewById(R.id.textViewReputation);
		TextView textViewComments = (TextView) listItemView.findViewById(R.id.textViewComments);
		textViewQuestionBody.setText(answer.getTextBody());
		textViewAskDate.setText(createDateString);
		imageViewGravatar.setImageBitmap(user.getGravatar());
		textViewUserName.setText(user.getName());
		textViewReputation.setText(Integer.toString(user.getReputation()));
		int commentCountText = answer.getComments().size() == 1 ? R.string.commentCount : R.string.commentsCount;
		textViewComments.setText(String.format(context.getString(commentCountText), answer.getComments().size()));
		return listItemView;
	}
}