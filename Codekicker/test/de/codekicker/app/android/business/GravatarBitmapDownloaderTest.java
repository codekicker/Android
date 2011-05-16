package de.codekicker.app.android.business;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.xtremelabs.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class GravatarBitmapDownloaderTest {
	private IGravatarBitmapDownloader gravatarBitmapDownloader;
	
	@Before
	public void before() {
		gravatarBitmapDownloader = new GravatarBitmapDownloader();
	}
	
	@Test
	@Ignore
	public void downloadBitmap() {
		throw new UnsupportedOperationException();
	}
}