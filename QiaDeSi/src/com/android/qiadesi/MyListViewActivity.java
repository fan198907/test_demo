/**
 * 
 */
package com.android.qiadesi;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author Administrator
 *
 */
public class MyListViewActivity extends Activity{
	private ListView myListView;
	private String type;
	public List<String> str_spot_name = new ArrayList<String>() ;
	public List<String> str_food_name = new ArrayList<String>();
	public List<String> str_food_star = new ArrayList<String>();
	public List<String> str_food_sales = new ArrayList<String>();
	public List<String> str_food_send_up = new ArrayList<String>();
	public List<String> str_food_costs = new ArrayList<String>();
	public List<String> str_food_time = new ArrayList<String>();
	public List<String> str_food_is_business = new ArrayList<String>();
	public List<String> list_spot = new ArrayList<String>();
	public List<String> list_food = new ArrayList<String>();
	public List<Bitmap> bmps_spot,bmps_food;
	public String baseUrl = "http://192.168.1.101:8080/MyWeb/Image/pic/";
	public String baseUrl_restaurant = "http://192.168.1.101:8080/MyWeb/Image/restaurant/";
	public Handler handler;
	private LvSpotAdapter lsAdapter;
	private FoodAdapter fAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_listview);
		myListView = (ListView)findViewById(R.id.myListView);
		
		type = this.getIntent().getExtras().getString("type");
		
		handler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				switch(msg.what){
					case 1:
						if(type.equals("food")){
							Map<String, Object> myMap = new HashMap<String, Object>();
							myMap.put("bmps_food", bmps_food);
							myMap.put("str_food_name", str_food_name);
							myMap.put("str_food_star", str_food_star);
							myMap.put("str_food_sales", str_food_sales);
							myMap.put("str_food_send_up", str_food_send_up);
							myMap.put("str_food_costs", str_food_costs);
							myMap.put("str_food_time", str_food_time);
							myMap.put("str_food_is_business", str_food_is_business);
							fAdapter = new FoodAdapter(MyListViewActivity.this, myMap);
							myListView.setAdapter(fAdapter);
							myListView.invalidate();
						}else if(type.equals("spot")){
							lsAdapter = new LvSpotAdapter(MyListViewActivity.this,bmps_spot, str_spot_name);
							myListView.setAdapter(lsAdapter);
							myListView.invalidate();
						}
						break;
					default:
						break;
				}
			}
			
		};
		
		new Thread(){

			@Override
			public void run() {
				if(type.equals("food")){
					try {
						List<List<String>> myList = HttpUtil.postRequest(HttpUtil.ADDERS,"selectFood","");
						str_food_name = myList.get(0);
						str_food_star = myList.get(1);
						str_food_sales = myList.get(2);
						str_food_send_up = myList.get(3);
						str_food_costs = myList.get(4);
						str_food_time = myList.get(5);
						str_food_is_business = myList.get(7);
						for(int i=0;i<myList.get(6).size();i++){
							list_food.add(baseUrl_restaurant+myList.get(6).get(i));
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					bmps_food = getBitmapFromServer(list_food);
					Message msg1 = new Message();
					msg1.what = 1;
					handler.sendMessage(msg1);
				}else if(type.equals("spot")){
					try {
						List<List<String>> myList = HttpUtil.postRequest(HttpUtil.ADDERS,"selectSpot","");
						str_spot_name = myList.get(0);
						for(int i=0;i<myList.get(1).size();i++){
							list_spot.add(baseUrl+myList.get(1).get(i));
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					bmps_spot = getBitmapFromServer(list_spot);
					Message msg2 = new Message();
					msg2.what = 1;
					handler.sendMessage(msg2);
				}
				
				
				
			}
        	
        }.start();
        
        myListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				TextView tvName;
				if(type.equals("food")){
					tvName = (TextView) view.findViewById(R.id.tv_food_name);
					Intent intent = new Intent();
					Bundle bundle = new Bundle();
					bundle.putString("name", tvName.getText().toString());
					intent.putExtras(bundle);
					intent.setClass(MyListViewActivity.this, RestaurantActivity.class);
					startActivity(intent);
				}
				
			}
		});
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
			}catch(Exception e){
				System.out.println(e);
			}
		}
		return pic;
	}
	
}
