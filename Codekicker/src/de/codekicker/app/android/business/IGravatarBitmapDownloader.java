package de.codekicker.app.android.business;

import android.graphics.Bitmap;

public interface IGravatarBitmapDownloader {
	Bitmap downloadBitmap(String gravatarHash);
}