package de.codekicker.app.android.activity;

import java.text.DateFormat;
import java.util.Date;

import roboguice.activity.RoboListActivity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import de.codekicker.app.android.business.INetwork;
import de.codekicker.app.android.model.Question;
import de.codekicker.app.android.model.User;
import de.codekicker.app.android.preference.IPreferenceManager;
import de.codekicker.app.android.service.QuestionDetailsDownloader;
import de.codekicker.app.android.service.SendAnswerService;
import de.codekicker.app.android.widget.QuestionDetailsAdapter;

public class QuestionDetails extends RoboListActivity implements OnClickListener {
	private static final String TAG = "QuestionDetails";
	@Inject private LayoutInflater layoutInflater;
	@Inject private IPreferenceManager preferenceManager;
	@Inject private INetwork network;
	private ProgressDialog progressDialog;
	private Question question;
	private EditText editTextYourAnswer;
	private BroadcastReceiver questionDownloadedReceiver = new BroadcastReceiver() {
		private static final String TAG = "QuestionDownloadedReceiver";
		
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.v(TAG, "Broadcast received");
			question = (Question) intent.getParcelableExtra("de.codekicker.app.android.Question");
			fillView(question);
			progressDialog.dismiss();
		}
	};
	private BroadcastReceiver answerSentReceiver = new BroadcastReceiver() {
		private static final String TAG = "AnswerSentReceiver";
		
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.v(TAG, "Broadcast received");
			boolean successful = intent.getBooleanExtra("successful", false);
			progressDialog.dismiss();
			if (successful) {
				Log.v(TAG, "Answer was successful sent. Navigating back.");
				QuestionDetails.this.setResult(QuestionDetailsResultCodes.ANSWERED);
				QuestionDetails.this.finish();
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		registerForContextMenu(getListView());
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
			fillView(question);
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		registerReceiver(questionDownloadedReceiver, new IntentFilter("de.codekicker.app.android.QUESTION_DOWNLOAD_FINISHED"));
		registerReceiver(answerSentReceiver, new IntentFilter("de.codekicker.app.android.ANSWER_SENT"));
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(questionDownloadedReceiver);
		unregisterReceiver(answerSentReceiver);
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.questions_details_context_menu, menu);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menuItemUpvote:
			// TODO: Upvote
			return true;
		case R.id.menuItemDownvote:
			// TODO: Downvote
			return true;
		default:
			return super.onContextItemSelected(item);
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
		QuestionDetailsAdapter adapter = new QuestionDetailsAdapter(this, R.layout.question_details_list_item, question.getAnswers(), layoutInflater);
		setListAdapter(adapter);
		int visibility = preferenceManager.getIsUserAuthenticated() ? View.VISIBLE : View.GONE;
		Button buttonAnswer = (Button) findViewById(R.id.buttonAnswer);
		buttonAnswer.setVisibility(visibility);
		buttonAnswer.setOnClickListener(this);
		editTextYourAnswer = (EditText) findViewById(R.id.editTextYourAnswer);
		editTextYourAnswer.setVisibility(visibility);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.buttonAnswer:
			if (!network.isOnline()) {
				Toast.makeText(this, R.string.NetworkNotConnected, Toast.LENGTH_LONG).show();
				return;
			}
			progressDialog = ProgressDialog.show(this, null, getString(R.string.sendingAnswer));
			Intent intent = new Intent(this, SendAnswerService.class);
			intent.putExtra("de.codekicker.app.android.Question", question);
			intent.putExtra("de.codekicker.app.android.AnswerBody", editTextYourAnswer.getText().toString());
			startService(intent);
			break;
		}
	}
}