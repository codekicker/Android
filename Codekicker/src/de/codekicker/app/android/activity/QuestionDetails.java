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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import de.codekicker.app.android.R;
import de.codekicker.app.android.model.Answer;
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
	
	private void questionDownloaded(Question question) {
		Log.v(TAG, "Fill view");
		TextView textViewHeadLine = (TextView) findViewById(R.id.textViewHeadline);
		TextView textViewQuestion = (TextView) findViewById(R.id.textViewQuestion);
		TextView textViewElapsedTime = (TextView) findViewById(R.id.textViewElapsedTime);
		TextView textViewAuthor = (TextView) findViewById(R.id.textViewAuthor);
		TextView textViewAnswersCount = (TextView) findViewById(R.id.textViewAnswersCount);
		textViewHeadLine.setText(question.getHeadline());
		textViewQuestion.setText(question.getQuestion());
		textViewElapsedTime.setText(String.format(getString(R.string.elapsedTime), question.getElapsedTime()));
		textViewAuthor.setText(question.getFromUsername());
		textViewAnswersCount.setText(String.format(getString(R.string.answersCount), question.getAnswersCount()));
		for (Answer answer : question.getAnswers()) {
			LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			layoutInflater.inflate(R.layout.question_details_answer, (ViewGroup) findViewById(R.id.frameLayoutAnswers));
		}
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