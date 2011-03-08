package de.codekicker.app.android.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.codekicker.app.android.model.Answer;
import de.codekicker.app.android.model.Question;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class QuestionDetailsDownloader extends IntentService {
	private static final String TAG = "QuestionDetailsDownloader";
	private static final String DOWNLOAD_URL = "http://android.echooff.de/codekicker_question_details_mock.php?questionId=%s";

	public QuestionDetailsDownloader() {
		super(TAG);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Log.v(TAG, "Downloading question details");
		Question question = intent.getParcelableExtra("de.codekicker.app.android.Question");
		BufferedReader bufferedReader = null;
		try {
			URL url = new URL(String.format(DOWNLOAD_URL, question.getId()));
			InputStream inputStream = url.openStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
			bufferedReader = new BufferedReader(inputStreamReader);
			StringBuilder stringBuilder = new StringBuilder();
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				stringBuilder.append(line);
			}
			String json = stringBuilder.toString();
			Log.v(TAG, json);
			createQuestion(json, question);
		} catch (MalformedURLException e) {
			Log.e(TAG, e.getMessage(), e);
		} catch (IOException e) {
			Log.e(TAG, e.getMessage(), e);
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					Log.e(TAG, e.getMessage(), e);
				}
			}
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