
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.util.jar.JarOutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import net.sf.json.JSONObject;
import net.sf.json.util.JSONStringer;
import net.sf.json.util.JSONUtils;

/**
 * 
 */

/**
 * @author Administrator
 *
 */
public class orderdishes_servlet extends HttpServlet{
	PrintWriter out;
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
//		String type = req.getParameter("type");
		
		PrintWriter out = resp.getWriter();
		out.print(req.getParameter("type"));
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
//			String type = req.getParameter("type");
//			StringBuffer jb = new StringBuffer();
//			String line = null;
//			try{
//				BufferedReader reader = req.getReader();
//				while((line = reader.readLine()) != null){
//					jb.append(line);
//				}
//			}catch(Exception e){}
//			JSONObject jsonObject = JSONObject.fromObject(jb.toString());
//			
//			String type = jsonObject.getString("type");
//			String type = null;
			String type = req.getParameter("type");
//			PrintWriter out = resp.getWriter();
//			out.print(type);
			String restaurantName = "";
//			if(type == null){
//				type = req.getParameter("type");
//			}
//			PrintWriter out = resp.getWriter();
//			out.print("true");
			resp.setContentType("text/html;charset=UTF-8");
//			if(!type.equals("selectSpot")){
//				out = resp.getWriter();
//				out.print(type);
//			}
			try{
					Class.forName("org.gjt.mm.mysql.Driver").newInstance();
					Connection C = DriverManager.getConnection("jdbc:mysql://localhost:3306/data?useUnicode=true&characterEncoding=utf8","root","123456");
					Statement state = C.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
					if(type.equals("selectSpot")){
						ResultSet resultSet=state.executeQuery("select * from scenic");
						if(resultSet!=null){
							JSONStringer stringer = new JSONStringer();
							stringer.array();
							while(resultSet.next()){
								stringer.object().key("name").value(resultSet.getString("name")).key("pic_address").value(resultSet.getString("pic_address")).endObject();
							}
							stringer.endArray();
							resp.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
							resp.setContentType("text/json;charset=UTF-8");
						}else{
//							out.print("false");
						}
					}else if(type.equals("selectFood")){
						ResultSet resultSet = state.executeQuery("select * from restaurant");
						if(resultSet != null){
							JSONStringer stringer = new JSONStringer();
							stringer.array();
							while(resultSet.next()){
								stringer.object().key("name").value(resultSet.getString("name")).key("star").value(resultSet.getString("star"))
								.key("sales").value(resultSet.getString("sales")).key("send_up").value(resultSet.getString("send_up"))
								.key("costs").value(resultSet.getString("costs")).key("time").value(resultSet.getString("time"))
								.key("pic_address").value(resultSet.getString("pic_address")).key("is_business").value(resultSet.getString("is_business")).endObject();
							}
							stringer.endArray();
							resp.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
							resp.setContentType("text/json;charset=UTF-8");
						}
					}else if(type.equals("selectFoodEachRestaurant")){
						restaurantName = req.getParameter("restaurant_name");
//						resp.setContentType("text/html;charset=UTF-8");
//						out = resp.getWriter();
//						out.print(restaurantName);
//						ResultSet resultSet = state.executeQuery("select * from restaurant where name='小石灶酒家（百旺店）'");
						ResultSet resultSet = state.executeQuery("select * from restaurant where name='"+restaurantName+"'");
						if(resultSet != null){
							JSONStringer stringer = new JSONStringer();
							stringer.array();
							while(resultSet.next()){
								stringer.object().key("pic_address").value(resultSet.getString("pic_address")).key("name").value(resultSet.getString("name"))
								.key("cuisine").value(resultSet.getString("cuisine")).key("star").value(resultSet.getString("star"))
								.key("business_time").value(resultSet.getString("business_time")).key("is_business").value(resultSet.getString("is_business"))
								.key("address").value(resultSet.getString("address")).key("time").value(resultSet.getString("time")).key("send_up")
								.value(resultSet.getString("send_up")).endObject();
							}
							stringer.endArray();
//							resp.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
//							resp.setContentType("text/json;charset=UTF-8");
							PrintWriter out = resp.getWriter();
							out.print(stringer.toString());
							
						}
					}else if(type.equals("menuOfRestaurant")){
						restaurantName = req.getParameter("restaurant_name");
						ResultSet resultSet = state.executeQuery("select * from menu where RESNAME='"+restaurantName+"'");
						if(resultSet != null){
							JSONStringer stringer = new JSONStringer();
							stringer.array();
							while(resultSet.next()){
								stringer.object().key("NAME").value(resultSet.getString("NAME")).key("PRICE").value(resultSet.getString("PRICE"))
								.key("IMAGE").value(resultSet.getString("IMAGE")).key("MONTHSALES").value(resultSet.getString("MONTHSALES")).endObject();
							}
							stringer.endArray();
//							resp.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
//							resp.setContentType("text/json;charset=UTF-8");
							PrintWriter out = resp.getWriter();
							out.print(stringer.toString());
						}
					}else if(type.equals("login")){
						String userName = new String(req.getParameter("userName").getBytes("iso-8859-1"),"utf-8");
						String password = new String(req.getParameter("password").getBytes("iso-8859-1"),"utf-8");
						ResultSet resultSet = state.executeQuery("select * from user where name='"+userName+"' and password='"+password+"'");
						if(resultSet.next()){
							resp.getOutputStream().write("true".getBytes("UTF-8"));
						}else{
							resp.getOutputStream().write("false".getBytes("UTF-8"));
						}
					}else if(type.equals("name")){
						String userName = new String(req.getParameter("userName").getBytes("iso-8859-1"),"utf-8");
						ResultSet resultSet = state.executeQuery("select * from user where name='"+userName+"'");
						if(resultSet.next()){
							resp.getOutputStream().write("true".getBytes("UTF-8"));
						}else{
							resp.getOutputStream().write("false".getBytes("UTF-8"));
						}
					}else if(type.equals("phone")){
						String userPhone = new String(req.getParameter("userPhone").getBytes("iso-8859-1"),"utf-8");
						ResultSet resultSet = state.executeQuery("select * from user where phone='"+userPhone+"'");
						if(resultSet.next()){
							resp.getOutputStream().write("true".getBytes("UTF-8"));
						}else{
							resp.getOutputStream().write("false".getBytes("UTF-8"));
						}
					}else if(type.equals("register")){
						String userName = new String(req.getParameter("userName").getBytes("iso-8859-1"),"utf-8");
						String password = new String(req.getParameter("password").getBytes("iso-8859-1"),"utf-8");
						String userPhone = new String(req.getParameter("userPhone").getBytes("iso-8859-1"),"utf-8");
						String userID = new String(req.getParameter("userID").getBytes("iso-8859-1"),"utf-8");
						int status = state.executeUpdate("insert into user values ('"+userName+"','"+password+"','"+userPhone+"','"+userID+"');");
						if(status == 1){
							resp.getOutputStream().write("true".getBytes("UTF-8"));
						}else{
							resp.getOutputStream().write("false".getBytes("UTF-8"));
						}
					}else if(type.equals("selectWithID")){
						String userName = new String(req.getParameter("userName").getBytes("iso-8859-1"),"utf-8");
						ResultSet resultSet = state.executeQuery("select * from user where name='"+userName+"'");
						if(resultSet != null){
							JSONStringer stringer = new JSONStringer();
							stringer.array();
							while(resultSet.next()){
								stringer.object().key("name").value(resultSet.getString("name")).key("phone").value(resultSet.getString("phone"))
								.key("id_card").value(resultSet.getString("id_card")).endObject();
							}
							stringer.endArray();
							resp.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
							resp.setContentType("text/json;charset=UTF-8");
						}else{
							JSONStringer stringer = new JSONStringer();
							stringer.object().key("userName").value(userName+"userName");
							stringer.endArray();
							resp.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
							resp.setContentType("text/json;charset=UTF-8");
						}
					}else if(type.equals("selectAddress")){
						String userName = new String(req.getParameter("userName").getBytes("iso-8859-1"),"utf-8");
						ResultSet resultSet = state.executeQuery("select * from address where name='"+userName+"'");
						if(resultSet != null){
							JSONStringer stringer = new JSONStringer();
							stringer.array();
							while(resultSet.next()){
								stringer.object().key("address").value(resultSet.getString("address")).endObject();
							}
							stringer.endArray();
							resp.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
							resp.setContentType("text/json;charset=UTF-8");
						}else{
							JSONStringer stringer = new JSONStringer();
							stringer.object().key("userName").value(userName+"userName");
							stringer.endArray();
							resp.getOutputStream().write(stringer.toString().getBytes("UTF-8"));
							resp.setContentType("text/json;charset=UTF-8");
						}
					}else if(type.equals("selectRecommend")){
						ResultSet resultSet = state.executeQuery("select name,address,pic_address from restaurant" +
								" where is_recommend='true' union select name,address,pic_address from scenic where is_recommend='true'");
						JSONStringer stringer = new JSONStringer();						
						stringer.array();
						if(resultSet != null){
							while(resultSet.next()){
								stringer.object().key("name").value(resultSet.getString("name")).key("address")
								.value(resultSet.getString("address")).key("image").value(resultSet.getString("pic_address")).endObject();
							}
						}
						stringer.endArray();
						
						
						
//						resp.getOutputStream().write(JSON.toJSONString(stringer).getBytes("UTF-8"));
//						resp.setContentType("text/html;charset=UTF-8");
						PrintWriter out = resp.getWriter();
						out.print(stringer.toString());
						
					}else if(type.equals("type_restaurant")){
						String name = req.getParameter("name");
						ResultSet resultSet = state.executeQuery("select * from restaurant where name='"+name+"'");
						if(resultSet.next()){
							resp.getOutputStream().write("true".getBytes("UTF-8"));
						}else{
							resp.getOutputStream().write("false".getBytes("UTF-8"));
						}
					}else if(type.equals("type_spot")){
						String name = req.getParameter("name");
						ResultSet resultSet = state.executeQuery("select * from scenic where name='"+name+"'");
						if(resultSet.next()){
							resp.getOutputStream().write("true".getBytes("UTF-8"));
						}else{
							resp.getOutputStream().write("false".getBytes("UTF-8"));
						}
					}
				}catch(Exception e){
				}
			
			
//		}
	}
	

}
