package de.codekicker.app.android.business;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.xtremelabs.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class NetworkTest {
	@Mock private ConnectivityManager mockConnectivityManager;
	@Mock private NetworkInfo mockNetworkInfo;
	private INetwork network;
	
	@Before
	public void before() {
		MockitoAnnotations.initMocks(this);
		when(mockConnectivityManager.getActiveNetworkInfo()).thenReturn(mockNetworkInfo);
		network = new Network(mockConnectivityManager);
	}
	
	@Test
	public void isOnline() {
		when(mockNetworkInfo.isConnected()).thenReturn(true);
		boolean isOnline = network.isOnline();
		verify(mockConnectivityManager).getActiveNetworkInfo();
		assertTrue(isOnline);
	}
	
	@Test
	public void isNotOnline() {
		boolean isOnline = network.isOnline(); // mockNetworkInfo.isConnected() return false
		assertFalse(isOnline);
		when(mockConnectivityManager.getActiveNetworkInfo()).thenReturn(null);
		isOnline = network.isOnline(); // mockNetworkInfo.isConnected() return false
		assertFalse(isOnline);
	}
}