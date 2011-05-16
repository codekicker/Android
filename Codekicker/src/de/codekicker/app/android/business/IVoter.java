package de.codekicker.app.android.business;

public interface IVoter {
	void voteUp(int id, VoteDoneCallback callback);
	void voteDown(int id, VoteDoneCallback callback);
}