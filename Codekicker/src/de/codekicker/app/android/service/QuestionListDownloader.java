package de.codekicker.app.android.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import de.codekicker.app.android.business.ServerRequest;
import de.codekicker.app.android.model.Question;
import de.codekicker.app.android.model.User;
import de.codekicker.app.android.preference.PreferenceManager;

public class QuestionListDownloader extends IntentService {
	private static final String TAG = "QuestionListDownloader";
	private static final String DOWNLOAD_URL = "QuestionList.json";
	
	public QuestionListDownloader() {
		super(TAG);
	}
	
	@Override
	protected void onHandleIntent(Intent intent) {
		Log.v(TAG, "Downloading questions");
		ArrayList<Question> questions = new ArrayList<Question>();
		try {
			PreferenceManager preferenceManager = PreferenceManager.getInstance(getApplicationContext());
			ServerRequest serverRequest = new ServerRequest(preferenceManager.getAppIdKey(), preferenceManager.getAppId());
			String json = serverRequest.downloadJSON(preferenceManager.getApiBaseUrl() + DOWNLOAD_URL, "sortOrder=AskDateTime&filterMinID=0");
			questions = createQuestions(json);
		} catch (Exception e) {
			Log.e(TAG, e.getMessage(), e);
		}
		Intent broadcastIntent = new Intent("de.codekicker.app.android.QUESTIONS_DOWNLOAD_FINISHED");
		broadcastIntent.putParcelableArrayListExtra("de.codekicker.app.android.Questions", questions);
		sendBroadcast(broadcastIntent);
	}
	
	private ArrayList<Question> createQuestions(String json) {
		Log.v(TAG, "Creating question models");
		ArrayList<Question> questions = new ArrayList<Question>();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			JSONObject jsonObject = new JSONObject(json);
			JSONArray rawQuestions = jsonObject.getJSONArray("Questions");
			for (int i = 0; i < rawQuestions.length(); i++) {
				JSONObject rawQuestion = rawQuestions.getJSONObject(i);
				JSONArray jsonTags = rawQuestion.getJSONArray("Tags");
				int jsonTagsLength = jsonTags.length();
				String[] tags = new String[jsonTagsLength];
				for (int j = 0; j < jsonTagsLength; j++) {
					tags[j] = jsonTags.getString(j);
				}
				JSONObject rawUserInfo = rawQuestion.getJSONObject("UserInfo");
				String userName = rawUserInfo.getString("Name");
				User user = new User(rawUserInfo.optInt("ID", -1),
						userName.equalsIgnoreCase("null") ? null : userName,
						rawUserInfo.getString("UrlName"),
						rawUserInfo.getInt("Reputation"),
						rawUserInfo.getString("GravatarID"));
				Date askDateTime = simpleDateFormat.parse(rawQuestion.getString("AskDateTime"));
				Question question = new Question(rawQuestion.getInt("ID"),
						rawQuestion.getString("Title"),
						rawQuestion.getString("UrlName"),
						askDateTime,
						rawQuestion.getString("QuestionBody"),
						rawQuestion.getBoolean("HasAcceptedAnswer"),
						rawQuestion.getInt("VoteScore"),
						rawQuestion.getInt("AnswerCount"),
						rawQuestion.getInt("ViewCount"),
						tags,
						user);
				questions.add(question);
			}
		} catch (JSONException e) {
			Log.e(TAG, e.getMessage(), e);
		} catch (ParseException e) {
			Log.e(TAG, e.getMessage(), e);
		}
		return questions;
	}
}