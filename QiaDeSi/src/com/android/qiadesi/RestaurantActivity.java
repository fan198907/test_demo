/**
 * 
 */
package com.android.qiadesi;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.cache.LruImageCache;
import com.android.cache.LruObjectCache;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author Administrator
 *
 */
public class RestaurantActivity extends Activity{
	public String adders = "http://192.168.1.101:8080/orderdishes/orderdishes_servlet";
	public String baseUrl_restaurant = "http://192.168.1.101:8080/MyWeb/Image/restaurant/";
	public String baseUrl_menu = "http://192.168.1.101:8080/MyWeb/Image/menu/";
	private String restaurantName = "";
	private String send_up = "";
	private float worsePrice = 0;
	private float total = 0;
	//餐厅主图片
	private com.android.volley.toolbox.NetworkImageView restaurant_page_iv_home_img;
	//餐厅名
	private TextView restaurant_page_tv_home_name;
	//菜系
	private TextView restaurant_page_tv_cuisine;
	//评分
	private ImageView restaurant_page_tv_star;
	//营业时间
	private TextView restaurant_page_tv_time;
	//是否营业
//	private TextView restaurant_page_tv_is_business;
	//餐厅地址
	private TextView restaurant_page_tv_txtAddress;
	//平均送达时间
	private TextView restaurant_page_tv_preTime;
	//菜单列表
	private ListView restaurant_page_iv_menu;
	//购物车
	private ImageView ivTrolley;
	//差多少满起送
	private TextView tvLastPrice;
	//总价格
	private TextView tvTotal;
	private List<List<String>> myList = new ArrayList<List<String>>();
	private List<List<String>> myMenuList = new ArrayList<List<String>>();
	private List<String> name_list = new ArrayList<String>();
	private List<String> price_list = new ArrayList<String>();
	private List<String> monthsales_list = new ArrayList<String>();
	private Handler handler;
	public List<Bitmap> homeImgMap,menuImgMap;
	private MenuAdapter lsAdapter;
	private ArrayList<List<String>> menuList = new ArrayList<List<String>>();
	private LruObjectCache lruObjectCache;
	private RequestQueue mRequestQueue;
	private String selectString="";
	private String selectMenuString="";
	ImageLoader mImageLoader;
	LruImageCache imageCache;
	LruImageCache mImageCache = new LruImageCache();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.restaurant_page);
		
		mRequestQueue = Volley.newRequestQueue(this);
		lruObjectCache = new LruObjectCache();
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
		if(this.getIntent() != null){
			restaurantName = this.getIntent().getExtras().getString("name");
		}
		//餐厅图标
		restaurant_page_iv_home_img = (com.android.volley.toolbox.NetworkImageView)findViewById(R.id.restaurant_page_iv_home_img);
		//餐厅名称
		restaurant_page_tv_home_name = (TextView)findViewById(R.id.restaurant_page_tv_home_name);
		//菜系
		restaurant_page_tv_cuisine = (TextView)findViewById(R.id.restaurant_page_tv_cuisine);
		//评级
		restaurant_page_tv_star = (ImageView)findViewById(R.id.restaurant_page_tv_star);
		//餐厅营业时间
		restaurant_page_tv_time = (TextView)findViewById(R.id.restaurant_page_tv_time);
		//地址
		restaurant_page_tv_txtAddress = (TextView)findViewById(R.id.restaurant_page_tv_txtPreTime);
		//预计送达时间
		restaurant_page_tv_preTime = (TextView)findViewById(R.id.restaurant_page_tv_preTime);
		//菜单列表
		restaurant_page_iv_menu = (ListView)findViewById(R.id.restaurant_page_iv_menu);
		ivTrolley = (ImageView)findViewById(R.id.ivTrolley);
		tvLastPrice = (TextView)findViewById(R.id.tvLastPrice);
		tvTotal = (TextView)findViewById(R.id.tvTotal);
		tvTotal.setText("购物车是空的");
