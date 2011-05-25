package de.codekicker.app.android.activity;

import java.util.ArrayList;
import java.util.List;

import roboguice.activity.RoboListActivity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.inject.Inject;

import de.codekicker.app.android.R;
import de.codekicker.app.android.business.IAnswerSender;
import de.codekicker.app.android.business.IAnswerSender.AnswerSentCallback;
import de.codekicker.app.android.business.INetwork;
import de.codekicker.app.android.business.IAnswersDownloader;
import de.codekicker.app.android.business.IAnswersDownloader.DownloadDoneCallback;
import de.codekicker.app.android.business.IVoteDoneCallbackFactory;
import de.codekicker.app.android.business.IVoter;
import de.codekicker.app.android.business.VoteType;
import de.codekicker.app.android.model.Answer;
import de.codekicker.app.android.model.Question;
import de.codekicker.app.android.preference.IPreferenceManager;
import de.codekicker.app.android.widget.IQuestionDetailsAdapter;

public class QuestionDetails extends RoboListActivity implements OnClickListener {
	private static final String TAG = "QuestionDetails";
	@Inject private LayoutInflater layoutInflater;
	@Inject private IPreferenceManager preferenceManager;
	@Inject private IAnswersDownloader questionDetailsDownloader;
	@Inject private INetwork network;
	@Inject private IAnswerSender answerSender;
	@Inject private IVoter voter;
	@Inject private IVoteDoneCallbackFactory voteDoneCallbackFactory;
	@Inject private IQuestionDetailsAdapter questionDetailsAdapter;
	private ProgressDialog progressDialog;
	private Question question;
	private List<Answer> answers = new ArrayList<Answer>();
	private EditText editTextYourAnswer;
	private boolean commentsVisible;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		question = getIntent().getParcelableExtra("de.codekicker.app.android.SelectedQuestion");
		ListView listView = getListView();
		registerForContextMenu(listView);
		fillHeaderAndFooter(listView);
		// Handle NonConfigurationInstace because screen orientation could have changed
		Object nonConfigurationInstance = getLastNonConfigurationInstance();
		if (nonConfigurationInstance == null) {
			downloadQuestion();
		} else {
			answers = (List<Answer>) nonConfigurationInstance;
			fillView(answers);
		}
	}
	
	private void fillHeaderAndFooter(ListView listView) {
		LinearLayout headerLinearLayout = (LinearLayout) layoutInflater.inflate(R.layout.question_details_header, null);
		LinearLayout footerLinearLayout = (LinearLayout) layoutInflater.inflate(R.layout.question_details_footer, null);
		listView.addHeaderView(headerLinearLayout);
		listView.addFooterView(footerLinearLayout);
		TextView textViewTitle = (TextView) headerLinearLayout.findViewById(R.id.textViewTitle);
		textViewTitle.setText(question.getTitle());
		int visibility = preferenceManager.isUserAuthenticated() ? View.VISIBLE : View.GONE;
		Button buttonAnswer = (Button) findViewById(R.id.buttonAnswer);
		buttonAnswer.setVisibility(visibility);
		buttonAnswer.setOnClickListener(this);
		setListAdapter(questionDetailsAdapter);
		editTextYourAnswer = (EditText) findViewById(R.id.editTextYourAnswer);
		editTextYourAnswer.setVisibility(visibility);
	}
	
	private void downloadQuestion() {
		progressDialog = ProgressDialog.show(this, null, getString(R.string.refreshingData));
		questionDetailsDownloader.downloadDetails(question, new DownloadDoneCallback() {
			@Override
			public void downloadDone(List<Answer> answers) {
				QuestionDetails.this.answers = answers;
				fillView(answers);
				progressDialog.dismiss();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.question_details_options_menu, menu);
		return true;
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.findItem(R.id.menuItemShowComments).setVisible(!commentsVisible);
		menu.findItem(R.id.menuItemHideComments).setVisible(commentsVisible);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menuItemShowComments:
			showComments();
			break;
		case R.id.menuItemHideComments:
			hideComments();
			break;
		case R.id.menuItemRefresh:
			downloadQuestion();
			break;
		}
		return true;
	}

	@Override
	public Object onRetainNonConfigurationInstance() {
		return answers;
	}
	
	private void fillView(List<Answer> answers) {
		Log.v(TAG, "Filling view");
		// Avoid multiple notifications to the view
		questionDetailsAdapter.setNotifyOnChange(false);
		questionDetailsAdapter.clear();
		for (Answer answer : answers) {
			questionDetailsAdapter.add(answer);
		}
		questionDetailsAdapter.notifyDataSetChanged();
	}
	
	private void showComments() {
		commentsVisible = true;
	}

	private void hideComments() {
		commentsVisible = false;
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.buttonAnswer:
			sendAnswer();
			break;
		}
	}
	
	public void onUpvoteClick(int rowPosition, Answer answer) {
		if (network.isOnline()) {
			voter.voteUp(answer.getId(), voteDoneCallbackFactory.create(this, rowPosition, VoteType.UP));
		} else {
			Toast.makeText(this, R.string.NetworkNotConnected, Toast.LENGTH_LONG).show();
		}
	}
	
	public void onDownvoteClick(int rowPosition, Answer answer) {
		if (network.isOnline()) {
			voter.voteDown(answer.getId(), voteDoneCallbackFactory.create(this, rowPosition, VoteType.DOWN));
		} else {
			Toast.makeText(this, R.string.NetworkNotConnected, Toast.LENGTH_LONG).show();
		}
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
}