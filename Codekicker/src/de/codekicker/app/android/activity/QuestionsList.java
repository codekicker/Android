package de.codekicker.app.android.activity;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import de.codekicker.app.android.R;
import de.codekicker.app.android.model.Question;
import de.codekicker.app.android.service.QuestionsDownloader;
import de.codekicker.app.android.widget.QuestionsListAdapter;

public class QuestionsList extends ListActivity {
	private static final String TAG = "QuestionsList";
	private Handler handler = new Handler();
	private QuestionsListAdapter listAdapter;
	private ProgressDialog progressDialog;
	private BroadcastReceiver questionsDownloadedReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, final Intent intent) {
			// Call to the UI thread because this callback is in another Thread
			handler.post(new Runnable() {
				@Override
				public void run() {
					// Avoid multiple notifications to the view
					listAdapter.setNotifyOnChange(false);
					for (Parcelable p : intent.getParcelableArrayListExtra("de.codekicker.app.android.Questions")) {
						listAdapter.add((Question) p);
					}
					listAdapter.notifyDataSetChanged();
					progressDialog.hide();
				}
			});
		}
	};
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerReceiver(questionsDownloadedReceiver, new IntentFilter("de.codekicker.app.android.QUESTIONS_DOWNLOAD_FINISHED"));
    	listAdapter = new QuestionsListAdapter(this, R.layout.questions_list_item);
        setListAdapter(listAdapter);
        registerForContextMenu(getListView());
        downloadQuestions();
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
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
    	case R.id.menuItemProfile:
    		Log.v(TAG, "Profile menu item selected.");
    		return true;
    	case R.id.menuItemPreferences:
    		Log.v(TAG, "Preferences menu item selected.");
    		return true;
    	case R.id.menuItemRefresh:
    		Log.v(TAG, "Refresh menu item selected.");
    		downloadQuestions();
    		return true;
    	default:
    		return super.onOptionsItemSelected(item);
    	}
    }

	private void downloadQuestions() {
		progressDialog = ProgressDialog.show(this, null, getString(R.string.refreshingData));
		Intent intent = new Intent(this, QuestionsDownloader.class);
		startService(intent);
	}
}