package de.codekicker.app.android.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class QuestionDetailsDownloader extends IntentService {
	private static final String TAG = "QuestionDetailsDownloader";
	private static final String DOWNLOAD_URL = "http://android.echooff.de/codekicker_question_list_mock.php";

	public QuestionDetailsDownloader() {
		super(TAG);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Log.v(TAG, "Downloading question details");
		int questionId = intent.getIntExtra("de.codekicker.app.android.QuestionId", -1);
		Intent broadcastIntent = new Intent("de.codekicker.app.android.QUESTION_DOWNLOAD_FINISHED");
		sendBroadcast(broadcastIntent);
	}
}