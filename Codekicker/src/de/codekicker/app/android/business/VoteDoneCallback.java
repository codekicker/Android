package de.codekicker.app.android.business;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import de.codekicker.app.android.R;
import de.codekicker.app.android.activity.QuestionDetails;

class VoteDoneCallback implements IVoteDoneCallback {
	private static final String TAG = VoteDoneCallback.class.getSimpleName();
	private final QuestionDetails questionDetails;
	private final RelativeLayout relativeLayout;
	private final VoteType voteType;
	
	public VoteDoneCallback(QuestionDetails questionDetails, RelativeLayout relativeLayout, VoteType voteType) {
		this.questionDetails = questionDetails;
		this.relativeLayout = relativeLayout;
		this.voteType = voteType;
	}

	@Override
	public void voteDone(boolean successful, int voteScore, String errorMessage) {
		if (successful) {
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
			ImageView imageViewUpvote = (ImageView) relativeLayout.findViewById(R.id.imageViewUpvote);
			TextView textViewVoteScore = (TextView) relativeLayout.findViewById(R.id.textViewVoteScore);
			ImageView imageViewDownvote = (ImageView) relativeLayout.findViewById(R.id.imageViewDownvote);
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
		relativeLayout.findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
		relativeLayout.findViewById(R.id.textViewVoteScore).setVisibility(View.VISIBLE);
	}
}