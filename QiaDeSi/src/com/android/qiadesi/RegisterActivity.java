/**
 * 
 */
package com.android.qiadesi;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.cookie.SM;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.test.suitebuilder.annotation.SmallTest;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Administrator
 *
 */
public class RegisterActivity extends Activity{
	private EditText etUserName,etUserPassword,etUserPhone,etUserId,etVerification;
	private Button userRegister,userCancle,btnVerification;
	private Handler handler;
	private MyApplication myApplication;
	private TextView tvUserBc,tvPasswordBc,tvUserPhoneBc,tvUserIdBc;
	private LinearLayout linearLayout;
	private String appkey = "99ab8b4df5f4";
	private String appsecret = "c975c6297b936234bdd3cab3e68e2efc";
	private EventHandler eventHandler;
	private int leftTime = 60;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_register);
		
		myApplication = (MyApplication) this.getApplication();
		
		etUserName = (EditText)findViewById(R.id.etUserName);
		etUserPassword = (EditText)findViewById(R.id.etUserPassword);
		etUserPhone = (EditText)findViewById(R.id.etUserPhone);
		etUserId = (EditText)findViewById(R.id.etUserId);
		etVerification = (EditText)findViewById(R.id.etVerification);
		
		userRegister = (Button)findViewById(R.id.userRegister);
		userCancle = (Button)findViewById(R.id.userCancle);
		btnVerification = (Button)findViewById(R.id.btnVerification);
		
		etUserName.setOnFocusChangeListener(new EditViewOnFocusChangeListener());
		etUserPassword.setOnFocusChangeListener(new EditViewOnFocusChangeListener());
		etUserPhone.setOnFocusChangeListener(new EditViewOnFocusChangeListener());
		etUserId.setOnFocusChangeListener(new EditViewOnFocusChangeListener());
		
		userRegister.setOnClickListener(new ButtonListener());
		userCancle.setOnClickListener(new ButtonListener());
		btnVerification.setOnClickListener(new ButtonListener());
		
		tvUserBc = (TextView)findViewById(R.id.tvUserBc);
		tvPasswordBc = (TextView)findViewById(R.id.tvPasswordBc);
		tvUserPhoneBc = (TextView)findViewById(R.id.tvUserPhoneBc);
		tvUserIdBc = (TextView)findViewById(R.id.tvUserIdBc);
		
		linearLayout = (LinearLayout)findViewById(R.id.linearLayout);
		linearLayout.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				linearLayout.setFocusable(true);
				linearLayout.setFocusableInTouchMode(true);
				linearLayout.requestFocus();
				return false;
			}
		});
		
		handler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				switch(msg.what){
					case 1:
						Toast toast1 = Toast.makeText(RegisterActivity.this, "该用户名已经被注册过", Toast.LENGTH_SHORT);
						toast1.setGravity(Gravity.CENTER, 0, 0);
						toast1.show();
						break;
					case 2:
						tvPasswordBc.setBackgroundResource(R.drawable.right);
						break;
					case 3:
						Toast toast3 = Toast.makeText(RegisterActivity.this, "该手机号已经被注册过", Toast.LENGTH_SHORT);
						toast3.setGravity(Gravity.CENTER, 0, 0);
						toast3.show();
						break;
					case 4:
						tvUserIdBc.setBackgroundResource(R.drawable.right);
						break;
					case 5:
						SMSSDK.submitVerificationCode("86", etUserPhone.getText().toString(), etVerification.getText().toString()); 
						
						break;
					case 6:
						tvUserBc.setBackgroundResource(R.drawable.right);
						break;
					case 8:
						tvUserPhoneBc.setBackgroundResource(R.drawable.right);
						break;
					case -9:
						btnVerification.setText("重新发送("+leftTime--+")");
						break;
					case -8:
						btnVerification.setText("获取验证码");
						btnVerification.setClickable(true);
						break;
					case 9:
						System.out.println("dddddddddddddd");
						int event = msg.arg1;
						int result = msg.arg2;
						Object data = msg.obj;
						if(result == SMSSDK.RESULT_COMPLETE){
							if(event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE){
								System.out.println("eeeeeeee");
								Toast.makeText(RegisterActivity.this, "提交验证码成功", Toast.LENGTH_SHORT).show();
								//保存用户名
								myApplication.setUserName(etUserName.getText().toString());
								//进入主界面
								Intent intent = new Intent();
								intent.setClass(RegisterActivity.this, MainActivity.class);
								finish();
								startActivity(intent);
							}else if(event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
								System.out.println("fffffffff");
								Toast.makeText(RegisterActivity.this, "验证码已经发送", Toast.LENGTH_SHORT).show();
							}else{
								System.out.println(event+"");
								System.out.println("ccccccc");
							}
						}else{
							Toast.makeText(RegisterActivity.this, "发送失败，请检查网络是否连接", Toast.LENGTH_SHORT).show();
						}
						break;
					default:
						break;
				}
			}
			
		};
		initSdk();
	}
	
	public void initSdk(){
		SMSSDK.initSDK(RegisterActivity.this, appkey, appsecret);
		eventHandler = new EventHandler(){

			@Override
			public void afterEvent(int event, int result, Object data) {
				Message msg = new Message();
				msg.arg1 = event;
				msg.arg2 = result;
				msg.obj = data;
				msg.what = 9;
				handler.sendMessage(msg);
			}
			
		};
		SMSSDK.registerEventHandler(eventHandler);
	}

	class ButtonListener implements View.OnClickListener{

		@Override
		public void onClick(View v) {
			switch(v.getId()){
				case R.id.userRegister:
					System.out.println("0000000");
					if(etUserName.getText().toString().equals("") || etUserPassword.getText().toString().equals("") || etUserPhone.getText().toString().equals("") || etUserId.getText().toString().equals("")){
						Toast toast = Toast.makeText(RegisterActivity.this, "请确认信息是否正确", Toast.LENGTH_SHORT);
						toast.setGravity(Gravity.CENTER, 0, 0);
						toast.show();
						return;
					}
					final Map<String,String> map = new HashMap<String,String>();
					map.put("userName", etUserName.getText().toString());
					map.put("password", etUserPassword.getText().toString());
					System.out.println("password= "+etUserPassword.getText().toString());
					map.put("userPhone", etUserPhone.getText().toString());
					map.put("userID", etUserId.getText().toString());
					new Thread(){

						@Override
						public void run() {
							try {
								boolean state = HttpUtil.postRequest(HttpUtil.ADDERS, map, "register");
								System.out.println("---"+state);
								if(state){
									Message msg = new Message();
									msg.what = 5;
									handler.sendMessage(msg);
								}
							} catch (Exception e) {
								e.printStackTrace();
								System.out.println(e);
							}
						}
						
					}.start();
					break;
				case R.id.userCancle:
					Intent intent = new Intent();
					intent.setClass(RegisterActivity.this, LoginActivity.class);
					finish();
					startActivity(intent);
					break;
				case R.id.btnVerification:
					String phoneNums = etUserPhone.getText().toString();
					if(!judgePhoneNums(phoneNums)){
						return;
					}
					System.out.println(phoneNums+"aaaaaaaaaaa");
					try{
						SMSSDK.getVerificationCode("86", phoneNums);
					}catch(Exception e){
						System.out.println(e+"");
					}
					btnVerification.setClickable(false);
					btnVerification.setText("重新发送("+leftTime--+")");
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							for(int i=60;i>0;i--){
								handler.sendEmptyMessage(-9);
								if(leftTime<0){
									
									break;
								}
								try{
									Thread.sleep(1000);
								}catch(Exception e){}
							}
							handler.sendEmptyMessage(-8);
							leftTime = 60;
						}
					}).start();
					break;
				default:
					break;
			}
		}
		
	}
	
	private Boolean judgePhoneNums(String phoneNum){
		
		return true;
	}
	
	class EditViewOnFocusChangeListener implements View.OnFocusChangeListener{

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			switch(v.getId()){
				case R.id.etUserName:
					if(!hasFocus){
						if(etUserName.getText().toString().equals("")){
							return;
						}
						final Map<String,String> map1 = new HashMap<String,String>();
						map1.put("userName", etUserName.getText().toString());
						new Thread(){

							@Override
							public void run() {
								try {
									boolean state = HttpUtil.postRequest(HttpUtil.ADDERS, map1, "name");
									if(state){
										Message msg1 = new Message();
										msg1.what = 1;
										handler.sendMessage(msg1);
									}else{
										Message msg6 = new Message();
										msg6.what = 6;
										handler.sendMessage(msg6);
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							
						}.start();
						
					}else{
						tvUserBc.setBackgroundResource(R.drawable.wrong);
					}
					break;
				case R.id.etUserPassword:
					if(!hasFocus){
						if(etUserPassword.getText().toString().equals("")){
							return;
						}
						Message msg2 = new Message();
						msg2.what = 2;
						handler.sendMessage(msg2);
					}else{
						tvPasswordBc.setBackgroundResource(R.drawable.wrong);
					}
					break;
				case R.id.etUserPhone:
					if(!hasFocus){
						if(etUserPhone.getText().toString().equals("")){
							return;
						}
						final Map<String,String> map2 = new HashMap<String,String>();
						map2.put("userPhone", etUserPhone.getText().toString());
						new Thread(){

							@Override
							public void run() {
								try {
									boolean state = HttpUtil.postRequest(HttpUtil.ADDERS, map2, "phone");
									if(state){
										Message msg3 = new Message();
										msg3.what = 3;
										handler.sendMessage(msg3);
									}else{
										Message msg8 = new Message();
										msg8.what = 8;
										handler.sendMessage(msg8);
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							
						}.start();
						
					}else{
						tvUserPhoneBc.setBackgroundResource(R.drawable.wrong);
					}
					break;
				case R.id.etUserId:
					if(!hasFocus){
						if(etUserId.getText().toString().equals("")){
							return;
						}
						Message msg4 = new Message();
						msg4.what = 4;
						handler.sendMessage(msg4);
					}else{
						tvUserIdBc.setBackgroundResource(R.drawable.wrong);
					}
					break;
				default:
					break;
			}
		}
		
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
		SMSSDK.unregisterEventHandler(eventHandler);
	}
	
}
