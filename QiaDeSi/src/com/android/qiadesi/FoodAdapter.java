/**
 * 
 */
package com.android.qiadesi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.smssdk.gui.DefaultContactViewItem.ViewHolder;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
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

public class FoodAdapter extends BaseAdapter{
	private Activity activity;
	private List<Bitmap> bMap;
	private Map<String, Object> myMap = new HashMap<String, Object>();
	private TextView tv_food_name,tv_food_sales,tv_food_send_up,tv_food_costs,tv_food_time,food_is_business;
	private ImageView iv_food_pic,iv_food_star;
	private List<String> str_food_name,str_food_star,str_food_sales,str_food_send_up,str_food_costs,str_food_time,str_food_is_business;
	FoodAdapter(Activity activity,Map<String, Object> myMap){
		this.activity = activity;
		this.myMap = myMap;
		this.bMap = (List<Bitmap>) this.myMap.get("bmps_food");
		this.str_food_name = (List<String>) this.myMap.get("str_food_name");
		this.str_food_star = (List<String>) this.myMap.get("str_food_star");
		this.str_food_sales = (List<String>) this.myMap.get("str_food_sales");
		this.str_food_send_up = (List<String>) this.myMap.get("str_food_send_up");
		this.str_food_costs = (List<String>) this.myMap.get("str_food_costs");
		this.str_food_time = (List<String>) this.myMap.get("str_food_time");
		this.str_food_is_business = (List<String>) this.myMap.get("str_food_is_business");
	}

	@Override
	public int getCount() {
		return bMap.size();
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
		if(convertView == null){
			convertView = LayoutInflater.from(activity).inflate(R.layout.lv_food, null);
		}
		tv_food_name = (TextView)convertView.findViewById(R.id.tv_food_name);
		tv_food_sales = (TextView)convertView.findViewById(R.id.tv_food_sales);
		tv_food_send_up = (TextView)convertView.findViewById(R.id.tv_food_send_up);
		tv_food_costs = (TextView)convertView.findViewById(R.id.tv_food_costs);
		tv_food_time = (TextView)convertView.findViewById(R.id.tv_food_time);
		food_is_business = (TextView)convertView.findViewById(R.id.food_is_business);
		
		iv_food_pic = (ImageView)convertView.findViewById(R.id.iv_food_pic);
		iv_food_star = (ImageView)convertView.findViewById(R.id.iv_food_star);
		BitmapDrawable myDrawable = new BitmapDrawable(bMap.get(position));
		iv_food_pic.setBackgroundDrawable(myDrawable);
		tv_food_name.setText(str_food_name.get(position));
		if(Float.parseFloat(str_food_star.get(position)) == 4.5){
			iv_food_star.setBackgroundResource(R.drawable.iv4_5);
		}else if(Float.parseFloat(str_food_star.get(position)) == 4){
			iv_food_star.setBackgroundResource(R.drawable.iv4);
		}
		tv_food_sales.setText(str_food_sales.get(position));
		tv_food_send_up.setText(str_food_send_up.get(position));
		tv_food_costs.setText(str_food_costs.get(position));
		if(Integer.parseInt(str_food_time.get(position))<=60){
			tv_food_time.setText(str_food_time.get(position)+"分钟");
		}else{
			tv_food_time.setText(Integer.parseInt(str_food_time.get(position))/60+"小时"+Integer.parseInt(str_food_time.get(position))%60+"分钟");
		}
		food_is_business.setText(str_food_is_business.get(position));
		if(str_food_is_business.get(position).equals("休息中")){
			food_is_business.setTextColor(Color.GRAY);
		}else{
			food_is_business.setTextColor(Color.GREEN);
		}
		return convertView;
	}

}
