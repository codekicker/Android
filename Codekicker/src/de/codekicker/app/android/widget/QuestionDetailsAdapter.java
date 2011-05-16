package de.codekicker.app.android.widget;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import de.codekicker.app.android.R;
import de.codekicker.app.android.activity.QuestionDetails;
import de.codekicker.app.android.model.Answer;
import de.codekicker.app.android.model.User;
import de.codekicker.app.android.preference.IPreferenceManager;

public class QuestionDetailsAdapter extends ArrayAdapter<Answer> {
	private final QuestionDetails questionDetails;
	private final IPreferenceManager preferenceManager;
	private final LayoutInflater inflater;
	private final int listItemResourceId;
	private final List<Answer> answers;
	private class UpvoteClickListener implements OnClickListener {
		private final Answer answer;
		private final int rowPosition;
		
		public UpvoteClickListener(int rowPosition, Answer answer) {
			this.answer = answer;
			this.rowPosition = rowPosition;
		}
		
		@Override
		public void onClick(View v) {
			questionDetails.onUpvoteClick(rowPosition, answer);
		}
	}
	private class DownvoteClickListener implements OnClickListener {
		private final Answer answer;
		private final int rowPosition;
		
		public DownvoteClickListener(int rowPosition, Answer answer) {
			this.answer = answer;
			this.rowPosition = rowPosition;
		}
		
		@Override
		public void onClick(View v) {
			questionDetails.onDownvoteClick(rowPosition, answer);
		}
	}

	public QuestionDetailsAdapter(QuestionDetails questionDetails,
			IPreferenceManager preferenceManager,
			int listItemResourceId,
			List<Answer> answers,
			LayoutInflater inflater) {
		super(questionDetails, listItemResourceId, answers);
		this.questionDetails = questionDetails;
		this.preferenceManager = preferenceManager;
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
		createDateString = String.format(questionDetails.getString(R.string.askDate), createDateString, createTimeString);
		ImageView imageViewUpvote = (ImageView) listItemView.findViewById(R.id.imageViewUpvote);
		ImageView imageViewDownvote = (ImageView) listItemView.findViewById(R.id.imageViewDownvote);
		TextView textViewVoteScore = (TextView) listItemView.findViewById(R.id.textViewVoteScore);
		TextView textViewQuestionBody = (TextView) listItemView.findViewById(R.id.textViewQuestionBody);
		TextView textViewAskDate = (TextView) listItemView.findViewById(R.id.textViewAskDate);
		ImageView imageViewGravatar = (ImageView) listItemView.findViewById(R.id.imageViewGravatar);
		TextView textViewUserName = (TextView) listItemView.findViewById(R.id.textViewUserName);
		TextView textViewReputation = (TextView) listItemView.findViewById(R.id.textViewReputation);
		TextView textViewComments = (TextView) listItemView.findViewById(R.id.textViewComments);
		imageViewUpvote.setEnabled(preferenceManager.isUserAuthenticated());
		imageViewUpvote.setOnClickListener(new UpvoteClickListener(position + 1, answer));
		textViewVoteScore.setText(Integer.toString(answer.getVoteScore()));
		imageViewDownvote.setOnClickListener(new DownvoteClickListener(position + 1, answer));
		imageViewDownvote.setEnabled(preferenceManager.isUserAuthenticated());
		textViewQuestionBody.setText(answer.getTextBody());
		textViewAskDate.setText(createDateString);
		imageViewGravatar.setImageBitmap(user.getGravatar());
		textViewUserName.setText(user.getName());
		textViewReputation.setText(Integer.toString(user.getReputation()));
		int commentCountText = answer.getComments().size() == 1 ? R.string.commentCount : R.string.commentsCount;
		textViewComments.setText(String.format(questionDetails.getString(commentCountText), answer.getComments().size()));
		return listItemView;
	}
}