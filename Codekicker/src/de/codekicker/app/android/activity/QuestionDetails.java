package de.codekicker.app.android.activity;

import java.text.DateFormat;
import java.util.Date;

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
import android.widget.ImageView;
import android.widget.TextView;
import de.codekicker.app.android.R;
import de.codekicker.app.android.model.Question;
import de.codekicker.app.android.model.User;
import de.codekicker.app.android.service.QuestionDetailsDownloader;

public class QuestionDetails extends Activity implements OnClickListener {
	private static final String TAG = "QuestionDetails";
	private ProgressDialog progressDialog;
	private ImageButton imageButtonUpvote;
	private ImageButton imageButtonDownvote;
	private Question question;
	private BroadcastReceiver questionDownloadedReceiver = new BroadcastReceiver() {
		private static final String TAG = "QuestionDownloadedReceiver";
		
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.v(TAG, "Broadcast received");
			question = (Question) intent.getParcelableExtra("de.codekicker.app.android.Question");
			setContentView(R.layout.question_details);
			fillView(question);
			progressDialog.hide();
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		registerReceiver(questionDownloadedReceiver, new IntentFilter("de.codekicker.app.android.QUESTION_DOWNLOAD_FINISHED"));
		Object nonConfigurationInstance = getLastNonConfigurationInstance();
		if (nonConfigurationInstance == null) {
			progressDialog = ProgressDialog.show(this, null, getString(R.string.refreshingData));
			Question question = getIntent().getParcelableExtra("de.codekicker.app.android.SelectedQuestion");
			Intent intent = new Intent(this, QuestionDetailsDownloader.class);
			intent.putExtra("de.codekicker.app.android.Question", question);
			startService(intent);
		} else {
			question = (Question) nonConfigurationInstance;
			setContentView(R.layout.question_details);
			fillView(question);
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(questionDownloadedReceiver);
	}
	
	@Override
	public Object onRetainNonConfigurationInstance() {
		return question;
	}
	
	private void fillView(Question question) {
		Log.v(TAG, "Fill view");
		User user = question.getUser();
		TextView textViewTitle = (TextView) findViewById(R.id.textViewTitle);
		TextView textViewQuestionBody = (TextView) findViewById(R.id.textViewQuestionBody);
		TextView textViewAskDate = (TextView) findViewById(R.id.textViewAskDate);
		TextView textViewVoteScore = (TextView) findViewById(R.id.textViewVoteScore);
		ImageView imageViewGravatar = (ImageView) findViewById(R.id.imageViewGravatar);
		TextView textViewUserName = (TextView) findViewById(R.id.textViewUserName);
		TextView textViewAnswerCount = (TextView) findViewById(R.id.textViewAnswerCount);
		textViewTitle.setText(question.getTitle());
		textViewQuestionBody.setText(question.getQuestionBody());
		Date askDate = question.getAskDate();
		String askDateString = DateFormat.getDateInstance(DateFormat.SHORT).format(askDate);
		String askTimeString = DateFormat.getTimeInstance(DateFormat.SHORT).format(askDate);
		askDateString = String.format(getString(R.string.askDate), askDateString, askTimeString);
		textViewAskDate.setText(askDateString);
		textViewVoteScore.setText(Integer.toString(question.getVoteScore()));
		imageViewGravatar.setImageBitmap(user.getGravatar());
		textViewUserName.setText(user.getName() != null ? user.getName() : getString(R.string.guest));
		textViewAnswerCount.setText(String.format(getString(R.string.answersCount), question.getAnswerCount()));
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