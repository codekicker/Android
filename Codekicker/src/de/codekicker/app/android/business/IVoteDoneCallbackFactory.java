package de.codekicker.app.android.business;

import android.widget.RelativeLayout;
import de.codekicker.app.android.activity.QuestionDetails;

public interface IVoteDoneCallbackFactory {
	IVoteDoneCallback create(QuestionDetails questionDetails, RelativeLayout relativeLayout, VoteType voteType);
}