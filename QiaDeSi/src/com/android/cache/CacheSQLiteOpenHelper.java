/**
 * 
 */
package com.android.cache;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author Administrator
 *
 */
public class CacheSQLiteOpenHelper extends SQLiteOpenHelper{

	/**
	 * @param context
	 * @param name
	 * @param factory
	 * @param version
	 */
	public final static String DATABASE_NAME="cache.db";
	public final static int DATABASE_VERSION=1;
	private String TABLE_NAME = "restaurant";
	private String TABLE_NAME_SCENIC = "scenic";
	private String _ID = "ID";
	private String NAME = "name";
	private String STAR = "star";
	private String SALES = "sales";
	private String SEND_UP = "send_up";
	private String COSTS = "costs";
	private String TIME = "time";
	private String PIC_ADDRESS = "pic_address";
	private String ADDRESS = "address";
	private String IS_BUSINESS = "is_business";
	private String BUSINESS_TIME = "business_time";
	private String CUISINE = "cuisine";
	private String IS_RECOMMEND = "is_recommend";
	private String DESCRIPT = "descript";
	private String WRITE_TIME = "write_time";
	public CacheSQLiteOpenHelper(Context context) {
		super(context,DATABASE_NAME,null,DATABASE_VERSION);
	}
	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" + _ID +" INTEGER primary key autoincrement, " + NAME + " text, " 
	+STAR + " text, " + SALES + " text, " + SEND_UP + " text, " +COSTS + " text, " + TIME + " text, " +PIC_ADDRESS + " text, " + ADDRESS
	 + " text, " + IS_BUSINESS + " text, " + BUSINESS_TIME + " text, " + CUISINE + " text, " +IS_RECOMMEND + " text, " + WRITE_TIME + " text)";
		db.execSQL(sql);
		
