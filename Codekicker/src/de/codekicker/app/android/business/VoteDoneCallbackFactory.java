package de.codekicker.app.android.business;

import de.codekicker.app.android.activity.QuestionDetails;

class VoteDoneCallbackFactory implements IVoteDoneCallbackFactory {
	public VoteDoneCallback create(QuestionDetails questionDetails, int rowPosition, VoteType voteType) {
		return new VoteDone(questionDetails, rowPosition, voteType);
	}
}