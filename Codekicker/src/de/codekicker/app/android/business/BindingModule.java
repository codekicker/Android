package de.codekicker.app.android.business;

import roboguice.config.AbstractAndroidModule;

public class BindingModule extends AbstractAndroidModule {

	@Override
	protected void configure() {
		bind(INetwork.class).to(Network.class);
		bind(IAuthenticator.class).to(Authenticator.class);
		bind(IServerRequest.class).to(ServerRequest.class);
		bind(IGravatarBitmapDownloader.class).to(GravatarBitmapDownloader.class);
	}
}