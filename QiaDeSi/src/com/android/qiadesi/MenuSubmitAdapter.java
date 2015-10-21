/**
 * 
 */
package com.android.qiadesi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * @author Administrator
 *
 */
public class MenuSubmitAdapter extends BaseAdapter{
	
	/**
	 * 
	 */
	private Context context;
	private ArrayList<List<String>> list;
	private Handler handler;
	public MenuSubmitAdapter(Context context,ArrayList<List<String>> list,Handler handler) {
		this.context = context;
		this.list = list;
		this.handler = handler;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = LayoutInflater.from(context).inflate(R.layout.submit_adapter, null);
		TextView tvDishesName = (TextView)convertView.findViewById(R.id.tvDishesName);
		TextView tvPriceNumber = (TextView)convertView.findViewById(R.id.tvPriceNumber);
		final Button numberMinus = (Button)convertView.findViewById(R.id.numberMinus);
		final EditText etDishesNumber = (EditText)convertView.findViewById(R.id.etDishesNumber);
		final Button numberAdd = (Button)convertView.findViewById(R.id.numberAdd);
		tvDishesName.setText(list.get(position).get(0).toString());
		tvPriceNumber.setText(list.get(position).get(2).toString());
		etDishesNumber.setText(list.get(position).get(1).toString());
		System.out.println(list.get(position).get(1).toString());
		if(Integer.parseInt(etDishesNumber.getText().toString())<=0){
			numberMinus.setVisibility(View.GONE);
		}
		numberMinus.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(Integer.parseInt(etDishesNumber.getText().toString())>0){
					int str = (int)(Integer.parseInt(etDishesNumber.getText().toString())-1);
					etDishesNumber.setText(str+"");
				}
				if(Integer.parseInt(etDishesNumber.getText().toString())==0){
					numberMinus.setVisibility(View.GONE);
				}
				Message msg = new Message();
				msg.what = 1;
				handler.sendMessage(msg);
			}
		});
		numberAdd.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int str = (int)(Integer.parseInt(etDishesNumber.getText().toString())+1);
				etDishesNumber.setText(str+"");
				if(Integer.parseInt(etDishesNumber.getText().toString())>0){
					numberMinus.setVisibility(View.VISIBLE);
				}
				Message msg = new Message();
				msg.what = 1;
				handler.sendMessage(msg);
			}
		});
		return convertView;
	}

}
