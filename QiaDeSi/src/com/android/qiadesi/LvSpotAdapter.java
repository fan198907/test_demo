/**
 * 
 */
package com.android.qiadesi;

import java.util.List;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author Administrator
 *
 */
public class LvSpotAdapter extends BaseAdapter{

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	
	private Activity activity;
	private TextView tvFood;
	private ImageView ivShow;
	private List<Bitmap> bMap;
	private List<String> str;
	
	public LvSpotAdapter(Activity activity,List<Bitmap> bMap,List<String> str) {
		this.bMap = bMap;
		this.str = str;
		this.activity = activity;
	}
	
	@Override
	public int getCount() {
		return str.size();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		return str.get(position);
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = LayoutInflater.from(activity).inflate(R.layout.lv_spot, null);
		tvFood = (TextView)convertView.findViewById(R.id.tvShow);
		ivShow = (ImageView)convertView.findViewById(R.id.ivShow);
		BitmapDrawable myDrawable = new BitmapDrawable(bMap.get(position));
		ivShow.setBackgroundDrawable(myDrawable);
		tvFood.setText(str.get(position));
		return convertView;
	}

}
