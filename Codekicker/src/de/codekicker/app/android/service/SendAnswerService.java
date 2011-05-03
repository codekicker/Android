package de.codekicker.app.android.service;

import java.net.URLEncoder;

import org.json.JSONObject;

import roboguice.service.RoboIntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
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
	private final Handler handler = new Handler();
	@Inject IPreferenceManager preferenceManager;
	@Inject IServerRequest serverRequest;
	@Inject Context context;
	@Inject Handler foo;
	
	public SendAnswerService() {
		super(TAG);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Log.v(TAG, "Sending answer to server");
		boolean successful = true;
		CharSequence toastMessage = context.getString(R.string.sendAnswerSuccessful);
		try {
			Question question = intent.getParcelableExtra("de.codekicker.app.android.Question");
			String answer = intent.getStringExtra("de.codekicker.app.android.AnswerBody");
			String fullUrl = preferenceManager.getApiBaseUrl() + DOWNLOAD_URL;
			String parameters = "questionID=" + question.getId() + "&answerBody=" + URLEncoder.encode(answer, "UTF-8");
			String username = preferenceManager.getUsername();
			String password = preferenceManager.getPassword();
			String result = serverRequest.send(fullUrl, parameters, username, password);
			JSONObject jsonResult = new JSONObject(result);
			String errorMessage = jsonResult.getString("ErrorMessage");
			if (errorMessage != null && !errorMessage.equals("null")) {
				Log.e(TAG, errorMessage);
				toastMessage = errorMessage;
				successful = false;
			}
		} catch (Exception e) {
			Log.e(TAG, e.getMessage(), e);
			toastMessage = context.getString(R.string.sendAnswerFailed);
			successful = false;
		}
		final CharSequence finalToastMessage = toastMessage;
		// Sending Toast in UI Thread
		handler.post(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(context, finalToastMessage, Toast.LENGTH_LONG).show();
			}
		});
		Log.v(TAG, "Sending answer was successful: " + successful);
		Intent broadcastIntent = new Intent("de.codekicker.app.android.ANSWER_SENT");
		broadcastIntent.putExtra("successful", successful);
		sendBroadcast(broadcastIntent);
	}
}