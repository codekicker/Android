package de.codekicker.app.android.activity;

import java.util.List;

import roboguice.activity.RoboListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.inject.Inject;

import de.codekicker.app.android.R;
import de.codekicker.app.android.business.INetwork;
import de.codekicker.app.android.business.IQuestionListDownloader;
import de.codekicker.app.android.business.IQuestionListDownloader.DownloadDoneCallback;
import de.codekicker.app.android.model.Question;
import de.codekicker.app.android.widget.IQuestionsListAdapter;

public class QuestionList extends RoboListActivity {
	private static final String TAG = "QuestionList";
	@Inject private INetwork network;
	@Inject private IQuestionListDownloader questionListDownloader;
	@Inject private IQuestionsListAdapter listAdapter;
	private ProgressDialog progressDialog;
	private Question[] questions;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setListAdapter(listAdapter);
		// Handle NonConfigurationInstace because screen orientation could have changed
		Object nonConfigurationInstance = getLastNonConfigurationInstance();
		if (nonConfigurationInstance == null) {
			downloadQuestions(true);
		} else {
			questions = (Question[]) nonConfigurationInstance;
			fillList();
		}
	}
	
	@Override
	public Object onRetainNonConfigurationInstance() {
		return questions;
	}

	@Override
	protected void onListItemClick(ListView listView, View view, int position, long id) {
		Question selectedQuestion = (Question) listView.getItemAtPosition(position);
		Intent intent = new Intent(this, QuestionDetails.class);
		intent.putExtra("de.codekicker.app.android.SelectedQuestion", selectedQuestion);
		startActivityForResult(intent, RESULT_FIRST_USER + 1);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == QuestionDetailsResultCodes.ANSWERED) {
			Log.v(TAG, "User has answered. Now refreshing list.");
			downloadQuestions(false);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.questions_list_options_menu, menu);
		return true;
	}

	private void downloadQuestions(boolean finishIfOffline) {
		if (!network.isOnline()) {
			Toast.makeText(this, R.string.NetworkNotConnected, Toast.LENGTH_LONG).show();
			if (finishIfOffline)
				finish();
			return;
		}
		progressDialog = ProgressDialog.show(this, null, getString(R.string.refreshingData));
		questionListDownloader.downloadQuestions(new DownloadDoneCallback() {
			@Override
			public void downloadDone(List<Question> questions) {
				Log.v(TAG, "Questions downloaded");
				QuestionList.this.questions = questions.toArray(new Question[0]);
				fillList();
				progressDialog.dismiss();
			}
		});
	}
	
	private void fillList() {
		Log.v(TAG, "Adding questions to list");
		// Avoid multiple notifications to the view
		listAdapter.setNotifyOnChange(false);
		listAdapter.clear();
		for (Question p : questions) {
			listAdapter.add(p);
		}
		listAdapter.notifyDataSetChanged();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menuItemPreferences:
			Log.v(TAG, "Preferences menu item selected.");
			startActivity(new Intent(this, Preferences.class));
			return true;
		case R.id.menuItemRefresh:
			Log.v(TAG, "Refresh menu item selected.");
			downloadQuestions(false);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}