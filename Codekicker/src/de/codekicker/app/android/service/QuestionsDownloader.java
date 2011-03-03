package de.codekicker.app.android.service;

import java.util.ArrayList;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import de.codekicker.app.android.model.Question;

public class QuestionsDownloader extends IntentService {
	private static final String TAG = "QuestionsDownloader";
	
	public QuestionsDownloader() {
		super("QuestionsDownloader");
	}
	
	@Override
	protected void onHandleIntent(Intent intent) {
		Log.v(TAG, "onHandleIntent()");
		// TODO: Downloading questions
		ArrayList<Question> questions = new ArrayList<Question>(5);
    	for (int i = 0; i < 15; i++) {
    		Question q = new Question("Lorem ipsum dolor sit amet " + i + "?", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. In aliquam, arcu sit amet blandit tempor, sem felis ultricies dolor, quis faucibus magna mauris quis ipsum. Duis massa nisl, fermentum ac sollicitudin a, pharetra convallis nisi. Aenean eleifend egestas ullamcorper. Pellentesque vitae elit dolor. Nulla tempus leo ut turpis mattis rutrum. Phasellus placerat volutpat elementum. Fusce mi mauris, auctor sed malesuada non, interdum nec erat. Fusce ac felis id felis vulputate congue ut non eros. Vestibulum ut metus mi, non mattis massa. Duis convallis, elit et semper placerat, purus urna bibendum ante, id accumsan enim dui vitae tellus. In facilisis lectus vel ante dictum viverra. Ut adipiscing tincidunt tellus, eu imperdiet tortor iaculis in. In sagittis gravida congue. Vestibulum adipiscing bibendum molestie. Curabitur laoreet porttitor nisi vitae porttitor.", 5, 0, 27, new String[] {"lorem", "ipsum", "dolor", "sit", "amet"}, "Logdog82");
    		questions.add(q);
    	}
		try {
			// Simulate long download process
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Intent broadcastIntent = new Intent("de.codekicker.app.android.QUESTIONS_DOWNLOAD_FINISHED");
		broadcastIntent.putParcelableArrayListExtra("de.codekicker.app.android.Questions", questions);
		sendBroadcast(broadcastIntent);
	}
}