package de.codekicker.app.android.activity;

import java.util.ArrayList;

import roboguice.activity.RoboListActivity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcelable;
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
import de.codekicker.app.android.model.Question;
import de.codekicker.app.android.preference.IPreferenceManager;
import de.codekicker.app.android.service.QuestionListDownloader;
import de.codekicker.app.android.widget.IQuestionsListAdapter;

public class QuestionList extends RoboListActivity {
	private static final String TAG = "QuestionList";
	@Inject private INetwork network;
	@Inject private IPreferenceManager preferenceManager;
	@Inject private IQuestionsListAdapter listAdapter;
	private ProgressDialog progressDialog;
	private Question[] questions;
	private BroadcastReceiver questionsDownloadedReceiver = new BroadcastReceiver() {
		private static final String TAG = "QuestionsDownloadedReceiver";
		
		@Override
		public void onReceive(Context context, final Intent intent) {
			Log.v(TAG, "Questions downloaded");
			ArrayList<Parcelable> parcelables = intent.getParcelableArrayListExtra("de.codekicker.app.android.Questions");
			questions = parcelables.toArray(new Question[0]);
			fillList();
			progressDialog.hide();
		}
	};
	
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
	protected void onResume() {
		super.onResume();
		registerReceiver(questionsDownloadedReceiver, new IntentFilter("de.codekicker.app.android.QUESTIONS_DOWNLOAD_FINISHED"));
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(questionsDownloadedReceiver);
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
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.questions_list_options_menu, menu);
		return true;
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		MenuItem profileMenuItem = menu.findItem(R.id.menuItemProfile);
		profileMenuItem.setEnabled(preferenceManager.getIsUserAuthenticated());
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
		Intent intent = new Intent(this, QuestionListDownloader.class);
		startService(intent);
	}
	
	private void fillList() {
		Log.v(TAG, "Adding questions to list");
		// Avoid multiple notifications to the view
		listAdapter.setNotifyOnChange(false);
		for (Question p : questions) {
			listAdapter.add(p);
		}
		listAdapter.notifyDataSetChanged();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menuItemProfile:
			Log.v(TAG, "Profile menu item selected.");
			return true;
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