//		restaurant_page_iv_menu.setItemsCanFocus(true);
		
		handler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				switch(msg.what){
					case 1:
						try {
							JSONArray array = new JSONArray(selectString);
							JSONObject object = array.getJSONObject(0);
							send_up = object.getString("send_up");
							if(!send_up.equals("")){
								worsePrice = Float.parseFloat(send_up);
								tvLastPrice.setText("还差"+worsePrice+"元");
							}
							restaurant_page_tv_home_name.setText(object.getString("name"));
							restaurant_page_tv_cuisine.setText(object.getString("cuisine"));
							if(Float.parseFloat(object.getString("star")) == 4){
								restaurant_page_tv_star.setBackgroundResource(R.drawable.iv4);
							}else if(Float.parseFloat(object.getString("star")) == 4.5){
								restaurant_page_tv_star.setBackgroundResource(R.drawable.iv4_5);
							}else if(Float.parseFloat(object.getString("star")) == 0){
								restaurant_page_tv_star.setBackgroundResource(R.drawable.iv0);
							}
							
							restaurant_page_tv_time.setText(object.getString("business_time"));
							restaurant_page_tv_txtAddress.setText(object.getString("address"));
							restaurant_page_tv_preTime.setText(object.getString("time"));
							
							String pic_url = baseUrl_restaurant+object.getString("pic_address");
							restaurant_page_iv_home_img.setImageUrl(pic_url, mImageLoader);
							restaurant_page_iv_home_img.setDefaultImageResId(R.drawable.ic_launcher);
						} catch (JSONException e) {
							e.printStackTrace();
						}
						
						break;
					case 2:
						
						List<String[]> myMaps = new ArrayList<String[]>();
						try {
							JSONArray array = new JSONArray(selectMenuString);
							for(int i=0;i<array.length();i++){
								JSONObject object = array.getJSONObject(i);
								String[] strs = new String[4];
								strs[0]=baseUrl_menu+object.getString("IMAGE");
								strs[1]=object.getString("NAME");
								strs[2]=object.getString("PRICE");
								strs[3]=object.getString("MONTHSALES");
								myMaps.add(strs);
							}
							lsAdapter = new MenuAdapter(RestaurantActivity.this, myMaps,handler);
							restaurant_page_iv_menu.setAdapter(lsAdapter);
							restaurant_page_iv_menu.invalidate();
						} catch (JSONException e) {
							e.printStackTrace();
						}
						
						break;
					case 3:
						float priceNumber = Float.parseFloat(msg.getData().getString("worse"));
						String type = msg.getData().getString("type");
						String strName = msg.getData().getString("name");
						String strNumber = msg.getData().getString("number");
						if(type.equals("add")){
							worsePrice = (float)(worsePrice-priceNumber);
							total = total+priceNumber;
						}else{
							worsePrice = (float)(worsePrice+priceNumber);
							total = total-priceNumber;
						}
						if(worsePrice<Float.parseFloat(send_up)){
							ivTrolley.setBackgroundResource(R.drawable.you);
						}else{
							ivTrolley.setBackgroundResource(R.drawable.wrong);
						}
						if(worsePrice<=0.0){
							tvLastPrice.setText("选好了");
						}else{
							tvLastPrice.setText("还差"+worsePrice+"元");
						}
						if(total<=0){
							tvTotal.setText("购物车是空的");
						}else{
							tvTotal.setText(total+"元");
						}
						if(menuList.size()>0){
							for(int i=0;i<menuList.size();i++){
								if(menuList.get(i).get(0).equals(strName)){
									menuList.remove(i);
									break;
								}
							}
							List<String> list = new ArrayList<String>();
							list.add(strName);
							list.add(strNumber);
							list.add(priceNumber+"");
							menuList.add(list);
						}else{
							List<String> list = new ArrayList<String>();
							list.add(strName);
							list.add(strNumber);
							list.add(priceNumber+"");
							menuList.add(list);
						}
						break;
					default:
						break;
				}
			}
			
		};
		tvLastPrice.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(worsePrice<=0.0){
					Intent intent = new Intent();
					intent.setClass(RestaurantActivity.this, SubmitMenuActivity.class);
					Bundle bundle = new Bundle();
					//付款
					bundle.putString("resName", restaurant_page_tv_home_name.getText().toString());
					intent.putExtra("list", (Serializable)menuList);
					intent.putExtras(bundle);
					startActivity(intent);
				}
			}
		});
		
		String tempselectFoodEachRestaurantStr = lruObjectCache.getString(HttpUtil.ADDERS+"selectFoodEachRestaurant"+restaurantName);
		System.out.println(tempselectFoodEachRestaurantStr+"++++++++++");
		selectString = tempselectFoodEachRestaurantStr;
        if(tempselectFoodEachRestaurantStr == null || tempselectFoodEachRestaurantStr.equals("")){
        	System.out.println("缓存数据为空");
			try {
				StringRequest strReques = new StringRequest(Request.Method.POST, HttpUtil.ADDERS , new Listener<String>() {
	
					@Override
					public void onResponse(String str) {
						try {
							selectString = str;
							System.out.println("selectString"+"[[[[[[[[[[[[[[[[[[[[[[["+selectString);
							if(str!=null && !str.equals("")){
								lruObjectCache.putString(HttpUtil.ADDERS+"selectFoodEachRestaurant"+restaurantName,str);
							}
					        new Thread(){
							
								@Override
								public void run() {
									Message msg = new Message();
									msg.what = 1;
									handler.sendMessage(msg);
								}
					        }.start();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				},new Response.ErrorListener() {
	
					@Override
					public void onErrorResponse(VolleyError arg0) {
						System.out.println("error[[[[[[[[[[");
					}
				}){
					@Override
					protected Map<String, String> getParams()
							throws AuthFailureError {
							Map<String, String> map = new HashMap<String, String>();
							map.put("type", "selectFoodEachRestaurant");
							map.put("restaurant_name", restaurantName);
							return map;
					}
				};
				strReques.setShouldCache(true);
				mRequestQueue.add(strReques);
				mRequestQueue.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
        }else{
        	new Thread(){
				
				@Override
				public void run() {
					Message msg = new Message();
					msg.what = 1;
					handler.sendMessage(msg);
				}
	        }.start();
        }
        
        
        String tempmenuOfRestaurantStr = lruObjectCache.getString(HttpUtil.ADDERS+"menuOfRestaurant"+restaurantName);
        System.out.println(tempmenuOfRestaurantStr+"++++++++++");
        selectMenuString = tempmenuOfRestaurantStr;
        if(tempmenuOfRestaurantStr == null || tempmenuOfRestaurantStr.equals("")){
        	System.out.println("缓存数据为空");
			try {
				StringRequest strReques = new StringRequest(Request.Method.POST, HttpUtil.ADDERS , new Listener<String>() {
	
					@Override
					public void onResponse(String str) {
						try {
							selectMenuString = str;
							System.out.println("selectMenuString"+"[[[[[[[[[[[[[[[[[[[[[[["+selectMenuString);
							if(str!=null && !str.equals("")){
								lruObjectCache.putString(HttpUtil.ADDERS+"menuOfRestaurant"+restaurantName,str);
							}
					        new Thread(){
							
								@Override
								public void run() {
									Message msg = new Message();
									msg.what = 2;
									handler.sendMessage(msg);
								}
					        }.start();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				},new Response.ErrorListener() {
	
					@Override
					public void onErrorResponse(VolleyError arg0) {
						System.out.println("error[[[[[[[[[[[[");
					}
				}){
					@Override
					protected Map<String, String> getParams()
							throws AuthFailureError {
							Map<String, String> map = new HashMap<String, String>();
							map.put("type", "menuOfRestaurant");
							map.put("restaurant_name", restaurantName);
							return map;
					}
				};
				strReques.setShouldCache(true);
				mRequestQueue.add(strReques);
				mRequestQueue.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
        }else{
        	new Thread(){
				
				@Override
				public void run() {
					Message msg = new Message();
					msg.what = 2;
					handler.sendMessage(msg);
				}
	        }.start();
        }
        
//		new Thread(){
//
//			@Override
//			public void run() {
//				
//				try {
//					myList = HttpUtil.postRequest(adders,"selectFoodEachRestaurant",restaurantName);
//					List<String> list_spot = new ArrayList<String>();
//					list_spot.add(baseUrl_restaurant+myList.get(0).get(0));
//					homeImgMap = getBitmapFromServer(list_spot);
//					Message msg = new Message();
//					msg.what = 1;
//					handler.sendMessage(msg);
//					
//					myMenuList = HttpUtil.postRequest(adders, "menuOfRestaurant", restaurantName);
//					List<String> list_menu = new ArrayList<String>();
//					for(int i=0;i<myMenuList.get(0).size();i++){
//						list_menu.add(baseUrl_menu+myMenuList.get(0).get(i));
//					}
//					menuImgMap = getBitmapFromServer(list_menu);
//					name_list = myMenuList.get(1);
//					price_list = myMenuList.get(2);
//					monthsales_list = myMenuList.get(3);
//					Message msg2 = new Message();
//					msg2.what = 2;
//					handler.sendMessage(msg2);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//			
//		}.start();
	}

	public static List<Bitmap> getBitmapFromServer(List<String> imagePath){
		URL myFileURL;
		List<Bitmap> pic = new ArrayList<Bitmap>();
		for(int i = 0;i < imagePath.size();i++){
			try{
				myFileURL = new URL(imagePath.get(i));
				HttpURLConnection conn = (HttpURLConnection)myFileURL.openConnection();
				conn.setConnectTimeout(6000);
				conn.setDoInput(true);
				conn.setUseCaches(false);
				conn.connect();
				InputStream is = conn.getInputStream();
				pic.add(BitmapFactory.decodeStream(is));
				is.close();
				conn.disconnect();
			}catch(Exception e){
				System.out.println(e);
			}
		}
		return pic;
	}
	
	
}
