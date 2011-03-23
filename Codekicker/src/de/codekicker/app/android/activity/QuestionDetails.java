package de.codekicker.app.android.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import de.codekicker.app.android.R;
import de.codekicker.app.android.model.Question;
import de.codekicker.app.android.service.QuestionDetailsDownloader;

public class QuestionDetails extends Activity implements OnClickListener {
	private static final String TAG = "QuestionDetails";
	private ProgressDialog progressDialog;
	private ImageButton imageButtonUpvote;
	private ImageButton imageButtonDownvote;
	private BroadcastReceiver questionDownloadedReceiver = new BroadcastReceiver() {
		private static final String TAG = "QuestionDownloadedReceiver";
		
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.v(TAG, "Broadcast received");
			Question question = (Question) intent.getParcelableExtra("de.codekicker.app.android.Question");
			questionDownloaded(question);
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		progressDialog = ProgressDialog.show(this, null, getString(R.string.refreshingData));
		registerReceiver(questionDownloadedReceiver, new IntentFilter("de.codekicker.app.android.QUESTION_DOWNLOAD_FINISHED"));
		Question question = getIntent().getParcelableExtra("de.codekicker.app.android.SelectedQuestion");
		Intent intent = new Intent(this, QuestionDetailsDownloader.class);
		intent.putExtra("de.codekicker.app.android.Question", question);
		startService(intent);
		setContentView(R.layout.question_details);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(questionDownloadedReceiver);
	}
	
	private void questionDownloaded(Question question) {
		Log.v(TAG, "Fill view");
		TextView textViewTitle = (TextView) findViewById(R.id.textViewTitle);
		TextView textViewQuestionBody = (TextView) findViewById(R.id.textViewQuestionBody);
		TextView textViewVoteScore = (TextView) findViewById(R.id.textViewVoteScore);
		textViewTitle.setText(question.getTitle());
		textViewQuestionBody.setText(question.getQuestionBody());
		textViewVoteScore.setText(Integer.toString(question.getVoteScore()));
		progressDialog.hide();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.imageButtonUpvote:
			// TODO: Upvote
			imageButtonUpvote.setImageResource(R.drawable.upvoteselected);
			break;
		case R.id.imageButtonDownvote:
			// TODO: Downvote
			imageButtonDownvote.setImageResource(R.drawable.downvoteselected);
			break;
		case R.id.buttonAnswer:
			// TODO: Send answer
			break;
		}
	}
}