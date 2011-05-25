package de.codekicker.app.android.business;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONObject;

import roboguice.util.RoboAsyncTask;

import android.util.Log;

import com.google.inject.Inject;

import de.codekicker.app.android.model.Question;
import de.codekicker.app.android.model.User;
import de.codekicker.app.android.preference.IPreferenceManager;

class QuestionListDownloader extends RoboAsyncTask<List<Question>> implements IQuestionListDownloader {
	private static final String TAG = "QuestionListDownloader";
	private static final String DOWNLOAD_URL = "QuestionList.json";
	private final IPreferenceManager preferenceManager;
	private final IServerRequest serverRequest;
	private DownloadDoneCallback callback;
	
	@Inject
	public QuestionListDownloader(IPreferenceManager preferenceManager, IServerRequest serverRequest) {
		this.preferenceManager = preferenceManager;
		this.serverRequest = serverRequest;
	}
	
	@Override
	public void downloadQuestions(DownloadDoneCallback callback) {
		this.callback = callback;
		execute();
	}
	
	@Override
	public List<Question> call() throws Exception {
		ArrayList<Question> questions = new ArrayList<Question>();
		try {
			Log.v(TAG, "Downloading questions");
			String json = serverRequest.downloadJSON(preferenceManager.getApiBaseUrl() + DOWNLOAD_URL, "sortOrder=AskDateTime&filterMinID=0");
			Log.v(TAG, "Creating question models");
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
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
		} catch (Exception e) {
			Log.e(TAG, e.getMessage(), e);
		}
		return questions;
	}
	
	@Override
	protected void onSuccess(List<Question> result) throws Exception {
		super.onSuccess(result);
		callback.downloadDone(result);
	}
}