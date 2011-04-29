package de.codekicker.app.android.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import roboguice.service.RoboIntentService;
import android.content.Intent;
import android.util.Log;

import com.google.inject.Inject;

import de.codekicker.app.android.business.IGravatarBitmapDownloader;
import de.codekicker.app.android.business.IServerRequest;
import de.codekicker.app.android.model.Answer;
import de.codekicker.app.android.model.Comment;
import de.codekicker.app.android.model.Question;
import de.codekicker.app.android.model.User;
import de.codekicker.app.android.preference.IPreferenceManager;

public class QuestionDetailsDownloader extends RoboIntentService {
	private static final String TAG = "QuestionDetailsDownloader";
	private static final String DOWNLOAD_URL = "QuestionView.json";
	@Inject IPreferenceManager preferenceManager;
	@Inject IServerRequest serverRequest;
	@Inject IGravatarBitmapDownloader gravatarBitmapDownloader;

	public QuestionDetailsDownloader() {
		super(TAG);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Log.v(TAG, "Downloading question details");
		Question question = intent.getParcelableExtra("de.codekicker.app.android.Question");
		try {
			String json = serverRequest.downloadJSON(preferenceManager.getApiBaseUrl() + DOWNLOAD_URL, "id=" + question.getId());
			createQuestion(json, question);
		} catch (Exception e) {
			Log.e(TAG, e.getMessage(), e);
		}
		Intent broadcastIntent = new Intent("de.codekicker.app.android.QUESTION_DOWNLOAD_FINISHED");
		broadcastIntent.putExtra("de.codekicker.app.android.Question", question);
		sendBroadcast(broadcastIntent);
	}

	private void createQuestion(String json, Question question) {
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			User questionUser = question.getUser();
			Log.v(TAG, "Downloading question Gravatar Bitmap");
			questionUser.setGravatar(gravatarBitmapDownloader.downloadBitmap(questionUser.getGravatarHash()));
			JSONObject jsonObject = new JSONObject(json);
			JSONArray rawAnswers = jsonObject.getJSONArray("Answers");
			Log.v(TAG, "Creating Answers");
			for (int i = 0; i < rawAnswers.length(); i++) {
				JSONObject rawAnswer = rawAnswers.getJSONObject(i);
				if (rawAnswer.getBoolean("IsQuestion"))
					continue;
				JSONArray jsonComments = rawAnswer.getJSONArray("Comments");
				int jsonCommentsLength = jsonComments.length();
				List<Comment> comments = new ArrayList<Comment>(jsonCommentsLength);
				for (int j = 0; j < jsonCommentsLength; j++) {
					JSONObject rawComment = jsonComments.getJSONObject(j);
					JSONObject rawUser = rawComment.getJSONObject("User");
					String userName = rawUser.getString("Name");
					User user = new User(rawUser.optInt("ID", -1),
							userName.equalsIgnoreCase("null") ? null : userName,
							rawUser.getString("UrlName"),
							rawUser.getInt("Reputation"),
							null,
							null);
					Date createDate = simpleDateFormat.parse(rawComment.getString("CreateDateTime"));
					Comment comment = new Comment(createDate,
							rawComment.getString("TextBody"),
							user);
					comments.add(comment);
				}
				JSONObject rawUserInfo = rawAnswer.getJSONObject("User");
				String userName = rawUserInfo.getString("Name");
				String gravatarId = rawUserInfo.getString("GravatarID");
				User user = new User(rawUserInfo.optInt("ID", -1),
						userName.equalsIgnoreCase("null") ? null : userName,
						rawUserInfo.getString("UrlName"),
						rawUserInfo.getInt("Reputation"),
						gravatarId,
						gravatarBitmapDownloader.downloadBitmap(gravatarId));
				Date createDateTime = simpleDateFormat.parse(rawAnswer.getString("CreateDateTime"));
				Answer answer = new Answer(createDateTime,
						rawAnswer.getString("TextBody"),
						rawAnswer.getBoolean("IsAccepted"),
						user,
						comments);
				question.add(answer);
			}
		} catch (JSONException e) {
			Log.e(TAG, e.getMessage(), e);
		} catch (ParseException e) {
			Log.e(TAG, e.getMessage(), e);
		}
	}
}