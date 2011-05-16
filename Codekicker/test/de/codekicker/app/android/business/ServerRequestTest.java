package de.codekicker.app.android.business;

import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.xtremelabs.robolectric.RobolectricTestRunner;

import de.codekicker.app.android.preference.IPreferenceManager;

@RunWith(RobolectricTestRunner.class)
public class ServerRequestTest {
	@Mock private IPreferenceManager mockPreferenceManager;
	private IServerRequest serverRequest;
	
	@Before
	public void before() {
		MockitoAnnotations.initMocks(this);
		serverRequest = new ServerRequest(mockPreferenceManager);
	}
	
	@Test
	@Ignore
	public void downloadJSONWithoutCredentials() {
		throw new UnsupportedOperationException();
	}
}