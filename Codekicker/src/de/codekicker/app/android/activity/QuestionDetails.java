package de.codekicker.app.android.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import de.codekicker.app.android.R;
import de.codekicker.app.android.model.Question;
import de.codekicker.app.android.service.QuestionDetailsDownloader;
import de.codekicker.app.android.service.QuestionListDownloader;

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
			foo();
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		registerReceiver(questionDownloadedReceiver, new IntentFilter("de.codekicker.app.android.QUESTION_DOWNLOAD_FINISHED"));
		progressDialog = ProgressDialog.show(this, null, getString(R.string.refreshingData));
		int questionId = getIntent().getIntExtra("de.codekicker.app.android.SelectedQuestionId", -1);
		Intent intent = new Intent(this, QuestionDetailsDownloader.class);
		intent.putExtra("de.codekicker.app.android.QuestionId", questionId);
		startService(intent);
		setContentView(R.layout.question_details);
//		progressDialog = ProgressDialog.show(this, null, getString(R.string.refreshingData));
//		Question selectedQuestion = getIntent().getParcelableExtra("de.codekicker.app.android.SelectedQuestion");
//		TextView textViewHeadline = (TextView) findViewById(R.id.textViewHeadline);
//		TextView textViewQuestion = (TextView) findViewById(R.id.textViewQuestion);
//		imageButtonUpvote = (ImageButton) findViewById(R.id.imageButtonUpvote);
//		imageButtonDownvote = (ImageButton) findViewById(R.id.imageButtonDownvote);
//		TextView textViewAnswersCount = (TextView) findViewById(R.id.textViewAnswersCount);
//		Button buttonAnswer = (Button) findViewById(R.id.buttonAnswer);
//		buttonAnswer.setOnClickListener(this);
//		imageButtonUpvote.setOnClickListener(this);
//		imageButtonDownvote.setOnClickListener(this);
//		textViewHeadline.setText(selectedQuestion.getHeadline());
//		textViewQuestion.setText(selectedQuestion.getQuestion());
//		textViewAnswersCount.setText(String.format(getString(R.string.answersCount), 1));
//		handler.postDelayed(new Runnable() {
//			@Override
//			public void run() {
//				progressDialog.hide();
//			}
//		}, 5000);
	}
	
	private void foo() {
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