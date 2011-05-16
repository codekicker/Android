package de.codekicker.app.android.business;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

class GravatarBitmapDownloader implements IGravatarBitmapDownloader {
	private static final String TAG = "GravatarBitmapDownloader";
	private static final String BASE_URL = "http://www.gravatar.com/avatar/%s?s=40";
	
	@Override
	public Bitmap downloadBitmap(String gravatarHash) {
		String downloadUrl = String.format(BASE_URL, gravatarHash);
		Log.v(TAG, "Downloading Gravatar image. URL: " + downloadUrl);
		InputStream inputStream = null;
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(downloadUrl);
			inputStream = httpClient.execute(httpGet).getEntity().getContent();
			return BitmapFactory.decodeStream(inputStream);
		} catch (IllegalStateException e) {
			Log.e(TAG, e.getMessage(), e);
		} catch (ClientProtocolException e) {
			Log.e(TAG, e.getMessage(), e);
		} catch (IOException e) {
			Log.e(TAG, e.getMessage(), e);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					Log.e(TAG, e.getMessage(), e);
				}
			}
		}
		return null;
	}
}