		String sql_scenic = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_SCENIC + "(" + NAME +" text primary key autoincrement, " + PIC_ADDRESS + " text, " 
				+DESCRIPT + " text, " + ADDRESS + " text, " + IS_RECOMMEND + " text, " + WRITE_TIME + " text)";
		db.execSQL(sql_scenic);
	}
	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
		db.execSQL(sql);
		
		String sql_scenic = "DROP TABLE IF EXISTS " + TABLE_NAME_SCENIC;
		db.execSQL(sql_scenic);
		onCreate(db);
		
	}
	
	public ArrayList<ArrayList<String>> selectWithType(String type,String value){
		SQLiteDatabase db = this.getReadableDatabase();
		ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
		if(type.equals("selectFood")){
			Cursor cursor = db.rawQuery("SELECT  * FROM "+TABLE_NAME, null);
			ArrayList<String> list_id = new ArrayList<String>();
			ArrayList<String> list_name = new ArrayList<String>();
			ArrayList<String> list_star = new ArrayList<String>();
			ArrayList<String> list_sales = new ArrayList<String>();
			ArrayList<String> list_send_up = new ArrayList<String>();
			ArrayList<String> list_costs = new ArrayList<String>();
			ArrayList<String> list_time = new ArrayList<String>();
			ArrayList<String> list_pic_address = new ArrayList<String>();
			ArrayList<String> list_address = new ArrayList<String>();
			ArrayList<String> list_is_business = new ArrayList<String>();
			ArrayList<String> list_business_time = new ArrayList<String>();
			ArrayList<String> list_cuisine = new ArrayList<String>();
			ArrayList<String> list_is_recommend = new ArrayList<String>();
			
			
			while(cursor.moveToNext()){
				String id = cursor.getString(cursor.getColumnIndex(_ID));
				String name = cursor.getString(cursor.getColumnIndex(NAME));
				String star = cursor.getString(cursor.getColumnIndex(STAR));
				String sales = cursor.getString(cursor.getColumnIndex(SALES));
				String send_up = cursor.getString(cursor.getColumnIndex(SEND_UP));
				String costs = cursor.getString(cursor.getColumnIndex(COSTS));
				String time = cursor.getString(cursor.getColumnIndex(TIME));
				String pic_address = cursor.getString(cursor.getColumnIndex(PIC_ADDRESS));
				String address = cursor.getString(cursor.getColumnIndex(ADDRESS));
				String is_business = cursor.getString(cursor.getColumnIndex(IS_BUSINESS));
				String business_time = cursor.getString(cursor.getColumnIndex(BUSINESS_TIME));
				String cuisine = cursor.getString(cursor.getColumnIndex(CUISINE));
				String is_recommend = cursor.getString(cursor.getColumnIndex(IS_RECOMMEND));
				list_id.add(id);
				list_name.add(name);
				list_star.add(star);
				list_sales.add(sales);
				list_send_up.add(send_up);
				list_costs.add(costs);
				list_time.add(time);
				list_pic_address.add(pic_address);
				list_address.add(address);
				list_is_business.add(is_business);
				list_business_time.add(business_time);
				list_cuisine.add(cuisine);
				list_is_recommend.add(is_recommend);
			}
			list.add(list_name);
			list.add(list_star);
			list.add(list_sales);
			list.add(list_send_up);
			list.add(list_costs);
			list.add(list_time);
			list.add(list_pic_address);
			list.add(list_address);
			list.add(list_is_business);
			list.add(list_business_time);
			list.add(list_cuisine);
			list.add(list_is_recommend);
			return list;
		}else if(type.equals("selectFoodEachRestaurant")){
			Cursor cursor = db.rawQuery("select * from restaurant where name='"+value+"'", null);
			ArrayList<String> list_pic_address = new ArrayList<String>();
			ArrayList<String> list_name = new ArrayList<String>();
			ArrayList<String> list_cuisine = new ArrayList<String>();
			ArrayList<String> list_star = new ArrayList<String>();
			ArrayList<String> list_business_time = new ArrayList<String>();
			ArrayList<String> list_is_business = new ArrayList<String>();
			ArrayList<String> list_address = new ArrayList<String>();
			ArrayList<String> list_time = new ArrayList<String>();
			ArrayList<String> list_send_up = new ArrayList<String>();
			
			
			while(cursor.moveToNext()){
				String pic_address = cursor.getString(cursor.getColumnIndex(PIC_ADDRESS));
				String name = cursor.getString(cursor.getColumnIndex(NAME));
				String cuisine = cursor.getString(cursor.getColumnIndex(CUISINE));
				String star = cursor.getString(cursor.getColumnIndex(STAR));
				String business_time = cursor.getString(cursor.getColumnIndex(BUSINESS_TIME));
				String is_business = cursor.getString(cursor.getColumnIndex(IS_BUSINESS));
				String address = cursor.getString(cursor.getColumnIndex(ADDRESS));
				String time = cursor.getString(cursor.getColumnIndex(TIME));
				String send_up = cursor.getString(cursor.getColumnIndex(SEND_UP));
				
				list_pic_address.add(pic_address);
				list_name.add(name);
				list_cuisine.add(cuisine);
				list_star.add(star);
				list_business_time.add(business_time);
				list_is_business.add(is_business);
				list_address.add(address);
				list_time.add(time);
				list_send_up.add(send_up);
			}
			list.add(list_pic_address);
			list.add(list_name);
			list.add(list_cuisine);
			list.add(list_star);
			list.add(list_business_time);
			list.add(list_is_business);
			list.add(list_address);
			list.add(list_time);
			list.add(list_send_up);
			return list;
		}else if(type.equals("selectRecommend")){
			Cursor cursor = db.rawQuery("select name,address,pic_address from restaurant where is_recommend='true' union select name,address,pic_address from scenic where is_recommend='true'", null);
			ArrayList<String> list_name = new ArrayList<String>();
			ArrayList<String> list_address = new ArrayList<String>();
			ArrayList<String> list_pic_address = new ArrayList<String>();
			
			while(cursor.moveToNext()){
				String name = cursor.getString(cursor.getColumnIndex(NAME));
				String address = cursor.getString(cursor.getColumnIndex(ADDRESS));
				String pic_address = cursor.getString(cursor.getColumnIndex(PIC_ADDRESS));
				
				list_name.add(name);
				list_address.add(address);
				list_pic_address.add(pic_address);
			}
			list.add(list_name);
			list.add(list_address);
			list.add(list_pic_address);
			return list;
		}else if(type.equals("selectSpot")){
			Cursor cursor = db.rawQuery("select * from scenic", null);
			ArrayList<String> list_name = new ArrayList<String>();
			ArrayList<String> list_pic_address = new ArrayList<String>();
			ArrayList<String> list_address = new ArrayList<String>();
			
			while(cursor.moveToNext()){
				String name = cursor.getString(cursor.getColumnIndex(NAME));
				String pic_address = cursor.getString(cursor.getColumnIndex(PIC_ADDRESS));
				String address = cursor.getString(cursor.getColumnIndex(ADDRESS));
				
				list_name.add(name);
				list_pic_address.add(pic_address);
				list_address.add(address);
			}
			list.add(list_name);
			list.add(list_pic_address);
			list.add(list_address);
			return list;
		}else{
			return null;
		}
	}
	
	public Boolean selectTypeBoolean(String type,String value){
		SQLiteDatabase db = this.getReadableDatabase();
		if(type.equals("type_restaurant")){
			Cursor cursor = db.rawQuery("select * from restaurant where name='"+value+"'", null);
			if(cursor.moveToNext()){
				return true;
			}else{
				return false;
			}
		}else if(type.equals("type_spot")){
			Cursor cursor = db.rawQuery("select * from scenic where name='"+value+"'", null);
			if(cursor.moveToNext()){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}
	
	public void insertRestaurant(ArrayList<ArrayList<String>> list){
		SQLiteDatabase db = this.getWritableDatabase();
		try{
			for(int i=0;i<list.size();i++){
				db.execSQL("insert into restaurant(" + _ID + "," + NAME + ","  +STAR + ","  + SALES + ","  + SEND_UP + "," + COSTS + "," + TIME + "," 
						+ PIC_ADDRESS + "," + ADDRESS + "," + IS_BUSINESS + "," + BUSINESS_TIME + "," + CUISINE + "," + IS_RECOMMEND + ")"+"values('"
						+ list.get(0).get(i) + "','" + list.get(1).get(i) + "','" + list.get(2).get(i) + "','" + list.get(3).get(i) + "','"
						 + list.get(4).get(i) + "','" + list.get(5).get(i) + "','" + list.get(6).get(i) + "','" + list.get(7).get(i) + "','"
						 + list.get(8).get(i) + "','" + list.get(9).get(i) + "','" + list.get(10).get(i) + "','" + list.get(11).get(i) + "','"
						 + list.get(12).get(i) + "')");
			}
			db.close();
		}catch(Exception e){}
	}
	
	public void insertScenic(ArrayList<ArrayList<String>> list){
		SQLiteDatabase db = this.getWritableDatabase();
		try{
			for(int i=0;i<list.size();i++){
				db.execSQL("insert into scenic(" + NAME + PIC_ADDRESS + "," + ADDRESS +")"+"values('" + list.get(0).get(i) +"','" 
						+ list.get(1).get(i) +"','" + list.get(2).get(i)  +"')");
			}
			db.close();
		}catch(Exception e){}
	}
	
}
