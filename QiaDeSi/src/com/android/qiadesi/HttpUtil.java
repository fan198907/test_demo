/**
 * 
 */
package com.android.qiadesi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author Administrator
 *
 */
public class HttpUtil {
//	public static final String ADDERS = "http://192.168.42.197:8080/orderdishes/orderdishes_servlet";
	public static final String ADDERS = "http://192.168.1.101:8080/orderdishes/orderdishes_servlet";
	public static List<List<String>> postRequest(String url,String name,String restaurantName) throws Exception{
		System.out.println("postRequest");
		HttpPost request = new HttpPost(url);
		List<List<String>> list = new ArrayList<List<String>>();
		List<NameValuePair> param = new ArrayList<NameValuePair>();  
        param.add(new BasicNameValuePair("type", name));
        if(name.equals("selectFoodEachRestaurant") || name.equals("menuOfRestaurant")){
        	param.add(new BasicNameValuePair("restaurant_name", restaurantName));
        }else if(name.equals("selectWithID")){
        	param.add(new BasicNameValuePair("userName", restaurantName));
        }else if(name.equals("selectAddress")){
        	param.add(new BasicNameValuePair("userName", restaurantName));
        }
		request.setEntity(new UrlEncodedFormEntity(param, HTTP.UTF_8));
		HttpResponse httpResponse = new DefaultHttpClient().execute(request);
		String retStr = EntityUtils.toString(httpResponse.getEntity());
		System.out.println(retStr);
		JSONArray array = new JSONArray(retStr);
		if(name.equals("selectSpot")){
			List<String> list_name = new ArrayList<String>();
			List<String> list_address = new ArrayList<String>();
			for(int i=0;i<array.length();i++){
				JSONObject object = array.getJSONObject(i);
				list_name.add(object.getString("name"));
				list_address.add(object.getString("pic_address"));
			}
			list.add(list_name);
			list.add(list_address);
		}else if(name.equals("selectFood")){
			List<String> list_name = new ArrayList<String>();
			List<String> list_star = new ArrayList<String>();
			List<String> list_sales = new ArrayList<String>();
			List<String> list_send_up = new ArrayList<String>();
			List<String> list_costs = new ArrayList<String>();
			List<String> list_time = new ArrayList<String>();
			List<String> list_pic_address = new ArrayList<String>();
			List<String> list_is_business = new ArrayList<String>();
			for(int i=0;i<array.length();i++){
				JSONObject object = array.getJSONObject(i);
				list_name.add(object.getString("name"));
				list_star.add(object.getString("star"));
				list_sales.add(object.getString("sales"));
				list_send_up.add(object.getString("send_up"));
				list_costs.add(object.getString("costs"));
				list_time.add(object.getString("time"));
				list_pic_address.add(object.getString("pic_address"));
				list_is_business.add(object.getString("is_business"));
			}
			list.add(list_name);
			list.add(list_star);
			list.add(list_sales);
			list.add(list_send_up);
			list.add(list_costs);
			list.add(list_time);
			list.add(list_pic_address);
			list.add(list_is_business);
		}else if(name.equals("selectFoodEachRestaurant")){
			
			List<String> list_pic_address = new ArrayList<String>();
			List<String> list_name = new ArrayList<String>();
			List<String> list_cuisine = new ArrayList<String>();
			List<String> list_star = new ArrayList<String>();
			List<String> list_business_time = new ArrayList<String>();
			List<String> list_is_business = new ArrayList<String>();
			List<String> list_address = new ArrayList<String>();
			List<String> list_pic_pre_time = new ArrayList<String>();
			List<String> list_send_up = new ArrayList<String>();
			
			for(int i=0;i<array.length();i++){
				JSONObject object = array.getJSONObject(i);
				list_pic_address.add(object.getString("pic_address"));
				list_name.add(object.getString("name"));
				list_cuisine.add(object.getString("cuisine"));
				list_star.add(object.getString("star"));
				list_business_time.add(object.getString("business_time"));
				list_is_business.add(object.getString("is_business"));
				list_address.add(object.getString("address"));
				list_pic_pre_time.add(object.getString("time"));
				list_send_up.add(object.getString("send_up"));
			}
			list.add(list_pic_address);
			list.add(list_name);
			list.add(list_cuisine);
			list.add(list_star);
			list.add(list_business_time);
			list.add(list_is_business);
			list.add(list_address);
			list.add(list_pic_pre_time);
			list.add(list_send_up);
		}else if(name.equals("menuOfRestaurant")){
			List<String> list_pic_address = new ArrayList<String>();
			List<String> list_name = new ArrayList<String>();
			List<String> list_price = new ArrayList<String>();
			List<String> list_monthsales = new ArrayList<String>();
			
			for(int i=0;i<array.length();i++){
				JSONObject object = array.getJSONObject(i);
				list_pic_address.add(object.getString("IMAGE"));
				list_name.add(object.getString("NAME"));
				list_price.add(object.getString("PRICE"));
				list_monthsales.add(object.getString("MONTHSALES"));
			}
			list.add(list_pic_address);
			list.add(list_name);
			list.add(list_price);
			list.add(list_monthsales);
		}else if(name.equals("selectWithID")){
			List<String> list_name = new ArrayList<String>();
			List<String> list_phone = new ArrayList<String>();
			List<String> list_id_card = new ArrayList<String>();
			System.out.println("selectWithID");
			for(int i=0;i<array.length();i++){
				JSONObject object = array.getJSONObject(i);
				list_name.add(object.getString("name"));
				list_phone.add(object.getString("phone"));
				list_id_card.add(object.getString("id_card"));
			}
			list.add(list_name);
			list.add(list_phone);
			list.add(list_id_card);
		}else if(name.equals("selectAddress")){
			List<String> list_address = new ArrayList<String>();
			System.out.println("selectAddress");
			for(int i=0;i<array.length();i++){
				JSONObject object = array.getJSONObject(i);
				list_address.add(object.getString("address"));
			}
			list.clear();
			list.add(list_address);
		}else if(name.equals("selectRecommend")){
			List<String> list_name = new ArrayList<String>();
			List<String> list_address = new ArrayList<String>();
			List<String> list_image = new ArrayList<String>();
			System.out.println("selectRecommend");
			for(int i=0;i<array.length();i++){
				JSONObject object = array.getJSONObject(i);
				list_name.add(object.getString("name"));
				list_address.add(object.getString("address"));
				list_image.add(object.getString("image"));
			}
			list.clear();
			list.add(list_name);
			list.add(list_address);
			list.add(list_image);
		}
		
		
		return list;
	}
	
