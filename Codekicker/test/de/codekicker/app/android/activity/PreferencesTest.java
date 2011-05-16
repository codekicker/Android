package de.codekicker.app.android.activity;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.xtremelabs.robolectric.RobolectricTestRunner;
import com.xtremelabs.robolectric.shadows.ShadowToast;

import de.codekicker.app.android.R;
import de.codekicker.app.android.business.IAuthenticator;
import de.codekicker.app.android.business.IAuthenticator.SuccessCallback;
import de.codekicker.app.android.preference.IPreferenceManager;

@RunWith(RobolectricTestRunner.class)
public class PreferencesTest {
	@Mock private IPreferenceManager mockPreferenceManager;
	@Mock private IAuthenticator mockAuthenticator;
	private Preferences activity;
	
	@Before
	public void before() {
		MockitoAnnotations.initMocks(this);
		activity = new Preferences(mockPreferenceManager, mockAuthenticator);
	}
	
	@Test
	public void onBackPressedWithValidCredentials() {
		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				SuccessCallback successCallback = (SuccessCallback) invocation.getArguments()[2];
				successCallback.authenticationDone(true);
				return null;
			}
		}).when(mockAuthenticator).verifyCredentials(anyString(), anyString(), any(SuccessCallback.class));
		when(mockPreferenceManager.getUsername()).thenReturn("foo");
		when(mockPreferenceManager.getPassword()).thenReturn("bar");
		activity.onBackPressed();
		verify(mockAuthenticator).verifyCredentials(eq("foo"), eq("bar"), any(SuccessCallback.class));
		verify(mockPreferenceManager).setIsUserAuthenticated(true);
		String toastMessage = activity.getResources().getString(R.string.successfulLoggedIn);
		assertEquals(toastMessage, ShadowToast.getTextOfLatestToast());
	}
	
	@Test
	public void onBackPressedWithInvalidCredentials() {
		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				SuccessCallback successCallback = (SuccessCallback) invocation.getArguments()[2];
				successCallback.authenticationDone(false);
				return null;
			}
		}).when(mockAuthenticator).verifyCredentials(anyString(), anyString(), any(SuccessCallback.class));
		when(mockPreferenceManager.getUsername()).thenReturn("foo");
		when(mockPreferenceManager.getPassword()).thenReturn("bar");
		activity.onBackPressed();
		verify(mockAuthenticator).verifyCredentials(eq("foo"), eq("bar"), any(SuccessCallback.class));
		verify(mockPreferenceManager).setIsUserAuthenticated(false);
		String toastMessage = activity.getResources().getString(R.string.errorCredentials);
		assertEquals(toastMessage, ShadowToast.getTextOfLatestToast());
	}
}