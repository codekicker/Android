package de.codekicker.app.android.business;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.xtremelabs.robolectric.RobolectricTestRunner;

import de.codekicker.app.android.preference.IPreferenceManager;

@RunWith(RobolectricTestRunner.class)
public class QuestionListDownloaderTest {
	@Mock private IPreferenceManager mockPreferenceManager;
	@Mock private IServerRequest mockServerRequest;
	private IQuestionListDownloader questionListDownloader;
	
	@Before
	public void before() {
		MockitoAnnotations.initMocks(this);
		questionListDownloader = new QuestionListDownloader(mockPreferenceManager, mockServerRequest);
	}
	
	@Test
	@Ignore
	public void downloadQuestions() {
		throw new UnsupportedOperationException();
	}
}