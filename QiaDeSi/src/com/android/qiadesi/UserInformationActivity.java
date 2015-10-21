/**
 * 
 */
package com.android.qiadesi;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

/**
 * @author Administrator
 *
 */
public class UserInformationActivity extends Activity implements OnGestureListener{
	private GestureDetector mGestureDetector;
	private String userName = "";
	private MyApplication mApplication;
	private FragmentTransaction transaction;
	private NotLoginPageFragment notLoginFragment;
	private LoginPageFragment loginFragment;
	private Button btnExitLogin;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_information);
		
		btnExitLogin = (Button)findViewById(R.id.btnExitLogin);
		mGestureDetector = new GestureDetector(this);
		mApplication = (MyApplication) this.getApplication();
		userName = mApplication.getUserName();
		transaction = getFragmentManager().beginTransaction();
		if(userName.equals("")){
			if(null == notLoginFragment){
				notLoginFragment = new NotLoginPageFragment();
			}
			transaction.add(R.id.fragment_container, notLoginFragment);
			transaction.commit();
			btnExitLogin.setVisibility(View.GONE);
		}else{
			if(null == loginFragment){
				loginFragment = new LoginPageFragment();
			}
			transaction.add(R.id.fragment_container, loginFragment);
			transaction.commit();
			btnExitLogin.setVisibility(View.VISIBLE);
		}
		btnExitLogin.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(null == notLoginFragment){
					notLoginFragment = new NotLoginPageFragment();
				}
				transaction = getFragmentManager().beginTransaction();
				transaction.replace(R.id.fragment_container, notLoginFragment);
				transaction.commit();
				btnExitLogin.setVisibility(View.GONE);
			}
		});
	}

	/* (non-Javadoc)
	 * @see android.view.GestureDetector.OnGestureListener#onDown(android.view.MotionEvent)
	 */
	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}

	/* (non-Javadoc)
	 * @see android.view.GestureDetector.OnGestureListener#onShowPress(android.view.MotionEvent)
	 */
	@Override
	public void onShowPress(MotionEvent e) {
		
	}

	/* (non-Javadoc)
	 * @see android.view.GestureDetector.OnGestureListener#onSingleTapUp(android.view.MotionEvent)
	 */
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}

	/* (non-Javadoc)
	 * @see android.view.GestureDetector.OnGestureListener#onScroll(android.view.MotionEvent, android.view.MotionEvent, float, float)
	 */
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return false;
	}

	/* (non-Javadoc)
	 * @see android.view.GestureDetector.OnGestureListener#onLongPress(android.view.MotionEvent)
	 */
	@Override
	public void onLongPress(MotionEvent e) {
		
	}

	/* (non-Javadoc)
	 * @see android.view.GestureDetector.OnGestureListener#onFling(android.view.MotionEvent, android.view.MotionEvent, float, float)
	 */
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		System.out.println("move");
		if(e1.getX() > e2.getX()){//move to left
			Intent intent = new Intent();
			intent.setClass(UserInformationActivity.this, MainActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.in_from_right, R.anim.out_of_left);
			finish();
		}else if(e1.getX() < e2.getX()){//move to right
		}else{
			return false;
		}
		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return mGestureDetector.onTouchEvent(event);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			Intent intent = new Intent();
			intent.setClass(UserInformationActivity.this, MainActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.in_from_right, R.anim.out_of_left);
			finish();
		}
		return true;
	}

	
}
