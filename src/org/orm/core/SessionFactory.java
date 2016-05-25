package org.orm.core;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class SessionFactory {
	
	private static Properties properties=new Properties();

	static{
		InputStream is =SessionFactory.class.getClassLoader().getResourceAsStream("config.properties");
		if(is==null){
			//获取默认的配置文件
			is= SessionFactory.class.getResourceAsStream("config.properties");
		}
		try {
			properties.load(is);
		} catch (IOException e) {
			throw new ExceptionInInitializerError("初始化配置文件失败");
		}
	}
	
	public static Session openSession() throws Exception{
		String driver=properties.getProperty("jdbc.driver");
		String url=properties.getProperty("jdbc.url");
		String user=properties.getProperty("jdbc.userName");
		String password=properties.getProperty("jdbc.password");
		if(driver!=null&&!"".equals(driver)){
			Class.forName(driver);
			Connection connection = DriverManager.getConnection(url, user, password);

			Class<?> clazz=Session.class;
			Constructor<?> constructor = clazz.getDeclaredConstructor(Connection.class);
			constructor.setAccessible(true);
			return (Session) constructor.newInstance(connection);
		}else{
			throw new Exception("you must suite the database driver");
		}
	}
}
