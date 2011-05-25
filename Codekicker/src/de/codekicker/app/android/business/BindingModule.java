package de.codekicker.app.android.business;

import roboguice.config.AbstractAndroidModule;

public class BindingModule extends AbstractAndroidModule {

	@Override
	protected void configure() {
		bind(IAuthenticator.class).to(Authenticator.class);
		bind(INetwork.class).to(Network.class);
		bind(IServerRequest.class).to(ServerRequest.class);
		bind(IQuestionListDownloader.class).to(QuestionListDownloader.class);
		bind(IGravatarBitmapDownloader.class).to(GravatarBitmapDownloader.class);
		bind(IAnswersDownloader.class).to(AnswersDownloader.class);
		bind(IAnswerSender.class).to(AnswerSender.class);
		bind(IVoter.class).to(Voter.class);
		bind(IVoteDoneCallbackFactory.class).to(VoteDoneCallbackFactory.class);
	}
}