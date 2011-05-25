package de.codekicker.app.android.business;

import java.util.List;

import de.codekicker.app.android.model.Answer;
import de.codekicker.app.android.model.Question;

public interface IAnswersDownloader {
	public interface DownloadDoneCallback {
		void downloadDone(List<Answer> answers);
	}
	void downloadDetails(Question question, DownloadDoneCallback callback);
}