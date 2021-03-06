package com.captechconsulting.captechbuzz.model.images;

import com.android.volley.toolbox.ImageLoader.ImageCache;

import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

/**
 * Basic LRU Memory cache.
 * 
 * @author Trey Robinson
 *
 */
public class BitmapLruImageCache extends LruCache<String, Bitmap> implements ImageCache{
	
	private final String TAG = this.getClass().getSimpleName();
	
	public BitmapLruImageCache(int maxSize) {
		super(maxSize);
	}
	
	@Override
	protected int sizeOf(String key, Bitmap value) {
		return value.getRowBytes() * value.getHeight();
	}
	
	@Override
	public Bitmap getBitmap(String url) {
		Log.v(TAG, "Retrieved item from Mem Cache");
		return get(url);
	}
 
	@Override
	public void putBitmap(String url, Bitmap bitmap) {
		Log.v(TAG, "Added item to Mem Cache");
		put(url, bitmap);
	}
}
