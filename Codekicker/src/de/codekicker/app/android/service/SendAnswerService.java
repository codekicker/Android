package de.codekicker.app.android.service;

import java.net.URLEncoder;

import roboguice.service.RoboIntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.inject.Inject;

import de.codekicker.app.android.R;
import de.codekicker.app.android.business.IServerRequest;
import de.codekicker.app.android.model.Question;
import de.codekicker.app.android.preference.IPreferenceManager;

public class SendAnswerService extends RoboIntentService {
	private final static String TAG = "AnswerService";
	private static final String DOWNLOAD_URL = "AddAnswer.json";
	@Inject IPreferenceManager preferenceManager;
	@Inject IServerRequest serverRequest;
	@Inject Context context;
	
	public SendAnswerService() {
		super(TAG);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Log.v(TAG, "Sending answer to server");
		try {
			Question question = intent.getParcelableExtra("de.codekicker.app.android.Question");
			String answer = intent.getStringExtra("de.codekicker.app.android.AnswerBody");
			String fullUrl = preferenceManager.getApiBaseUrl() + DOWNLOAD_URL;
			String parameters = "questionID=" + question.getId() + "&answerBody=" + URLEncoder.encode(answer, "UTF-8");
			String username = preferenceManager.getUsername();
			String password = preferenceManager.getPassword();
			String result = serverRequest.send(fullUrl, parameters, username, password);
			Toast.makeText(context, R.string.sendAnswerSuccessful, Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			Log.e(TAG, e.getMessage(), e);
			Toast.makeText(context, R.string.sendAnswerFailed, Toast.LENGTH_LONG).show();
		}
		Intent broadcastIntent = new Intent("de.codekicker.app.android.ANSWER_SENT");
		sendBroadcast(broadcastIntent);
	}
}