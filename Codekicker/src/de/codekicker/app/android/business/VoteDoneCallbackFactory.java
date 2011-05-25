package de.codekicker.app.android.business;

import de.codekicker.app.android.activity.QuestionDetails;

class VoteDoneCallbackFactory implements IVoteDoneCallbackFactory {
	public IVoteDoneCallback create(QuestionDetails questionDetails, int rowPosition, VoteType voteType) {
		return new VoteDoneCallback(questionDetails, rowPosition, voteType);
	}
}