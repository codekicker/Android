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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import de.codekicker.app.android.R;
import de.codekicker.app.android.model.Answer;
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
		// Handle NonConfigurationInstace because screen orientation could have changed
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
		Log.v(TAG, "Filling view");
		User user = question.getUser();
		LinearLayout rootLinearLayout = (LinearLayout) findViewById(R.id.rootLinearLayout);
		TextView textViewTitle = (TextView) findViewById(R.id.textViewTitle);
		textViewTitle.setText(question.getTitle());
		foo(rootLinearLayout, question.getQuestionBody(), question.getAskDate(), question.getVoteScore(), question.getAnswerCount(), user);
		LayoutInflater layoutInflater = getLayoutInflater();
		LinearLayout linearLayoutAnswers = (LinearLayout) findViewById(R.id.linearLayoutAnswers);
		for (Answer answer : question.getAnswers()) {
			LinearLayout answerLinearLayout = (LinearLayout) layoutInflater.inflate(R.layout.question_details_answer, null);
			TextView textViewQuestionBody = (TextView) answerLinearLayout.findViewById(R.id.textViewQuestionBody);
			textViewQuestionBody.setText(answer.getTextBody());
			linearLayoutAnswers.addView(answerLinearLayout);
		}
	}
	
	private void foo(LinearLayout linearLayout, String questionBody, Date date, int voteScore, int answerCount, User user) {
		TextView textViewQuestionBody = (TextView) linearLayout.findViewById(R.id.textViewQuestionBody);
		TextView textViewaskDate = (TextView) linearLayout.findViewById(R.id.textViewAskDate);
		TextView textViewVoteScore = (TextView) linearLayout.findViewById(R.id.textViewVoteScore);
		ImageView imageViewGravatar = (ImageView) linearLayout.findViewById(R.id.imageViewGravatar);
		TextView textViewUserName = (TextView) linearLayout.findViewById(R.id.textViewUserName);
		TextView textViewReputation = (TextView) linearLayout.findViewById(R.id.textViewReputation);
		TextView textViewAnswerCount = (TextView) linearLayout.findViewById(R.id.textViewAnswerCount);
		textViewQuestionBody.setText(questionBody);
		String askDateString = DateFormat.getDateInstance(DateFormat.SHORT).format(date);
		String askTimeString = DateFormat.getTimeInstance(DateFormat.SHORT).format(date);
		askDateString = String.format(getString(R.string.askDate), askDateString, askTimeString);
		textViewaskDate.setText(askDateString);
		textViewVoteScore.setText(Integer.toString(voteScore));
		imageViewGravatar.setImageBitmap(user.getGravatar());
		textViewUserName.setText(user.getName() != null ? user.getName() : getString(R.string.guest));
		textViewReputation.setText(Integer.toString(user.getReputation()));
		textViewAnswerCount.setText(String.format(getString(R.string.answersCount), answerCount));
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