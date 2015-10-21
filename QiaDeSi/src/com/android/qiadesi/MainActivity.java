package com.android.qiadesi;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.android.cache.LruObjectCache;
import com.android.qiadesi.MyCustListView.OnLoadListener;
import com.android.qiadesi.MyCustListView.OnRefreshListener;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity{
	
	private LvSpotAdapter lsAdapter;
	private RecommendAdapter fAdapter;
	public String baseUrl = "http://192.168.1.101:8080/MyWeb/Image/pic/";
	public String baseUrl_restaurant = "http://192.168.1.101:8080/MyWeb/Image/restaurant/";
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
	
	
	public List<String> list_name = new ArrayList<String>();
	public List<String> list_address = new ArrayList<String>();
	public List<String> list_image = new ArrayList<String>();
	
	public List<Bitmap> bmps_spot,bmps_food;
	public List<Bitmap> bmps_image;
	public Handler handler;
	private MyCustListView lvRecommend;
	private Button btnPeopleLogin;
	private FragmentTransaction transaction;
	private FirstFragment firstFragment;
	private SecondFragment secondFragment;
	private FrameLayout fragment_container;
	private int startX,endX;
	private TextView city;
	private MyApplication myApp;
	private LocationManager manager;
	private String cityName="南昌";
	private Location location;
	private GestureDetector mGestureDetector;
	//当前数据位置
	RequestQueue mRequestQueue;
	private int currentCount = 20;
//	private String userName = "";
	LruObjectCache lruObjectCache;
	private int currentTotalCount;
	private Button btnShareWeiXin;
	private IWXAPI wxApi;
	private String apiAPPID = "wx2116244240bf98d3";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        myApp = (MyApplication) this.getApplication();
//        mGestureDetector = new GestureDetector(this);
        mGestureDetector = new GestureDetector(MainActivity.this,onGestureListener);
        mRequestQueue = Volley.newRequestQueue(this);
        
        lruObjectCache = new LruObjectCache();
        
        final List<Object> listAll = new ArrayList<Object>();
        fAdapter = new RecommendAdapter(MainActivity.this, listAll);
        handler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				switch(msg.what){
					case 1:
						lsAdapter = new LvSpotAdapter(MainActivity.this,bmps_spot, str_spot_name);
						break;
					case 2:
						listAll.clear();
						if(list_image.size() < currentCount){
							currentCount = list_image.size();
						}
						for(int i=0;i < currentCount;i++){
							String[] strs = new String[3];
							strs[0] = list_name.get(i);
							strs[1] = list_address.get(i);
							strs[2] = list_image.get(i);
							listAll.add(strs);
						}
						lvRecommend.setTotalSize(list_image.size());
						currentTotalCount = listAll.size();
						lvRecommend.setAdapter(fAdapter);
						lvRecommend.invalidate();
						break;
					case 3:
						if(startX>endX+20){
							//左滑
							if(null == secondFragment){
								secondFragment = new SecondFragment();
							}
							transaction = getFragmentManager().beginTransaction();
							transaction.replace(R.id.fragment_container, secondFragment);
							transaction.commit();
						}
						if(startX<endX-20){
							//右滑
							if(null == firstFragment){
								firstFragment = new FirstFragment();
							}
							transaction = getFragmentManager().beginTransaction();
							transaction.replace(R.id.fragment_container, firstFragment);
							transaction.commit();
						}
						break;
					case 4:
						city.setText(cityName);
						break;
					case 5:
						Bundle bundle = msg.getData();
						String type = bundle.getString("type");
						String name = bundle.getString("name");
						if(type.equals("restaurant")){							
							Intent intent = new Intent();
							bundle.putString("name", name);
							intent.putExtras(bundle);
							intent.setClass(MainActivity.this, RestaurantActivity.class);
							startActivity(intent);
						}else if(type.equals("spot")){
							
						}
						break;
					default:
						break;
				}
			}
        	
        };
        
        transaction = getFragmentManager().beginTransaction();
        if(null == firstFragment){
        	firstFragment = new FirstFragment();
        }
        transaction.add(R.id.fragment_container, firstFragment);
        transaction.commit();
        fragment_container = (FrameLayout)findViewById(R.id.fragment_container);
        fragment_container.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					startX = (int) event.getX();
				}
				if(event.getAction() == MotionEvent.ACTION_UP){
					endX = (int) event.getX();
					Message msg = new Message();
					msg.what = 3;
					handler.sendMessage(msg);
				}
				return true;
			}
		});
        
        lvRecommend = (MyCustListView)findViewById(R.id.lvRecommend);
        btnShareWeiXin = (Button)findViewById(R.id.btnShareWeiXin);
        
        wxApi = WXAPIFactory.createWXAPI(this, apiAPPID);
        btnShareWeiXin.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final AlertDialog dlg = new AlertDialog.Builder(MainActivity.this).create();
				dlg.show();
				
				Window window = dlg.getWindow();
				window.setGravity(Gravity.BOTTOM);
				window.setContentView(R.layout.share_weixin);
				
				Button btnShareFriend = (Button)window.findViewById(R.id.btnShareFriend);
				Button btnShareCircleOfFriends = (Button)window.findViewById(R.id.btnShareCircleOfFriends);
				btnShareFriend.setOnClickListener(new ButtonOnClickListener());
				btnShareCircleOfFriends.setOnClickListener(new ButtonOnClickListener());
			}
		});
        
        city = (TextView)findViewById(R.id.city);
        
        String tempRecommendStr = lruObjectCache.getString(HttpUtil.ADDERS+"selectRecommend");
        if(tempRecommendStr == null || tempRecommendStr.equals("")){
        	System.out.println("缓存数据为空");
			try {
				StringRequest strReques = new StringRequest(Request.Method.POST, HttpUtil.ADDERS , new Listener<String>() {
	
					@Override
					public void onResponse(String str) {
						list_name.clear();
						list_address.clear();
						list_image.clear();
						try {
							if(str!=null && !str.equals("")){
								lruObjectCache.putString(HttpUtil.ADDERS+"selectRecommend",str);
							}
							JSONArray array = new JSONArray(str);
							
							System.out.println("selectRecommend");
							List<String> list_image_temp = new ArrayList<String>();
							for(int i=0;i<array.length();i++){
								JSONObject object = array.getJSONObject(i);
								
								list_name.add(object.getString("name"));
								list_address.add(object.getString("address"));
								list_image_temp.add(object.getString("image"));
							}
							for(int j=0;j<list_image_temp.size();j++){
								list_image.add(baseUrl_restaurant+list_image_temp.get(j));
							}
							
					        new Thread(){
							
								@Override
								public void run() {
									Message msg2 = new Message();
									msg2.what = 2;
									handler.sendMessage(msg2);
								}
					        }.start();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				},new Response.ErrorListener() {
	
					@Override
					public void onErrorResponse(VolleyError arg0) {
						System.out.println("error");
					}
				}){
	
					@Override
					protected Map<String, String> getParams()
							throws AuthFailureError {
							Map<String, String> map = new HashMap<String, String>();
							map.put("type", "selectRecommend");
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
        	try {
				JSONArray array = new JSONArray(tempRecommendStr);
				System.out.println("selectRecommend");
				
				List<String> list_image_temp = new ArrayList<String>();
				for(int i=0;i<array.length();i++){
					JSONObject object = array.getJSONObject(i);
					
					list_name.add(object.getString("name"));
					list_address.add(object.getString("address"));
					list_image_temp.add(object.getString("image"));
					System.out.println(object.getString("image"));
				}
				for(int j=0;j<list_image_temp.size();j++){
					list_image.add(baseUrl_restaurant+list_image_temp.get(j));
				}
		        new Thread(){
				
					@Override
					public void run() {
//						bmps_image = getBitmapFromServer(list_image);
						Message msg2 = new Message();
						msg2.what = 2;
						handler.sendMessage(msg2);
					}
		        }.start();
			} catch (JSONException e) {
				e.printStackTrace();
			}
        }
        
        
        lvRecommend.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				TextView etName = (TextView)view.findViewById(R.id.tv_recommend_name);
				final String name = etName.getText().toString();
				String type_restaurantStr = lruObjectCache.getString(HttpUtil.ADDERS+"type_restaurant"+name);
				System.out.println(type_restaurantStr+"+++++++++");
				if(type_restaurantStr == null || type_restaurantStr.equals("false") || type_restaurantStr.equals("")){
					try {
						StringRequest strReques = new StringRequest(Request.Method.POST, HttpUtil.ADDERS , new Listener<String>() {

							@Override
							public void onResponse(String str) {
								System.out.println(str);
								if(str.equals("true")){
									if(str!=null && !str.equals("")){
										lruObjectCache.putString(HttpUtil.ADDERS+"type_restaurant"+name,str);
									}
									Message msg = new Message();
									msg.what = 5;
									Bundle bundle = new Bundle();
									bundle.putString("type", "restaurant");
									bundle.putString("name", name);
									msg.setData(bundle);
									handler.sendMessage(msg);
								} 
							}
						},new Response.ErrorListener() {
	
							@Override
							public void onErrorResponse(VolleyError arg0) {
								System.out.println("error");
							}
						}){
	
							@Override
							protected Map<String, String> getParams()
									throws AuthFailureError {
									Map<String, String> map = new HashMap<String, String>();
									map.put("type", "type_restaurant");
									map.put("name", name);
									return map;
							}

						};
						strReques.setShouldCache(true);
						mRequestQueue.add(strReques);
						mRequestQueue.start();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}else if(type_restaurantStr.contains("true")){
					Message msg = new Message();
					msg.what = 5;
					Bundle bundle = new Bundle();
					bundle.putString("type", "restaurant");
					bundle.putString("name", name);
					msg.setData(bundle);
					handler.sendMessage(msg);
				}
				
				String type_spotStr = lruObjectCache.getString(HttpUtil.ADDERS+"type_spot"+name);
				if(type_spotStr == null || type_spotStr.equals("false") || type_spotStr.equals("")){
					try {
						StringRequest strReques = new StringRequest(Request.Method.POST, HttpUtil.ADDERS , new Listener<String>() {
	
							@Override
							public void onResponse(String str) {
								System.out.println(str);
								if(str.equals("true")){
									if(str!=null && !str.equals("")){
										lruObjectCache.putString(HttpUtil.ADDERS+"type_spot"+name,str);
									}
									Message msg = new Message();
									msg.what = 5;
									Bundle bundle = new Bundle();
									bundle.putString("type", "spot");
									bundle.putString("name", name);
									msg.setData(bundle);
									handler.sendMessage(msg);
								}
							}
						},new Response.ErrorListener() {
	
							@Override
							public void onErrorResponse(VolleyError arg0) {
								System.out.println("error");
							}
						}){
	
							@Override
							protected Map<String, String> getParams()
									throws AuthFailureError {
									Map<String, String> map = new HashMap<String, String>();
									map.put("type", "type_spot");
									map.put("name", name);
									return map;
							}
							
						};
						strReques.setShouldCache(true);
						mRequestQueue.add(strReques);
						mRequestQueue.start();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}else if(type_spotStr.contains("true")){
					Message msg = new Message();
					msg.what = 5;
					Bundle bundle = new Bundle();
					bundle.putString("type", "spot");
					bundle.putString("name", name);
					msg.setData(bundle);
					handler.sendMessage(msg);
				}
				
			}
		});
        
        lvRecommend.setOnScrollListener(new AbsListView.OnScrollListener() {
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				
				lvRecommend.currentTotalCount = totalItemCount;
				lvRecommend.firstVisibleItem = firstVisibleItem;
				lvRecommend.visibleItemCount = visibleItemCount;
			}

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				lvRecommend.scrollState = scrollState;
			}
		});
        
        lvRecommend.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				try {
					StringRequest strReques = new StringRequest(Request.Method.POST, HttpUtil.ADDERS , new Listener<String>() {
		
						@Override
						public void onResponse(String str) {
							list_name.clear();
							list_address.clear();
							list_image.clear();
							try {
								if(str!=null && !str.equals("")){
									lruObjectCache.putString(HttpUtil.ADDERS+"selectRecommend",str);
								}
								JSONArray array = new JSONArray(str);
								
								System.out.println("selectRecommend");
								List<String> list_image_temp = new ArrayList<String>();
								for(int i=0;i<array.length();i++){
									JSONObject object = array.getJSONObject(i);
									
									list_name.add(object.getString("name"));
									list_address.add(object.getString("address"));
									list_image_temp.add(object.getString("image"));
								}
								for(int j=0;j<list_image_temp.size();j++){
									System.out.println(baseUrl_restaurant+list_image_temp.get(j));
									list_image.add(baseUrl_restaurant+list_image_temp.get(j));
								}
								lvRecommend.refreshing.setVisibility(View.GONE);
								lvRecommend.state = lvRecommend.NONE;
						        new Thread(){
								
									@Override
									public void run() {
										Message msg2 = new Message();
										msg2.what = 2;
										handler.sendMessage(msg2);
									}
						        }.start();
							} catch (JSONException e) {
								lvRecommend.refreshing.setVisibility(View.GONE);
								lvRecommend.state = lvRecommend.NONE;
								Toast toast = Toast.makeText(MainActivity.this, "刷新失败，请检查网络是否异常", Toast.LENGTH_SHORT);
						        toast.show();
								e.printStackTrace();
							}
						}
					},new Response.ErrorListener() {
		
						@Override
						public void onErrorResponse(VolleyError arg0) {
							System.out.println("error");
							lvRecommend.refreshing.setVisibility(View.GONE);
							lvRecommend.state = lvRecommend.NONE;
							Toast toast = Toast.makeText(MainActivity.this, "刷新失败，请检查网络是否异常", Toast.LENGTH_SHORT);
					        toast.show();
						}
					}){
		
						@Override
						protected Map<String, String> getParams()
								throws AuthFailureError {
								Map<String, String> map = new HashMap<String, String>();
								map.put("type", "selectRecommend");
								return map;
						}
						
					};
					strReques.setShouldCache(true);
					mRequestQueue.add(strReques);
					mRequestQueue.start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
        lvRecommend.setOnLoadListener(new OnLoadListener() {
			
			@Override
			public void onLoad() {
				currentTotalCount = listAll.size();
				int tempCount = currentTotalCount+currentCount;
				if(tempCount > list_image.size()){
					tempCount = list_image.size();
				}
				for(int i=currentTotalCount;i<tempCount;i++){
					String[] strs = new String[3];
					strs[0] = list_name.get(i);
					strs[1] = list_address.get(i);
					strs[2] = list_image.get(i);
					fAdapter.addMoreItem(strs);
					lvRecommend.invalidate();
				}
				lvRecommend.loading.setVisibility(View.GONE);
				lvRecommend.footState = lvRecommend.NONE;
			}
		});
      //获取系统的服务， 

        manager = (LocationManager)getSystemService(Context.LOCATION_SERVICE); 
//        location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER); 
        location = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        new Thread(){

			@Override
			public void run() {
				//第一次获得设备的位置 
		        
		        updateWithNewLocation(location);

		        //重要函数，监听数据测试 

			}
        	
        }.start();

        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 6000, 10,    
        		
        		locationListener);  
        
        btnPeopleLogin = (Button)findViewById(R.id.btnPeopleLogin);
        btnPeopleLogin.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, UserInformationActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.in_from_left, R.anim.out_of_right);
				finish();
			}
		});
    }
    
    class ButtonOnClickListener implements View.OnClickListener{
		@Override
		public void onClick(View v) {
			switch(v.getId()){
				case R.id.btnShareFriend:
					wechatShare(0);
					break;
				case R.id.btnShareCircleOfFriends:
					wechatShare(1);
					break;
				default:
					break;
			}
		}
		
	}
    
    private void wechatShare(int flag){
    	WXWebpageObject webpage = new WXWebpageObject();
    	webpage.webpageUrl = "http://image.baidu.com/search/detail?ct=503316480&z=0&ipn=d&word=%2410000&step_word=&pn=4&spn=0&di=130730920650&pi=&rn=1&tn=baiduimagedetail&is=&istype=0&ie=utf-8&oe=utf-8&in=&cl=2&lm=-1&st=undefined&cs=870106666%2C3761805250&os=533072962%2C417600273&adpicid=0&ln=1472&fr=&fmq=1443405679365_R&ic=undefined&s=undefined&se=&sme=&tab=0&width=&height=&face=undefined&ist=&jit=&cg=&bdtype=0&objurl=http%3A%2F%2Ffile2.gucn.com%2Ffile4%2FCheckCuriofile%2F201105%2FGucn_20110522118321104247Pic2.jpg&fromurl=ippr_z2C%24qAzdH3FAzdH3Fooo_z%26e3B27vg_z%26e3Bv54AzdH3FSj6etvj_C76t5Cijvh_Si5o_z%26e3Bwfr%3FID%3Dn9mnml&gsm=0";
    	WXMediaMessage msg = new WXMediaMessage(webpage);
    	msg.title = "全国仅此一家";
    	msg.description = "10000元现金等你来抢！行动吧！";
    	
    	Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.foodorg);
    	msg.setThumbImage(thumb);
    	
    	SendMessageToWX.Req req = new SendMessageToWX.Req();
    	req.transaction = String.valueOf(System.currentTimeMillis());
    	req.message = msg;
    	req.scene = flag==0?SendMessageToWX.Req.WXSceneSession:SendMessageToWX.Req.WXSceneTimeline;
    	wxApi.sendReq(req);
    }
    
    public static List<Bitmap> getBitmapFromServer(List<String> imagePath){
		URL myFileURL;
		List<Bitmap> pic = new ArrayList<Bitmap>();
		for(int i = 0;i < imagePath.size();i++){
			try{
				System.out.println(imagePath.get(i));
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
    
    private final LocationListener locationListener = new LocationListener() {  
        public void onLocationChanged(Location location) {  
        updateWithNewLocation(location);  
        }  
        public void onProviderDisabled(String provider){  
        updateWithNewLocation(null);  
        }  
        public void onProviderEnabled(String provider){ }  
        public void onStatusChanged(String provider, int status,  
        Bundle extras){ }  
    };  
    
    private void updateWithNewLocation(Location location) {  
        String latLongString;  
        double lat = 0;
        double lng = 0;
        if (location != null) {  
	        lat = location.getLatitude();  
	        lng = location.getLongitude();  
	        latLongString = "纬度:" + lat + "\n经度:" + lng;  
        } else {  
        	latLongString = "无法获取地理信息";  
        }  
        List<Address> addList = null;  
        Geocoder ge = new Geocoder(this);  
        try {  
            addList = ge.getFromLocation(lat, lng, 1);  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        
        if(addList!=null && addList.size()>0){  
            for(int i=0; i<addList.size(); i++){  
                Address ad = addList.get(i);  
                latLongString += "\n";  
                cityName = ad.getFeatureName();
                latLongString += ad.getCountryName() + ";" + ad.getLocality()+";"+ad.getAdminArea()+";"+ad.getSubLocality()+";"+ad.getFeatureName();  
            }  
        }  
        System.out.println("您当前的位置是:\n" +latLongString);
        Message msg = new Message();
		msg.what = 4;
		handler.sendMessage(msg);
        myApp.setCityName(cityName);
    }

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		this.mGestureDetector.onTouchEvent(ev);
		return super.dispatchTouchEvent(ev);
	}

	private GestureDetector.OnGestureListener onGestureListener =   
	        new GestureDetector.SimpleOnGestureListener() {

				@Override
				public boolean onFling(MotionEvent e1, MotionEvent e2,
						float velocityX, float velocityY) {
					System.out.println("move");
					if(e1.getX() > e2.getX()){//move to left
						
					}else if(e1.getX() < e2.getX()-80){//move to right
						Intent intent = new Intent();
						intent.setClass(MainActivity.this, UserInformationActivity.class);
						startActivity(intent);
						overridePendingTransition(R.anim.in_from_left, R.anim.out_of_right);
						finish();
					}else{
						return false;
					}
					return true;
				}
		
	};

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return mGestureDetector.onTouchEvent(event);
	}

	private class NormalPostRequest extends Request<JSONObject> {
	    private Map<String, String> mMap;
	    private Listener<JSONObject> mListener;
	    public NormalPostRequest(String url, Listener<JSONObject> listener,ErrorListener errorListener, Map<String, String> map) {
	        super(Request.Method.POST, url, errorListener);
	             
	        mListener = listener;
	        mMap = map;
	    }
	     
	    //mMap是已经按照前面的方式,设置了参数的实例
	    @Override
	    protected Map<String, String> getParams() throws AuthFailureError {
	        return mMap;
	    }
	     
	    //此处因为response返回值需要json数据,和JsonObjectRequest类一样即可
	    @Override
	    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
	        try {
	            String jsonString = new String(response.data,HttpHeaderParser.parseCharset(response.headers));
	            return Response.success(new JSONObject(jsonString),HttpHeaderParser.parseCacheHeaders(response));
	        } catch (UnsupportedEncodingException e) {
	            return Response.error(new ParseError(e));
	        } catch (JSONException je) {
	            return Response.error(new ParseError(je));
	        }
	    }
	    @Override
	    protected void deliverResponse(JSONObject response) {
	        mListener.onResponse(response);
	    }
	}
	
	
	public void loadImage(Object currItem){
	}
	
}
