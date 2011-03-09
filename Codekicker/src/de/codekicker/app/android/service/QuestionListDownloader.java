package de.codekicker.app.android.service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import de.codekicker.app.android.model.Question;

public class QuestionListDownloader extends IntentService {
	private static final String TAG = "QuestionListDownloader";
	private static final String DOWNLOAD_URL = "http://codekicker.de/api/v1/QuestionList.json";
	
	public QuestionListDownloader() {
		super(TAG);
	}
	
	@Override
	protected void onHandleIntent(Intent intent) {
		Log.v(TAG, "Downloading questions");
		BufferedReader bufferedReader = null;
		DataOutputStream dataOutputStream = null;
		ArrayList<Question> questions = new ArrayList<Question>();
		try {
			byte[] postParameters = "sortOrder=AskDateTime&filterMinID=1265".getBytes();
			URL url = new URL(DOWNLOAD_URL);
			HttpURLConnection httpUrlConnection = (HttpURLConnection) url.openConnection();
			httpUrlConnection.setRequestMethod("POST");
			httpUrlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			httpUrlConnection.setRequestProperty("Content-Length", Integer.toString(postParameters.length));
			httpUrlConnection.setRequestProperty("CK-App-ID", "codekicker.official-app.v1.0");
			httpUrlConnection.setDoOutput(true);
			dataOutputStream = new DataOutputStream(httpUrlConnection.getOutputStream());
			dataOutputStream.write(postParameters, 0, postParameters.length);
			dataOutputStream.flush();
			InputStreamReader inputStreamReader = new InputStreamReader(httpUrlConnection.getInputStream());
			bufferedReader = new BufferedReader(inputStreamReader);
			StringBuilder stringBuilder = new StringBuilder();
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				stringBuilder.append(line);
			}
			String json = stringBuilder.toString();
			Log.v(TAG, json);
			questions = createQuestions(json);
		} catch (MalformedURLException e) {
			Log.e(TAG, e.getMessage(), e);
		} catch (IOException e) {
			Log.e(TAG, e.getMessage(), e);
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
					dataOutputStream.close();
				} catch (IOException e) {
					Log.e(TAG, e.getMessage(), e);
				}
			}
		}
		Intent broadcastIntent = new Intent("de.codekicker.app.android.QUESTIONS_DOWNLOAD_FINISHED");
		broadcastIntent.putParcelableArrayListExtra("de.codekicker.app.android.Questions", questions);
		sendBroadcast(broadcastIntent);
	}
	
	private ArrayList<Question> createQuestions(String json) {
		Log.v(TAG, "Creating question models");
		ArrayList<Question> questions = new ArrayList<Question>();
		try {
			JSONObject jsonObject = new JSONObject(json);
			JSONArray rawQuestions = jsonObject.getJSONArray("questions");
			for (int i = 0; i < rawQuestions.length(); i++) {
				JSONObject rawQuestion = rawQuestions.getJSONObject(i);
				JSONArray jsonTags = rawQuestion.getJSONArray("tags");
				int jsonTagsLength = jsonTags.length();
				String[] tags = new String[jsonTagsLength];
				for (int j = 0; j < jsonTagsLength; j++) {
					tags[j] = jsonTags.getString(j);
				}
				Question question = new Question(rawQuestion.getInt("id"),
												 rawQuestion.getString("headline"),
												 rawQuestion.getString("question"),
												 rawQuestion.getInt("ratings"),
												 rawQuestion.getInt("answers"),
												 rawQuestion.getInt("views"),
												 tags,
												 rawQuestion.getString("username"),
												 rawQuestion.getString("elapsedTime"));
				questions.add(question);
			}
		} catch (JSONException e) {
			Log.e(TAG, e.getMessage(), e);
		}
		return questions;
	}
}