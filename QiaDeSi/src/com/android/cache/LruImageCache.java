/**
 * 
 */
package com.android.cache;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.LruCache;
import com.android.volley.toolbox.ImageLoader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
/**
 * @author Administrator
 *
 */
public class LruImageCache implements ImageLoader.ImageCache {
    LruCache<String, Bitmap> lruCache;
    DiskLruCache diskLruCache;
    final int RAM_CACHE_SIZE = 10 * 1024 * 1024;
    String DISK_CACHE_DIR = "image";
    final long DISK_MAX_SIZE = 50 * 1024 * 1024;
 
    public LruImageCache() {
        this.lruCache = new LruCache<String, Bitmap>(RAM_CACHE_SIZE) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };
 
        File cacheDir = new File(Environment.getExternalStorageDirectory(), DISK_CACHE_DIR);
        if(!cacheDir.exists())
        {
            cacheDir.mkdir();
        }
        try {
            diskLruCache = DiskLruCache.open(cacheDir, 1, 1, DISK_MAX_SIZE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 
    @Override
    public Bitmap getBitmap(String url) {
        String key=generateKey(url);
        Bitmap bmp = lruCache.get(key);
        if (bmp == null) {
            bmp = getBitmapFromDiskLruCache(key);
            //从磁盘读出后，放入内存
            if(bmp!=null)
            {
                lruCache.put(key,bmp);
            }
        }
        return bmp;
    }
 
    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        String key=generateKey(url);
        lruCache.put(url, bitmap);
        putBitmapToDiskLruCache(key,bitmap);
    }
 
    private void putBitmapToDiskLruCache(String key, Bitmap bitmap) {
    	System.out.println("put from disk-->key is:"+key);
        try {
            DiskLruCache.Editor editor = diskLruCache.edit(key);
            if(editor!=null)
            {
                OutputStream outputStream = editor.newOutputStream(0);
                bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
                editor.commit();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 
    private Bitmap getBitmapFromDiskLruCache(String key) {
    	System.out.println("get from disk-->key is:"+key);
        try {
            DiskLruCache.Snapshot snapshot=diskLruCache.get(key);
            if(snapshot!=null)
            {
                InputStream inputStream = snapshot.getInputStream(0);
                if (inputStream != null) {
                    Bitmap bmp = BitmapFactory.decodeStream(inputStream);
                    inputStream.close();
                    System.out.println("get from disk sucess");
                    return bmp;
                }
            }else{
            	System.out.println("cache have not this key -->key is:"+key);
            }
        } catch (IOException e) {
        	System.out.println("error:"+e);
            e.printStackTrace();
        }
        return null;
    }
 
    /**
     * 因为DiskLruCache对key有限制，只能是[a-z0-9_-]{1,64},所以用md5生成key
     * @param url
     * @return
     */
    private String generateKey(String url)
    {
        return MD5Utils.md5String32(url);
    }

}
