package cn.itcast.utils;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtils {
	private static Configuration conf ;
	// 通过配置对象构建一个会话工厂对象
	public static SessionFactory factory ;
	
	static{
		conf = new Configuration().configure();
		factory = conf.buildSessionFactory();
	}
	
	public static Session getSession(){
		// 从工厂中获取一个会话对象
		Session session = factory.openSession();
		return session;
	}
	
	public static void main(String[] args) {
		getSession();
	}
}
