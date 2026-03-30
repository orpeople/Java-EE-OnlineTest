package com.mrkj.ygl.Connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * The beauty of the code
 * 
 * @author yuguoliang
 *
 *	单例模式，初始化与数据的连接
 */
public class DaoConnection {

	private static DaoConnection dc;
	
	private Connection connection;
	
	private static final String url = "jdbc:mysql://127.0.0.1:3306/db_mrks?useUnicode=true&characterEncoding=utf-8";//数据库连接路径
	private static final String username = "root";//数据库用户名
	private static final String pwd = "1234";//数据库密码
	private static final String driver = "com.mysql.cj.jdbc.Driver";//数据库驱动类
	
	//私有构造函数
	private DaoConnection (){}
	
	//匿名内部类加载驱动类
	{
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//初始化
	public static DaoConnection initDaoConnection (){
		if (dc == null){
			dc = new DaoConnection();
		}
		return dc;
	}

	/**
	 * @return 数据库连接
	 * @throws SQLException 
	 */
	public Connection getJDBCConnection() throws SQLException {
		connection = DriverManager.getConnection(url, username, pwd);
		return connection;
	}
	
	
	/**
	 * @param sql SQL语句
	 * @param arguments 参数集合
	 * @return
	 * @throws SQLException
	 */
	public PreparedStatement getPreparedExec (String sql,Object ...arguments) throws SQLException{
		
		DaoConnection dc = initDaoConnection();
		
		PreparedStatement ps = dc.getJDBCConnection().prepareStatement(sql);
		int i = 1;
		if (arguments!=null&arguments.length>0){
			for (Object arg:arguments){
				ps.setObject(i, arg);
				i++;
			}
		}
		return ps;
	}
	
}