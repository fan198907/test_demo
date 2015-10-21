package com.android.qiadesi;

import java.util.ArrayList;
import java.util.List;
import com.android.cache.LruImageCache;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @author Administrator
 *
 */
public class RecommendAdapter extends BaseAdapter{

	private Activity activity;
	private List<Object> myMap = new ArrayList<Object>();
	RequestQueue mRequestQueue;
	LruImageCache mImageCache = new LruImageCache();
	LruImageCache imageCache;
	ImageLoader mImageLoader;
	RecommendAdapter(Activity activity,List<Object> myMap){
		this.activity = activity;
		this.myMap = myMap;
		mRequestQueue = Volley.newRequestQueue(activity);
		imageCache = new LruImageCache(){

			@Override
			public Bitmap getBitmap(String key) {
				System.out.println("get image key");
				return mImageCache.getBitmap(key);
			}

			@Override
			public void putBitmap(String key, Bitmap value) {
				if(value!=null && !value.equals(""))
				{
					mImageCache.putBitmap(key, value);
				}
			}
			
		};
		mImageLoader = new ImageLoader(mRequestQueue, imageCache);
	}
	
	public void addMoreItem(String[] strs){
		myMap.add(strs);
	}
	
	@Override
	public int getCount() {
		return myMap.size();
	}

	@Override
	public Object getItem(int position) {
		return myMap.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder=null;
		if(convertView == null){
			holder = new Holder();
			convertView = LayoutInflater.from(activity).inflate(R.layout.lv_recommend, null);
			holder.iv_food_pic = (NetworkImageView )convertView.findViewById(R.id.iv_recommend_pic);
			holder.tv_recommend_name = (TextView)convertView.findViewById(R.id.tv_recommend_name);
			holder.tv_recommend_address = (TextView)convertView.findViewById(R.id.tv_recommend_address);
			convertView.setTag(holder);
		}else{
			holder=(Holder) convertView.getTag();
		}
		
		
		holder.iv_food_pic.setImageUrl(((String[])myMap.get(position))[2], mImageLoader);
		holder.iv_food_pic.setDefaultImageResId(R.drawable.ic_launcher);
		holder.tv_recommend_name.setText(((String[])myMap.get(position))[0]);
		holder.tv_recommend_address.setText(((String[])myMap.get(position))[1]);
		return convertView;
	}
	class Holder {
		NetworkImageView iv_food_pic;
		TextView tv_recommend_name,tv_recommend_address;
	}
}
