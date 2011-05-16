package de.codekicker.app.android.business;

import de.codekicker.app.android.model.Question;

public interface IQuestionDetailsDownloader {
	public interface DownloadDoneCallback {
		void downloadDone(Question question);
	}
	void downloadDetails(Question question, DownloadDoneCallback callback);
}