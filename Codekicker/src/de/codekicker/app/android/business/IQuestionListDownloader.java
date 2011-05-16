package de.codekicker.app.android.business;

import java.util.List;

import de.codekicker.app.android.model.Question;

public interface IQuestionListDownloader {
	public interface DownloadDoneCallback {
		void downloadDone(List<Question> questions);
	}
	void downloadQuestions(DownloadDoneCallback callback);
}