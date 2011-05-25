package de.codekicker.app.android.business;

public interface IVoteDoneCallback {
	void voteDone(boolean successful, int voteScore, String errorMessage);
}