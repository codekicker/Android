package de.codekicker.app.android.business;

import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import de.codekicker.app.android.R;
import de.codekicker.app.android.activity.QuestionDetails;

class VoteDoneCallback implements IVoteDoneCallback {
	private static final String TAG = VoteDoneCallback.class.getSimpleName();
	private final QuestionDetails questionDetails;
	private final int rowPosition;
	private final VoteType voteType;
	
	public VoteDoneCallback(QuestionDetails questionDetails, int rowPosition, VoteType voteType) {
		this.questionDetails = questionDetails;
		this.rowPosition = rowPosition;
		this.voteType = voteType;
	}

	@Override
	public void voteDone(boolean successful, int voteScore, String errorMessage) {
		if (successful) {
			LinearLayout linearLayout = (LinearLayout) questionDetails.getExpandableListView().getChildAt(rowPosition);
			int imageViewUpvoteResId, imageViewDownvoteResId;
			switch (voteType) {
			case UP:
				imageViewUpvoteResId = R.drawable.upvoteselected;
				imageViewDownvoteResId = R.drawable.downvote;
				break;
			case DOWN:
				imageViewUpvoteResId = R.drawable.upvote;
				imageViewDownvoteResId = R.drawable.downvoteselected;
				break;
			case RESET:
				imageViewUpvoteResId = R.drawable.upvote;
				imageViewDownvoteResId = R.drawable.downvote;
				break;
			default:
				Log.e(TAG, "Error finding matching view after vote");
				return;
			}
			ImageView imageViewUpvote = (ImageView) linearLayout.findViewById(R.id.imageViewUpvote);
			TextView textViewVoteScore = (TextView) linearLayout.findViewById(R.id.textViewVoteScore);
			ImageView imageViewDownvote = (ImageView) linearLayout.findViewById(R.id.imageViewDownvote);
			imageViewUpvote.setImageResource(imageViewUpvoteResId);
			textViewVoteScore.setText(Integer.toString(voteScore));
			imageViewDownvote.setImageResource(imageViewDownvoteResId);
		} else {
			String toastMessage = questionDetails.getString(R.string.errorSendingVote);
			if (errorMessage != null) {
				toastMessage = errorMessage;
			}
			Toast.makeText(questionDetails.getApplicationContext(), toastMessage, Toast.LENGTH_LONG).show();
		}
	}
}