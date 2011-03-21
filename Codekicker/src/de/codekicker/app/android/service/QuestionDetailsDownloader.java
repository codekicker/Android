package de.codekicker.app.android.service;

import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import de.codekicker.app.android.model.Answer;
import de.codekicker.app.android.model.Question;

public class QuestionDetailsDownloader extends IntentService {
	private static final String TAG = "QuestionDetailsDownloader";
	private static final String DOWNLOAD_URL = "http://codekicker.de/api/v1/QuestionView.json";

	public QuestionDetailsDownloader() {
		super(TAG);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Log.v(TAG, "Downloading question details");
		Question question = intent.getParcelableExtra("de.codekicker.app.android.Question");
		try {
			byte[] postParameters = ("id=" + question.getId()).getBytes();
			JSONDownloader jsonDownloader = new JSONDownloader();
			String json = jsonDownloader.downloadJSON(DOWNLOAD_URL, postParameters);
			createQuestion(json, question);
		} catch (MalformedURLException e) {
			Log.e(TAG, e.getMessage(), e);
		} catch (IOException e) {
			Log.e(TAG, e.getMessage(), e);
		}
		Intent broadcastIntent = new Intent("de.codekicker.app.android.QUESTION_DOWNLOAD_FINISHED");
		broadcastIntent.putExtra("de.codekicker.app.android.Question", question);
		sendBroadcast(broadcastIntent);
	}

	private void createQuestion(String json, Question question) {
		try {
			JSONObject jsonObject = new JSONObject(json);
			JSONArray rawAnswers = jsonObject.getJSONArray("answers");
			for (int i = 0; i < rawAnswers.length(); i++) {
				JSONObject rawAnswer = rawAnswers.getJSONObject(i);
				Answer answer = new Answer(rawAnswer.getString("text"),
						rawAnswer.getString("username"),
						rawAnswer.getString("elapsedTime"),
						rawAnswer.getInt("reputation"));
				question.add(answer);
			}
		} catch (JSONException e) {
			Log.e(TAG, e.getMessage(), e);
		}
	}
}