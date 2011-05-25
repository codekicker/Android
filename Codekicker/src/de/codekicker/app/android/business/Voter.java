package de.codekicker.app.android.business;

import org.json.JSONObject;

import roboguice.util.RoboAsyncTask;
import android.util.Log;

import com.google.inject.Inject;

import de.codekicker.app.android.preference.IPreferenceManager;

class Voter extends RoboAsyncTask<Boolean> implements IVoter {
	private static final String TAG = "Voter";
	private static final String VOTE_URL = "VoteAnswer.json";
	private final IPreferenceManager preferenceManager;
	private final IServerRequest serverRequest;
	private int answerId, voteScore;
	private IVoteDoneCallback callback;
	private String voteType, errorMessage;
	
	@Inject
	public Voter(IPreferenceManager preferenceManager, IServerRequest serverRequest) {
		this.preferenceManager = preferenceManager;
		this.serverRequest = serverRequest;
	}
	
	public void voteUp(int answerId, IVoteDoneCallback callback) {
		Log.v(TAG, "Voting up");
		this.answerId = answerId;
		this.callback = callback;
		voteType = "Up";
		execute();
	}
	
	public void voteDown(int answerId, IVoteDoneCallback callback) {
		Log.v(TAG, "Voting down");
		this.answerId = answerId;
		this.callback = callback;
		voteType = "Down";
		execute();
	}
	
	public void voteReset(int answerId, IVoteDoneCallback callback) {
		Log.v(TAG, "Resetting vote");
		this.answerId = answerId;
		this.callback = callback;
		voteType = "Reset";
		execute();
	}

	@Override
	public Boolean call() throws Exception {
		boolean successful = true;
		try {
			String fullUrl = preferenceManager.getApiBaseUrl() + VOTE_URL;
			String parameters = "answerID=" + answerId + "&voteType=" + voteType;
			String username = preferenceManager.getUsername();
			String password = preferenceManager.getPassword();
			// {"VoteScore":2,"ErrorCode":null,"ErrorMessage":null}
			String result = serverRequest.send(fullUrl, parameters, username, password);
			JSONObject jsonResult = new JSONObject(result);
			String errorCode = jsonResult.getString("ErrorCode");
			if (errorCode != null && !errorCode.trim().equalsIgnoreCase("null")) {
				successful = false;
				String errorMessage = jsonResult.getString("ErrorMessage");
				if (errorMessage != null && !errorMessage.trim().equalsIgnoreCase("null")) {
					this.errorMessage = errorMessage;
				}
			} else {
				voteScore = jsonResult.getInt("VoteScore");
			}
		} catch (Exception e) {
			Log.e(TAG, e.getMessage(), e);
			successful = false;
		}
		Log.v(TAG, "Sending vote was successful: " + successful);
		if (!successful && errorMessage != null) {
			Log.v(TAG, "Server returned following error message " + errorMessage);
		}
		return successful;
	}
	
	@Override
	protected void onSuccess(Boolean successful) throws Exception {
		super.onSuccess(successful);
		callback.voteDone(successful, voteScore, errorMessage);
	}
}