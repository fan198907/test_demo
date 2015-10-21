/**
 * 
 */
package com.android.cache;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.LruCache;

/**
 * @author Administrator
 *
 */
public class LruObjectCache {
	LruCache<String, String> lruCache;
    DiskLruCacheString diskLruCache;
    final int RAM_CACHE_SIZE = 10 * 1024 * 1024;
    String DISK_CACHE_DIR = "string";
    final long DISK_MAX_SIZE = 50 * 1024 * 1024;
	/**
	 * 
	 */
	public LruObjectCache() {
		this.lruCache = new LruCache<String, String>(RAM_CACHE_SIZE) {
            @Override
            protected int sizeOf(String key, String value) {
                return value.getBytes().length;
            }
        };
 
        File cacheDir = new File(Environment.getExternalStorageDirectory(), DISK_CACHE_DIR);
        if(!cacheDir.exists())
        {
            cacheDir.mkdir();
        }
        try {
            diskLruCache = DiskLruCacheString.open(cacheDir, 1, 1, DISK_MAX_SIZE);
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	
	public String getString(String url){
		String key=generateKey(url);
        String str = lruCache.get(key);
        if (str == null) {
            str = getStringFromDiskLruCache(key);
            //从磁盘读出后，放入内存
            if(str!=null)
            {
                lruCache.put(key,str);
            }else{
            	System.out.println("硬盘数据为空");
            }
        }
		return str;
	}
	
	/**
	 * @param key
	 * @return
	 */
	private String getStringFromDiskLruCache(String key) {
		try {
            DiskLruCacheString.Snapshot snapshot=diskLruCache.get(key);
            if(snapshot!=null)
            {
            	System.out.println("key of disk string is not null");
                InputStream inputStream = snapshot.getInputStream(0);
                if (inputStream != null) {
                    String str = convertStreamToString(inputStream);
                    inputStream.close();
                    System.out.println("get from disk sucess");
                    return str;
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


	public void putString(String url,String value){
		String key=generateKey(url);
        lruCache.put(url, value);
        putStringToDiskLruCache(key,value);
	}
	
	/**
	 * @param key
	 * @param value
	 */
	private void putStringToDiskLruCache(String key, String value) {
		try {
            DiskLruCacheString.Editor editor = diskLruCache.edit(key);
            if(editor!=null)
            {
                OutputStream outputStream = editor.newOutputStream(0);
                outputStream.write(value.getBytes());
                editor.commit();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
	}


	private String generateKey(String url)
    {
        return MD5Utils.md5String32(url);
    }
	
	public String convertStreamToString(InputStream is) {   

		BufferedReader reader = new BufferedReader(new InputStreamReader(is));   

        StringBuilder sb = new StringBuilder();   

        String line = null;   

        try {   

            while ((line = reader.readLine()) != null) {   

                sb.append(line + "/n");   

            }   

        } catch (IOException e) {   

            e.printStackTrace();   

        } finally {   

            try {   

                is.close();   

            } catch (IOException e) {   

                e.printStackTrace();   

            }   

        }   

    

        return sb.toString();   

    }
	
}
