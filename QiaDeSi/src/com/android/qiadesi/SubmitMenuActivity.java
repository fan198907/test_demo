/**
 * 
 */
package com.android.qiadesi;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * @author Administrator
 *
 */
public class SubmitMenuActivity extends Activity{
	private ArrayList<List<String>> menuList = new ArrayList<List<String>>();
	private String restaurantName = "";
	private TextView tvName,tvPhone,tvRestaurantName;
	private Spinner spAddress;
	private ListView lvListMenu;
	private TextView totalPrice;
	private Handler handler;
	private MenuSubmitAdapter menuAdapter;
	private List<String> list_name = new ArrayList<String>();
	private List<String> list_phone = new ArrayList<String>();
	private List<String> list_id_card = new ArrayList<String>();
	private MyApplication myApp;
	private String userName;
	private List<List<String>> myAddressList;
	private String address="";
	private ReadAndWriteXml rwx;
	private ArrayList<String> addressList = new ArrayList<String>();
	private ArrayAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_submit_menu);
		myApp = (MyApplication) this.getApplication();
		try{
			if(this.getIntent() != null){
				menuList = (ArrayList<List<String>>) this.getIntent().getSerializableExtra("list");
				System.out.println(menuList);
				restaurantName = this.getIntent().getExtras().getString("resName");
			}
		}catch(Exception e){
			System.out.println(""+e);
		}
		
		rwx = new ReadAndWriteXml();
		
		tvRestaurantName = (TextView)findViewById(R.id.tvRestaurantName);
		spAddress = (Spinner)findViewById(R.id.spAddress);
		tvName = (TextView)findViewById(R.id.tvName);
		tvPhone = (TextView)findViewById(R.id.tvPhone);
		totalPrice = (TextView)findViewById(R.id.totalPrice);
		
		lvListMenu = (ListView)findViewById(R.id.lvListMenu);
		
		tvRestaurantName.setText(restaurantName);
		
		handler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				switch(msg.what){
				case 1:
					float total = 0;
					for (int i = 0; i < lvListMenu.getChildCount(); i++) {
					     LinearLayout layout = (LinearLayout)lvListMenu.getChildAt(i);// 获得子item的layout
					     TextView tvPriceNumber = (TextView) layout.findViewById(R.id.tvPriceNumber);// 从layout中获得控件,根据其id
					     EditText etDishesNumber = (EditText)layout.findViewById(R.id.etDishesNumber);
					     total += Float.parseFloat(tvPriceNumber.getText().toString())*Float.parseFloat(etDishesNumber.getText().toString());
					}
					totalPrice.setText(""+total+"元");
					//修改姓名电话
					tvName.setText(list_name.get(0));
					tvPhone.setText(list_phone.get(0));
					System.out.print("=="+list_name.get(0)+"=="+list_phone.get(0));
					//修改送餐地址
					System.out.println("fkadlkfj"+myAddressList);
					if(myAddressList!=null){
						for(int i=0;i<myAddressList.size();i++){
							addressList.add(myAddressList.get(i).get(0));
						}
					}
					spAddress.setAdapter(new ArrayAdapter(SubmitMenuActivity.this, android.R.layout.simple_list_item_1, addressList));
					spAddress.invalidate();
					break;
				case 2:
					break;
				case 3:
					break;
				default:
					break;
				}
			}
			
		};
		
		menuAdapter = new MenuSubmitAdapter(SubmitMenuActivity.this, menuList, handler);
		lvListMenu.setAdapter(menuAdapter);
		lvListMenu.invalidate();
		
		userName = myApp.getUserName();
		if(!userName.equals("")){
			new Thread(){

				@Override
				public void run() {
					try {
						List<List<String>> myList = HttpUtil.postRequest(HttpUtil.ADDERS,"selectWithID",userName);
						System.out.println("========="+myList.toString());
						list_name = myList.get(0);
						list_phone = myList.get(1);
						list_id_card = myList.get(2);
						myAddressList = HttpUtil.postRequest(HttpUtil.ADDERS, "selectAddress", userName);
						//默认地址
//						if(myAddressList.size()>0){
//							address = myAddressList.get(0).get(0);
//						}
						Message msg = new Message();
						msg.what = 1;
						handler.sendMessage(msg);
					}catch(Exception e){	
						System.out.println("=="+e+"==");
					}
				}
				
			}.start();
			
		}else{
			Intent intent = new Intent();
			intent.setClass(SubmitMenuActivity.this, UserInformationActivity.class);
			startActivity(intent);
			finish();
		}
		
		
		
	}

}