	public static boolean postRequest(String url, Map<String, String> map,String type) throws Exception{
		HttpPost request = new HttpPost(url);
		List<NameValuePair> param = new ArrayList<NameValuePair>();  
		param.add(new BasicNameValuePair("type", type));
		if(type.equals("login")){
	        param.add(new BasicNameValuePair("userName", map.get("userName")));
	        param.add(new BasicNameValuePair("password", map.get("password")));
		}else if(type.equals("name")){
			param.add(new BasicNameValuePair("userName", map.get("userName")));
		}else if(type.equals("phone")){
			param.add(new BasicNameValuePair("userPhone", map.get("userPhone")));
		}else if(type.equals("register")){
			param.add(new BasicNameValuePair("userName", map.get("userName")));
			param.add(new BasicNameValuePair("password", map.get("password")));
			param.add(new BasicNameValuePair("userPhone", map.get("userPhone")));
			param.add(new BasicNameValuePair("userID", map.get("userID")));
		}else if(type.equals("type_restaurant")){
			param.add(new BasicNameValuePair("name", map.get("name")));
		}else if(type.equals("type_spot")){
			param.add(new BasicNameValuePair("name", map.get("name")));
		}
        request.setEntity(new UrlEncodedFormEntity(param, HTTP.UTF_8));
		HttpResponse httpResponse = new DefaultHttpClient().execute(request);
		String retStr = EntityUtils.toString(httpResponse.getEntity());
		System.out.println("return "+retStr);
		if(retStr.equals("true")){
			return true;
		}else{
			return false;
		}
		
	}
}
