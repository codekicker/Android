package de.codekicker.app.android.business;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONObject;

import roboguice.util.RoboAsyncTask;
import android.util.Log;

import com.google.inject.Inject;

import de.codekicker.app.android.model.Answer;
import de.codekicker.app.android.model.Comment;
import de.codekicker.app.android.model.Question;
import de.codekicker.app.android.model.User;
import de.codekicker.app.android.preference.IPreferenceManager;

class AnswersDownloader extends RoboAsyncTask<List<Answer>> implements IAnswersDownloader {
	private static final String TAG = "QuestionDetailsDownloader";
	private static final String DOWNLOAD_URL = "QuestionView.json";
	private final IPreferenceManager preferenceManager;
	private final IServerRequest serverRequest;
	private final IGravatarBitmapDownloader gravatarBitmapDownloader;
	private Question question;
	private DownloadDoneCallback callback;
	
	@Inject
	public AnswersDownloader(IPreferenceManager preferenceManager,
			IServerRequest serverRequest,
			IGravatarBitmapDownloader gravatarBitmapDownloader) {
		this.preferenceManager = preferenceManager;
		this.serverRequest = serverRequest;
		this.gravatarBitmapDownloader = gravatarBitmapDownloader;
	}
	
	@Override
	public void downloadDetails(Question question, DownloadDoneCallback callback) {
		this.question = question;
		this.callback = callback;
		execute();
	}

	@Override
	public List<Answer> call() throws Exception {
		List<Answer> answers = new ArrayList<Answer>();
		try {
			Log.v(TAG, "Downloading question details");
			String json = serverRequest.downloadJSON(preferenceManager.getApiBaseUrl() + DOWNLOAD_URL, "id=" + question.getId());
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
			User questionUser = question.getUser();
			Log.v(TAG, "Downloading question Gravatar Bitmap");
			questionUser.setGravatar(gravatarBitmapDownloader.downloadBitmap(questionUser.getGravatarHash()));
			JSONObject jsonObject = new JSONObject(json);
			JSONArray rawAnswers = jsonObject.getJSONArray("Answers");
			Log.v(TAG, "Creating Answers");
			for (int i = 0; i < rawAnswers.length(); i++) {
				JSONObject rawAnswer = rawAnswers.getJSONObject(i);
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
				Answer answer = new Answer(rawAnswer.optInt("ID", -1),
						rawAnswer.getBoolean("IsQuestion"),
						createDateTime,
						rawAnswer.getString("TextBody"),
						rawAnswer.getInt("VoteScore"),
						rawAnswer.optBoolean("IsAccepted", false),
						user,
						comments);
				answers.add(answer);
			}
		} catch (Exception e) {
			Log.e(TAG, e.getMessage(), e);
		}
		Log.v(TAG, "Downloading question details done");
		return answers;
	}
	
	@Override
	protected void onSuccess(List<Answer> answers) throws Exception {
		super.onSuccess(answers);
		callback.downloadDone(answers);
	}
}