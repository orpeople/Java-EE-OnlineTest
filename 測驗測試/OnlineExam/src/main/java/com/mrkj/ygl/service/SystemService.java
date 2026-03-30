package com.mrkj.ygl.service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.mrkj.ygl.Connection.DaoConnection;
import com.mrkj.ygl.entity.UserInfo;

/**
 * The beauty of the code
 * 
 * @author Administrator
 *
 *	1、systemService层处理系统层面的操作，如登录，注册，权限等等
 *
 *	2、这里我们使用的JDBC4.1标准，该标准，Connection、ResultSet和Statement都实现了Closeable借口，所有在try-with-resources语句中调用，就可以自动关闭资源。
 */
public class SystemService{

	public Map<String,Object> selectUserInfoByusername (String username){
		
		Map<String,Object> returnMap = new HashMap<String, Object>();//返回结果集
		
		try {
			DaoConnection dc = DaoConnection.initDaoConnection();
			PreparedStatement PreparedExec = dc.getPreparedExec(UserInfo.SELECTBYusername.toString(),username);
			ResultSet rs = PreparedExec.executeQuery();//数据库结果集
			while (rs.next()){
				returnMap.put("id", rs.getString(1));
				returnMap.put(UserInfo.username.toString(), rs.getString(2));
				returnMap.put(UserInfo.password.toString(), rs.getString(3));
				returnMap.put(UserInfo.fullname.toString(), rs.getString(4));
				returnMap.put(UserInfo.email.toString(), rs.getString(5));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return returnMap;
	}
	
	public int insert (String id,String username,String password,String fullname,String email){
		
		DaoConnection dc = DaoConnection.initDaoConnection();
		int resultInt = 0;
		try {
			PreparedStatement PreparedExec = dc.getPreparedExec(UserInfo.INSERT.toString(),id,username,password,fullname,email);
			resultInt = PreparedExec.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return resultInt;
	}
	/**
	 * 插入功能（ID为自动编号）
	 * @param username
	 * @param password
	 * @param fullname
	 * @param email
	 * @return
	 */
	public int insert (String username,String password,String fullname,String email){
		
		DaoConnection dc = DaoConnection.initDaoConnection();
		int resultInt = 0;
		try {
			PreparedStatement PreparedExec = dc.getPreparedExec(UserInfo.INSERT.toString(),username,password,fullname,email);
			resultInt = PreparedExec.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return resultInt;
	}
	
}
