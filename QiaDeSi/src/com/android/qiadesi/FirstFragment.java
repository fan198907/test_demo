/**
 * 
 */
package com.android.qiadesi;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * @author Administrator
 *
 */
public class FirstFragment extends Fragment{

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_first, container,false);
		TextView firstTvFood = (TextView)v.findViewById(R.id.firstTvFood);
		TextView firstTvScenic = (TextView)v.findViewById(R.id.firstTvScenic);
		TextView firstTvHotel = (TextView)v.findViewById(R.id.firstTvHotel);
		TextView firstTvFriendly = (TextView)v.findViewById(R.id.firstTvFriendly);
		TextView firstTvShopping = (TextView)v.findViewById(R.id.firstTvShopping);
		TextView firstTvAll = (TextView)v.findViewById(R.id.firstTvAll);
		firstTvFood.setOnClickListener(new clickListener());
		firstTvScenic.setOnClickListener(new clickListener());
		firstTvHotel.setOnClickListener(new clickListener());
		firstTvFriendly.setOnClickListener(new clickListener());
		firstTvShopping.setOnClickListener(new clickListener());
		firstTvAll.setOnClickListener(new clickListener());
		return v;
	}
	

	class clickListener implements View.OnClickListener{

		@Override
		public void onClick(View v) {
			switch(v.getId()){
				case R.id.firstTvFood:
					Intent intent1 = new Intent();
					intent1.setClass(getActivity(), MyListViewActivity.class);
					Bundle bundle1 = new Bundle();
					bundle1.putString("type", "food");
					intent1.putExtras(bundle1);
					startActivity(intent1);
					break;
				case R.id.firstTvScenic:
					Intent intent2 = new Intent();
					intent2.setClass(getActivity(), MyListViewActivity.class);
					Bundle bundle2 = new Bundle();
					bundle2.putString("type", "spot");
					intent2.putExtras(bundle2);
					startActivity(intent2);
					break;
				case R.id.firstTvHotel:
					break;
				case R.id.firstTvFriendly:
					break;
				case R.id.firstTvShopping:
					break;
				case R.id.firstTvAll:
					break;
				default:
					break;
			}
		}
		
	}
}
