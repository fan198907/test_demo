/**
 * 
 */
package com.android.qiadesi;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author Administrator
 *
 */
public class AddressActivity extends Activity{
//	private TextView tvEditDefaultAddress;
	private ListView lvAddress;
	private ReadAndWriteXml rwx;
	private ArrayList<String> addressList = new ArrayList<String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_address);
//		tvEditDefaultAddress = (TextView)findViewById(R.id.tvEditDefaultAddress);
		lvAddress = (ListView)findViewById(R.id.lvAddress);
		rwx = new ReadAndWriteXml();
		File file = new File(this.getFilesDir(), "address.xml");
		if(file.exists()){
			addressList = rwx.readAddress(this, "address.xml", "address");
		}
		if(addressList != null && addressList.size()>0){
			lvAddress.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, addressList));
			lvAddress.invalidate();
		}
		lvAddress.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				
				return true;
			}
		});
	}

}
