package de.codekicker.app.android.business;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.xtremelabs.robolectric.RobolectricTestRunner;
import com.xtremelabs.robolectric.shadows.ShadowAsyncTask;

import de.codekicker.app.android.business.IAuthenticator.SuccessCallback;
import de.codekicker.app.android.preference.IPreferenceManager;

@RunWith(RobolectricTestRunner.class)
public class AuthenticatorTest {
	@Mock private IPreferenceManager mockPreferenceManager;
	@Mock private IServerRequest mockServerRequest;
	private boolean successCallbackCalled;
	private boolean credentialsValid;
	private IAuthenticator authenticator;
	
	@Before
	public void before() {
		MockitoAnnotations.initMocks(this);
		when(mockPreferenceManager.getApiBaseUrl()).thenReturn("http://localhost/");
		authenticator = new Authenticator(mockPreferenceManager, mockServerRequest);
		successCallbackCalled = false;
		credentialsValid = false;
	}
	
	@Test
	@Ignore("How can I test an AsyncTask?")
	public void verifyCredentials() {
		throw new UnsupportedOperationException();
	}
}