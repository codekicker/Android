package de.codekicker.app.android.business;

import java.net.URLEncoder;

import org.json.JSONObject;

import roboguice.util.RoboAsyncTask;
import android.util.Log;

import com.google.inject.Inject;

import de.codekicker.app.android.model.Question;
import de.codekicker.app.android.preference.IPreferenceManager;

class AnswerSender extends RoboAsyncTask<Boolean> implements IAnswerSender {
	private final static String TAG = AnswerSender.class.getSimpleName();
	private static final String ANSWER_URL = "AddAnswer.json";
	private final IPreferenceManager preferenceManager;
	private final IServerRequest serverRequest;
	private Question question;
	private String answer;
	private AnswerSentCallback callback;
	private String serverErrorMessage;
	
	@Inject
	public AnswerSender(IPreferenceManager preferenceManager, IServerRequest serverRequest) {
		this.preferenceManager = preferenceManager;
		this.serverRequest = serverRequest;
	}
	
	@Override
	public void sendAnswer(Question question, String answer, AnswerSentCallback callback) {
		this.question = question;
		this.answer = answer;
		this.callback = callback;
		execute();
	}
	
	@Override
	public Boolean call() throws Exception {
		Log.v(TAG, "Sending answer to server");
		boolean successful = true;
		try {
			String fullUrl = preferenceManager.getApiBaseUrl() + ANSWER_URL;
			String parameters = "questionID=" + question.getId() + "&answerBody=" + URLEncoder.encode(answer, "UTF-8");
			String username = preferenceManager.getUsername();
			String password = preferenceManager.getPassword();
			String result = serverRequest.send(fullUrl, parameters, username, password);
			JSONObject jsonResult = new JSONObject(result);
			String errorCode = jsonResult.getString("ErrorCode");
			if (errorCode != null && !errorCode.equals("null")) {
				Log.e(TAG, "Server sent error code: " + errorCode);
				String errorMessage = jsonResult.getString("ErrorMessage");
				if (errorMessage != null && !errorMessage.equals("null")) {
					serverErrorMessage = errorMessage;
				}
				successful = false;
			}
		} catch (Exception e) {
			Log.e(TAG, e.getMessage(), e);
			successful = false;
		}
		Log.v(TAG, "Sending answer was successful: " + successful);
		return successful;
	}
	
	@Override
	protected void onSuccess(Boolean successful) throws Exception {
		super.onSuccess(successful);
		callback.answerSent(successful, serverErrorMessage);
	}
}