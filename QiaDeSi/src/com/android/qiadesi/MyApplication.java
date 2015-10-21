/**
 * 
 */
package com.android.qiadesi;

import android.app.Application;

/**
 * @author Administrator
 *
 */
public class MyApplication extends Application{
	private String userName = "";
	private String cityName = "";
	@Override
	public void onCreate() {
		super.onCreate();
	}

	public void setUserName(String userName){
		this.userName = userName;
	}
	
	public String getUserName(){
		return userName;
	}
	
	public void setCityName(String cityName){
		this.cityName = cityName;
	}
	
	public String getCityName(){
		return this.cityName;
	}
}
