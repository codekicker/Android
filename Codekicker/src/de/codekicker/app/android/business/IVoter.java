package de.codekicker.app.android.business;

public interface IVoter {
	void voteUp(int id, IVoteDoneCallback callback);
	void voteDown(int id, IVoteDoneCallback callback);
}