package de.codekicker.app.android.business;

import android.widget.RelativeLayout;
import de.codekicker.app.android.activity.QuestionDetails;

class VoteDoneCallbackFactory implements IVoteDoneCallbackFactory {
	public IVoteDoneCallback create(QuestionDetails questionDetails, RelativeLayout relativeLayout, VoteType voteType) {
		return new VoteDoneCallback(questionDetails, relativeLayout, voteType);
	}
}