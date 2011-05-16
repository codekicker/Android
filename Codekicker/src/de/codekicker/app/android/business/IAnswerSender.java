package de.codekicker.app.android.business;

import de.codekicker.app.android.model.Question;

public interface IAnswerSender {
	public interface AnswerSentCallback {
		void answerSent(boolean successful, String serverErrorMessage);
	}
	void sendAnswer(Question question, String answer, AnswerSentCallback callback);
}