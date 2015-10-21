/**
 * 
 */
package com.android.qiadesi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.android.cache.LruImageCache;
import com.android.qiadesi.RecommendAdapter.Holder;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author Administrator
 *
 */
public class MenuAdapter extends BaseAdapter{

	/**
	 * 
	 */
	private Activity activity;
	private List<Bitmap> bit;
	private Handler handler;
	private List<String[]> myMap = new ArrayList<String[]>();
	RequestQueue mRequestQueue;
	LruImageCache mImageCache = new LruImageCache();
	LruImageCache imageCache;
	ImageLoader mImageLoader;
	public MenuAdapter(Activity activity,List<String[]> myMap,Handler handler) {
		this.activity = activity;
		this.myMap = myMap;
		this.handler = handler;
		System.out.println("----"+myMap);
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
		final Holder holder = new Holder();
		final float priceNumber;
		if(convertView == null){
			convertView = LayoutInflater.from(activity).inflate(R.layout.lv_menu, null);
			final EditText etNumber;
			holder.name = (TextView)convertView.findViewById(R.id.name);
			holder.price = (TextView)convertView.findViewById(R.id.price);
			holder.monthSalse = (TextView)convertView.findViewById(R.id.monthSalse);
			holder.ivMenu = (NetworkImageView)convertView.findViewById(R.id.ivMenu);
			holder.minus = (Button)convertView.findViewById(R.id.minus);
			holder.add = (Button)convertView.findViewById(R.id.add);
			holder.etNumber = (EditText)convertView.findViewById(R.id.etNumber);
		}
		holder.name.setText(myMap.get(position)[1]);
		holder.price.setText(myMap.get(position)[2]);
		priceNumber = Float.parseFloat(myMap.get(position)[2]);
		holder.monthSalse.setText(myMap.get(position)[3]);
		holder.ivMenu.setImageUrl(myMap.get(position)[0], mImageLoader);
		holder.ivMenu.setDefaultImageResId(R.drawable.ic_launcher);
		
		
		holder.add.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				holder.minus.setVisibility(View.VISIBLE);
				holder.etNumber.setVisibility(View.VISIBLE);
				holder.etNumber.setText(""+(Integer.parseInt(holder.etNumber.getText().toString())+1));
				Message msg = new Message();
				msg.what=3;
				Bundle bundle = new Bundle();
				bundle.putString("worse", ""+priceNumber);
				bundle.putString("type", "add");
				bundle.putString("name", holder.name.getText().toString());
				bundle.putString("number", holder.etNumber.getText().toString());
				msg.setData(bundle);
				handler.sendMessage(msg);
			}
		});
		holder.minus.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Message msg = new Message();
				msg.what=3;
				Bundle bundle = new Bundle();
				bundle.putString("worse", ""+priceNumber);
				bundle.putString("type", "minus");
				bundle.putString("name", holder.name.getText().toString());
				bundle.putString("number", holder.etNumber.getText().toString());
				msg.setData(bundle);
				handler.sendMessage(msg);
				
				holder.etNumber.setText(""+(Integer.parseInt(holder.etNumber.getText().toString())-1));
				if(Integer.parseInt(holder.etNumber.getText().toString()) == 0){
					holder.minus.setVisibility(View.GONE);
					holder.etNumber.setVisibility(View.GONE);
				}
			}
		});
		return convertView;
	}
	
	class Holder {
		NetworkImageView ivMenu;
		TextView name,price,monthSalse;
		Button minus,add;
		EditText etNumber;
	}

}
