/**
 * 
 */
package com.android.qiadesi;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

/**
 * @author Administrator
 *
 */
public class LoginActivity extends Activity{
	private EditText etUser,etPassword;
	private Button btnLogin,btnRegister;
	private CheckBox checkBoxremenber;
	private Handler handler;
	private boolean is_login=false;
	private MyApplication myApp;
	private ProgressDialog progressDialog;
	private int count = 0;
	private long firClick,secClick;
	private ReadAndWriteXml rwx;
	private String strUserName = "";
	private String strPassword = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		myApp = (MyApplication) this.getApplication();
		rwx = new ReadAndWriteXml();
		
		etUser = (EditText)findViewById(R.id.etUser);
		etPassword = (EditText)findViewById(R.id.etPassword);
		
		btnLogin = (Button)findViewById(R.id.btnLogin);
		btnRegister = (Button)findViewById(R.id.btnRegister);
		
		checkBoxremenber = (CheckBox)findViewById(R.id.checkBoxremenber);
		
		btnLogin.setOnClickListener(new ButtonListener());
		btnRegister.setOnClickListener(new ButtonListener());
		handler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				switch(msg.what){
					case 1:
						if(is_login){
							//保存用户名，密码
							myApp.setUserName(etUser.getText().toString());
							System.out.println("set username"+etUser.getText().toString());
							Intent loginIntent = new Intent();
							loginIntent.setClass(LoginActivity.this, MainActivity.class);
							finish();
							startActivity(loginIntent);
							progressDialog.dismiss();
							if(checkBoxremenber.isChecked()){
								new Thread(){

									@Override
									public void run() {
										File file = new File(LoginActivity.this.getFilesDir(), "user.xml");
										if(file.exists()){
											file.delete();
										}
										rwx.writeNameXml(LoginActivity.this, "user.xml", etUser.getText().toString(), etPassword.getText().toString());
									}
									
								}.start();
							}else{
								File file = new File(LoginActivity.this.getFilesDir(), "user.xml");
								if(file.exists()){
									file.delete();
								}
							}
						}else{
							progressDialog.dismiss();
							Toast toast = Toast.makeText(LoginActivity.this, "用户名或密码错误，请确认", Toast.LENGTH_SHORT);
							toast.setGravity(Gravity.CENTER, 0, 0);
							toast.show();
						}
						
						break;
					case 2:
						progressDialog.dismiss();
						Toast toast = Toast.makeText(LoginActivity.this, "登录超时请检查网络是否连接正确", Toast.LENGTH_SHORT);
						toast.setGravity(Gravity.CENTER, 0, 0);
						toast.show();
						break;
					case 3:
						if(!strUserName.equals("") && !strPassword.equals(""))
						{
							checkBoxremenber.setChecked(true);
							etUser.setText(strUserName);
							etPassword.setText(strPassword);
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
				try{
					File file = new File(LoginActivity.this.getFilesDir(), "user.xml");
					if(file.exists()){
						strUserName = rwx.readXml(LoginActivity.this, "user.xml", "userName");
						strPassword = rwx.readXml(LoginActivity.this, "user.xml", "password");
						Message msg = new Message();
						msg.what = 3;
						handler.sendMessage(msg);
					}
				}catch(Exception e){}
			}
			
		}.start();
	}

	class ButtonListener implements View.OnClickListener{

		@Override
		public void onClick(View v) {
			switch(v.getId()){
				case R.id.btnLogin:
					if(etUser.getText().toString().equals("") || etPassword.getText().toString().equals("")){
						Toast toast = Toast.makeText(LoginActivity.this, "用户名或密码不能为空", Toast.LENGTH_SHORT);
						toast.setGravity(Gravity.CENTER, 0, 0);
						toast.show();
						return;
					}
					progressDialog = ProgressDialog.show(LoginActivity.this, "请稍等...", "正在登录...", true);
					isLogin(etUser.getText().toString(),etPassword.getText().toString());
					break;
				case R.id.btnRegister:
					Intent intent = new Intent();
					intent.setClass(LoginActivity.this, RegisterActivity.class);
					finish();
					startActivity(intent);
					break;
				default:
					break;
			}
		}
		
	}
	
	public void isLogin(final String user,final String password){
		final Map<String, String> map = new HashMap<String, String>();
		map.put("userName", user);
		map.put("password", password);
		new Thread(){

			@Override
			public void run() {
				if(user.equals("test") && password.equals("test")){
					
				}
				try {
					is_login=HttpUtil.postRequest(HttpUtil.ADDERS, map, "login");
					Message msg = new Message();
					msg.what = 1;
					handler.sendMessage(msg);
				} catch (Exception e) {
					
					Message msg1 = new Message();
					msg1.what = 2;
					handler.sendMessage(msg1);
					e.printStackTrace();
				}
			}
			
		}.start();
		
	}
	
	class onDoubleClick implements View.OnTouchListener{  
		  
	    @Override  
	    public boolean onTouch(View v, MotionEvent event) {  
	        if(MotionEvent.ACTION_DOWN == event.getAction()){  
	            count++;  
	            if(count == 1){  
	                firClick = System.currentTimeMillis();  
	                  
	            } else if (count == 2){  
	                secClick = System.currentTimeMillis();  
	                if(secClick - firClick < 500){  
	                    //双击事件  
	                	if(progressDialog != null){
		                    if(progressDialog.isShowing()){
		                    	progressDialog.dismiss();
		                    }
	                	}
	                }  
	                count = 0;  
	                firClick = 0;  
	                secClick = 0;  
	                  
	            }  
	        }  
	        return true;  
	    }  
	      
	} 
	
}
