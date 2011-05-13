package de.codekicker.app.android.service;

import java.net.URLEncoder;

import org.json.JSONObject;

import com.google.inject.Inject;

import de.codekicker.app.android.R;
import de.codekicker.app.android.business.IServerRequest;
import de.codekicker.app.android.model.Question;
import de.codekicker.app.android.preference.IPreferenceManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;
import roboguice.service.RoboIntentService;

public class VoteService extends RoboIntentService {
	private static final String TAG = "VoteService";
	private static final String VOTE_URL = "VoteAnswer.json";
	private final Handler handler = new Handler();
	@Inject IPreferenceManager preferenceManager;
	@Inject IServerRequest serverRequest;
	@Inject Context context;
	
	public VoteService() {
		super(TAG);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
//		VoteAnswer.json?answerID=4969&voteType=Down
//		voteType=Up|Down|Reset
	}
}