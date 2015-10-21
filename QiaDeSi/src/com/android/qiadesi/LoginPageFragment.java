/**
 * 
 */
package com.android.qiadesi;

import java.util.ArrayList;
import java.util.List;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * @author Administrator
 *
 */
public class LoginPageFragment extends Fragment{
	private List<String> list_name = new ArrayList<String>();
	private List<String> list_phone = new ArrayList<String>();
	private List<String> list_id_card = new ArrayList<String>();
	private Handler handler;
	private String userName;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_login, container,false);
		final TextView txtName = (TextView)v.findViewById(R.id.txtName);
		final TextView txtPhone = (TextView)v.findViewById(R.id.txtPhone);
		final TextView txtID = (TextView)v.findViewById(R.id.txtID);
		final TextView txtUserName = (TextView)v.findViewById(R.id.txtUserName);
		
		handler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				switch(msg.what){
					case 1:
						if(list_name.size()>0){
							txtName.setText(list_name.get(0).toString());
							txtUserName.setText(list_name.get(0).toString());
						}
						if(list_phone.size()>0){
							txtPhone.setText(list_phone.get(0).toString());
						}
						if(list_id_card.size()>0){
							txtID.setText(list_id_card.get(0).toString());
						}
						break;
					default:
						break;
				}
			}
			
		};
		
		MyApplication mApplication = (MyApplication)getActivity().getApplication();
		userName = mApplication.getUserName();
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
						
					} catch (Exception e) {
						System.out.println(e+"");
						e.printStackTrace();
						
					}
					Message msg = new Message();
					msg.what = 1;
					handler.sendMessage(msg);
				}
				
			}.start();
		}
		return v;
	}

}
