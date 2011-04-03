package de.codekicker.app.android.activity;

import java.text.DateFormat;
import java.util.Date;

import android.app.ListActivity;
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
import android.widget.ListView;
import android.widget.TextView;
import de.codekicker.app.android.R;
import de.codekicker.app.android.model.Question;
import de.codekicker.app.android.model.User;
import de.codekicker.app.android.service.QuestionDetailsDownloader;
import de.codekicker.app.android.widget.QuestionDetailsAdapter;

public class QuestionDetails extends ListActivity implements OnClickListener {
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
	
	@Override
	protected void onListItemClick(ListView listView, View view, int position, long id) {
		super.onListItemClick(listView, view, position, id);
	}
	
	private void fillView(Question question) {
		Log.v(TAG, "Filling view");
		User user = question.getUser();
		LayoutInflater layoutInflater = getLayoutInflater();
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
		QuestionDetailsAdapter adapter = new QuestionDetailsAdapter(this, R.layout.question_details_list_item, question.getAnswers());
		setListAdapter(adapter);
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