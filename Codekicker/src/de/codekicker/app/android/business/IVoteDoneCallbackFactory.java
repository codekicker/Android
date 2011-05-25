package de.codekicker.app.android.business;

import de.codekicker.app.android.activity.QuestionDetails;

public interface IVoteDoneCallbackFactory {
	IVoteDoneCallback create(QuestionDetails questionDetails, int rowPosition, VoteType voteType);
}