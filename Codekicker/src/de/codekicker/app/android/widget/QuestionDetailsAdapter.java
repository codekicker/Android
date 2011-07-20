package de.codekicker.app.android.widget;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import de.codekicker.app.android.R;
import de.codekicker.app.android.activity.QuestionDetails;
import de.codekicker.app.android.business.VoteType;
import de.codekicker.app.android.model.Answer;
import de.codekicker.app.android.model.Comment;
import de.codekicker.app.android.model.User;
import de.codekicker.app.android.preference.IPreferenceManager;

public class QuestionDetailsAdapter extends BaseExpandableListAdapter implements IQuestionDetailsAdapter {
	private static final String TAG = QuestionDetailsAdapter.class.getSimpleName();
	private List<Answer> answers = new ArrayList<Answer>();
	private final QuestionDetails questionDetails;
	private final LayoutInflater inflater;
	private final IPreferenceManager preferenceManager;
	private class UpvoteClickListener implements OnClickListener {
		private final RelativeLayout relativeLayout;
		private final int groupPosition;
		
		public UpvoteClickListener(int groupPosition, RelativeLayout relativeLayout) {
			this.relativeLayout = relativeLayout;
			this.groupPosition = groupPosition;
		}
		
		@Override
		public void onClick(View v) {
			questionDetails.onUpvoteClick(groupPosition, relativeLayout);
		}
	}
	private class DownvoteClickListener implements OnClickListener {
		private final RelativeLayout relativeLayout;
		private final int groupPosition;
		
		public DownvoteClickListener(int groupPosition, RelativeLayout relativeLayout) {
			this.relativeLayout = relativeLayout;
			this.groupPosition = groupPosition;
		}
		
		@Override
		public void onClick(View v) {
			questionDetails.onDownvoteClick(groupPosition, relativeLayout);
		}
	}
	
	public QuestionDetailsAdapter(QuestionDetails questionDetails,
			LayoutInflater inflater,
			IPreferenceManager preferenceManager) {
		this.questionDetails = questionDetails;
		this.inflater = inflater;
		this.preferenceManager = preferenceManager;
	}
	
	public void add(Answer answer) {
		answers.add(answer);
	}
	
	public void clear() {
		answers.clear();
	}
	
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return answers.get(groupPosition).getComments().get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		Comment comment = answers.get(groupPosition).getComments().get(childPosition);
		View commentView = inflater.inflate(R.layout.question_details_comment, null);
		TextView textViewComment = (TextView) commentView.findViewById(R.id.textViewComment);
		textViewComment.setText(comment.getTextBody());
		TextView textViewUserName = (TextView) commentView.findViewById(R.id.textViewUserName);
		textViewUserName.setText(comment.getUser().getName());
		return commentView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return answers.get(groupPosition).getComments().size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return answers.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return answers.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,	View convertView, ViewGroup parent) {
		RelativeLayout relativeLayout = (RelativeLayout) inflater.inflate(R.layout.question_details_list_item, null);
		Answer answer = answers.get(groupPosition);
		User user = answer.getUser();
		Date createDate = answer.getCreateDate();
		String createDateString = DateFormat.getDateInstance(DateFormat.SHORT).format(createDate);
		String createTimeString = DateFormat.getTimeInstance(DateFormat.SHORT).format(createDate);
		createDateString = String.format(questionDetails.getString(R.string.askDate), createDateString, createTimeString);
		TextView textViewVoteScore = (TextView) relativeLayout.findViewById(R.id.textViewVoteScore);
		TextView textViewQuestionBody = (TextView) relativeLayout.findViewById(R.id.textViewQuestionBody);
		TextView textViewAskDate = (TextView) relativeLayout.findViewById(R.id.textViewAskDate);
		ImageView imageViewGravatar = (ImageView) relativeLayout.findViewById(R.id.imageViewGravatar);
		TextView textViewUserName = (TextView) relativeLayout.findViewById(R.id.textViewUserName);
		TextView textViewReputation = (TextView) relativeLayout.findViewById(R.id.textViewReputation);
		TextView textViewComments = (TextView) relativeLayout.findViewById(R.id.textViewComments);
		prepareUpvote(relativeLayout, groupPosition, answer);
		prepareDownvote(relativeLayout, groupPosition, answer);
		textViewVoteScore.setText(Integer.toString(answer.getVoteScore()));
		textViewQuestionBody.setText(answer.getTextBody());
		textViewAskDate.setText(createDateString);
		imageViewGravatar.setImageBitmap(user.getGravatar());
		textViewUserName.setText(user.getName());
		textViewReputation.setText(Integer.toString(user.getReputation()));
		int commentCountText = answer.getComments().size() == 1 ? R.string.commentCount : R.string.commentsCount;
		textViewComments.setText(String.format(questionDetails.getString(commentCountText), answer.getComments().size()));
		LinearLayout answersCountLinearLayout = (LinearLayout) relativeLayout.findViewById(R.id.answersCountLinearLayout);
		if (groupPosition == 0) {
			TextView textViewAnswerCount = (TextView) answersCountLinearLayout.findViewById(R.id.textViewAnswerCount);
			int realAnswers = answers.size() - 1;
			int answerCountText = realAnswers == 1 ? R.string.answerCount : R.string.answersCount;
			textViewAnswerCount.setText(String.format(questionDetails.getString(answerCountText), realAnswers));
			answersCountLinearLayout.setVisibility(View.VISIBLE);
		} else {
			answersCountLinearLayout.setVisibility(View.GONE);
		}
		return relativeLayout;
	}

	private void prepareUpvote(RelativeLayout relativeLayout, int groupPosition, Answer answer) {
		ImageView imageViewUpvote = (ImageView) relativeLayout.findViewById(R.id.imageViewUpvote);
		imageViewUpvote.setEnabled(preferenceManager.isUserAuthenticated());
		imageViewUpvote.setOnClickListener(new UpvoteClickListener(groupPosition, relativeLayout));
		if (answer.getVoteType() == VoteType.UP) {
			imageViewUpvote.setImageResource(R.drawable.upvoteselected);
		}
	}
	
	private void prepareDownvote(RelativeLayout relativeLayout, int groupPosition, Answer answer) {
		ImageView imageViewDownvote = (ImageView) relativeLayout.findViewById(R.id.imageViewDownvote);
		imageViewDownvote.setEnabled(preferenceManager.isUserAuthenticated());
		imageViewDownvote.setOnClickListener(new DownvoteClickListener(groupPosition, relativeLayout));
		if (answer.getVoteType() == VoteType.DOWN) {
			imageViewDownvote.setImageResource(R.drawable.downvoteselected);
		}
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}
}