package de.codekicker.app.android.business;

public interface VoteDoneCallback {
	void voteDone(boolean successful, int voteScore, String errorMessage);
}