package de.codekicker.app.android.activity;

import java.text.DateFormat;
import java.util.Date;

import roboguice.activity.RoboListActivity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.inject.Inject;

import de.codekicker.app.android.R;
import de.codekicker.app.android.business.IAnswerSender;
import de.codekicker.app.android.business.IAnswerSender.AnswerSentCallback;
import de.codekicker.app.android.business.INetwork;
import de.codekicker.app.android.business.IQuestionDetailsDownloader;
import de.codekicker.app.android.business.IQuestionDetailsDownloader.DownloadDoneCallback;
import de.codekicker.app.android.business.IVoteDoneCallbackFactory;
import de.codekicker.app.android.business.IVoter;
import de.codekicker.app.android.business.VoteType;
import de.codekicker.app.android.model.Answer;
import de.codekicker.app.android.model.Question;
import de.codekicker.app.android.model.User;
import de.codekicker.app.android.preference.IPreferenceManager;
import de.codekicker.app.android.widget.QuestionDetailsAdapter;

public class QuestionDetails extends RoboListActivity implements OnClickListener {
	private static final String TAG = "QuestionDetails";
	@Inject private LayoutInflater layoutInflater;
	@Inject private IPreferenceManager preferenceManager;
	@Inject private IQuestionDetailsDownloader questionDetailsDownloader;
	@Inject private INetwork network;
	@Inject private IAnswerSender answerSender;
	@Inject private IVoter voter;
	@Inject private IVoteDoneCallbackFactory voteDoneCallbackFactory;
	private ProgressDialog progressDialog;
	private Question question;
	private EditText editTextYourAnswer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		registerForContextMenu(getListView());
		// Handle NonConfigurationInstace because screen orientation could have changed
		Object nonConfigurationInstance = getLastNonConfigurationInstance();
		if (nonConfigurationInstance == null) {
			progressDialog = ProgressDialog.show(this, null, getString(R.string.refreshingData));
			question = getIntent().getParcelableExtra("de.codekicker.app.android.SelectedQuestion");
			questionDetailsDownloader.downloadDetails(question, new DownloadDoneCallback() {
				@Override
				public void downloadDone(Question question) {
					fillView(question);
					progressDialog.dismiss();
				}
			});
		} else {
			question = (Question) nonConfigurationInstance;
			fillView(question);
		}
	}
	
	@Override
	public Object onRetainNonConfigurationInstance() {
		return question;
	}
	
	private void fillView(Question question) {
		Log.v(TAG, "Filling view");
		User user = question.getUser();
		LinearLayout headerLinearLayout = (LinearLayout) layoutInflater.inflate(R.layout.question_details_header, null);
		LinearLayout footerLinearLayout = (LinearLayout) layoutInflater.inflate(R.layout.question_details_footer, null);
		Date date = question.getAskDate();
		String askDateString = DateFormat.getDateInstance(DateFormat.SHORT).format(date);
		String askTimeString = DateFormat.getTimeInstance(DateFormat.SHORT).format(date);
		askDateString = String.format(getString(R.string.askDate), askDateString, askTimeString);
		TextView textViewTitle = (TextView) headerLinearLayout.findViewById(R.id.textViewTitle);
		TextView textViewVoteScore = (TextView) headerLinearLayout.findViewById(R.id.textViewVoteScore);
		TextView textViewQuestionBody = (TextView) headerLinearLayout.findViewById(R.id.textViewQuestionBody);
		TextView textViewaskDate = (TextView) headerLinearLayout.findViewById(R.id.textViewAskDate);
		ImageView imageViewGravatar = (ImageView) headerLinearLayout.findViewById(R.id.imageViewGravatar);
		TextView textViewUserName = (TextView) headerLinearLayout.findViewById(R.id.textViewUserName);
		TextView textViewReputation = (TextView) headerLinearLayout.findViewById(R.id.textViewReputation);
		TextView textViewAnswerCount = (TextView) headerLinearLayout.findViewById(R.id.textViewAnswerCount);
		TextView textViewComments = (TextView) headerLinearLayout.findViewById(R.id.textViewComments);
		textViewTitle.setText(question.getTitle());
		textViewVoteScore.setText(Integer.toString(question.getVoteScore()));
		textViewQuestionBody.setText(question.getQuestionBody());
		textViewaskDate.setText(askDateString);
		imageViewGravatar.setImageBitmap(user.getGravatar());
		textViewUserName.setText(user.getName());
		textViewReputation.setText(Integer.toString(user.getReputation()));
		int answerCountText = question.getAnswerCount() == 1 ? R.string.answerCount : R.string.answersCount;
		textViewAnswerCount.setText(String.format(getString(answerCountText), question.getAnswerCount()));
		int commentCountText = 0 == 1 ? R.string.commentCount : R.string.commentsCount;
		textViewComments.setText(String.format(getString(commentCountText), 0));
		ListView listView = getListView();
		listView.addHeaderView(headerLinearLayout);
		listView.addFooterView(footerLinearLayout);
		ImageView imageViewUpvote = (ImageView) findViewById(R.id.imageViewUpvote);
		imageViewUpvote.setEnabled(preferenceManager.isUserAuthenticated());
		imageViewUpvote.setOnClickListener(this);
		ImageView imageViewDownvote = (ImageView) findViewById(R.id.imageViewDownvote);
		imageViewDownvote.setEnabled(preferenceManager.isUserAuthenticated());
		imageViewDownvote.setOnClickListener(this);
		int visibility = preferenceManager.isUserAuthenticated() ? View.VISIBLE : View.GONE;
		Button buttonAnswer = (Button) findViewById(R.id.buttonAnswer);
		buttonAnswer.setVisibility(visibility);
		buttonAnswer.setOnClickListener(this);
		editTextYourAnswer = (EditText) findViewById(R.id.editTextYourAnswer);
		editTextYourAnswer.setVisibility(visibility);
		QuestionDetailsAdapter adapter = new QuestionDetailsAdapter(this, preferenceManager, R.layout.question_details_list_item, question.getAnswers(), layoutInflater);
		setListAdapter(adapter);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.imageViewUpvote:
			voteUp(0, question.getAnswerId());
			break;
		case R.id.imageViewDownvote:
			voteDown(0, question.getAnswerId());
			break;
		case R.id.buttonAnswer:
			sendAnswer();
			break;
		}
	}
	
	public void onUpvoteClick(int rowPosition, Answer answer) {
		voteUp(rowPosition, answer.getId());
	}
	
	public void onDownvoteClick(int rowPosition, Answer answer) {
		voteDown(rowPosition, answer.getId());
	}
	
	private void sendAnswer() {
		if (!network.isOnline()) {
			Toast.makeText(this, R.string.NetworkNotConnected, Toast.LENGTH_LONG).show();
			return;
		}
		progressDialog = ProgressDialog.show(this, null, getString(R.string.sendingAnswer));
		answerSender.sendAnswer(question, editTextYourAnswer.getText().toString(), new AnswerSentCallback() {
			@Override
			public void answerSent(boolean successful, String serverErrorMessage) {
				progressDialog.dismiss();
				if (successful) {
					Log.v(TAG, "Answer was successful sent. Navigating back");
					Toast.makeText(QuestionDetails.this, QuestionDetails.this.getString(R.string.sendAnswerSuccessful), Toast.LENGTH_LONG).show();
					QuestionDetails.this.setResult(QuestionDetailsResultCodes.ANSWERED);
					QuestionDetails.this.finish();
				} else {
					Log.e(TAG, "Answer not successful sent");
					String toastText = serverErrorMessage == null ? QuestionDetails.this.getString(R.string.sendAnswerFailed) : serverErrorMessage;
					Toast.makeText(QuestionDetails.this, toastText, Toast.LENGTH_LONG).show();
				}
			}
		});
	}
	
	private void voteUp(int rowPosition, int id) {
		if (network.isOnline()) {
			voter.voteUp(id, voteDoneCallbackFactory.create(this, rowPosition, VoteType.UP));
		} else {
			Toast.makeText(this, R.string.NetworkNotConnected, Toast.LENGTH_LONG).show();
		}
	}
	
	private void voteDown(int rowPosition, int id) {
		if (network.isOnline()) {
			voter.voteDown(id, voteDoneCallbackFactory.create(this, rowPosition, VoteType.DOWN));
		} else {
			Toast.makeText(this, R.string.NetworkNotConnected, Toast.LENGTH_LONG).show();
		}
	}
}