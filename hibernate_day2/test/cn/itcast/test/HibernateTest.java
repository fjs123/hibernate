package cn.itcast.test;

import org.hibernate.FlushMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;

import cn.itcast.domain.User;
import cn.itcast.utils.HibernateUtils;

public class HibernateTest {

	/**
	 * 对象的状态
	 */
	@Test
	public void test1(){
		Session session = HibernateUtils.getSession();
		Transaction tx = session.beginTransaction();
		
		User user = new User();//瞬时对象
		user.setName("test");
		
		//session.save(user);//持久对象
		session.saveOrUpdate(user);//持久对象
		
		tx.commit();
		session.close();
	}
	
	/**
	 * 持久对象拥有自动更新数据库的能力
	 */
	@Test
	public void test2(){
		Session session = HibernateUtils.getSession();
		Transaction tx = session.beginTransaction();
		
		User user = (User) session.get(User.class, 1);//持久对象,被载入一级缓存
		
		user.setAddress("北京");//修改持久对象，修改缓存中的对象
		user.setName("张三");
		
		//2 当调用Query.list()方法时刷出缓存
		/*Query query = session.createQuery("FROM User");
		query.list();*/
		
		//3 当手动调用Session.flush()方法时刷出缓存
		session.flush();
		
		//1 当事务提交时刷出缓存
		tx.commit();
		session.close();
	}
	
	/**
	 * 测试一级缓存（session缓存）的存在,一级缓存的生命周期为session的生命周期
	 */
	@Test
	public void test3(){
		Session session = HibernateUtils.getSession();
		Transaction tx = session.beginTransaction();
		
		User user = (User) session.get(User.class, 1);//持久对象,被载入一级缓存
		User user2 = (User) session.get(User.class,1);//不会发出SQL查询数据库，直接从一级缓存中获取
		System.out.println(user == user2);
		tx.commit();
		session.close();//session关闭，缓存消失
		
		Session session2 = HibernateUtils.getSession();
		User user3 = (User) session2.get(User.class,1);//发出SQL查询数据库，缓存中的数据已经消失
		session2.close();
	}
	
	/**
	 * session操作缓存的方法
	 */
	@Test
	public void test4(){
		Session session = HibernateUtils.getSession();
		Transaction tx = session.beginTransaction();
		
		User user1 = (User) session.get(User.class, 1);//持久对象,被载入一级缓存
		User user2 = (User) session.get(User.class,1);//不会发出SQL查询数据库，直接从一级缓存中获取
		
		//session.clear();//清理所有一级缓存中的引用
		session.evict(user1);//清理指定对象,user1状态为托管
		
		//user1.setAddress(address)
		
		//会发出SQL查询数据库，一级缓存中的数据已经清理掉
		User user3 = (User) session.get(User.class, 1);//持久对象
		user3.setAge(50);//修改持久对象
		
		session.refresh(user3);//使用数据库中的数据覆盖缓存中的数据
		tx.commit();
		session.close();//session关闭，缓存消失
	}
	
	/**
	 * 刷出模式
	 */
	@Test
	public void test5(){
		Session session = HibernateUtils.getSession();
		//session.setFlushMode(FlushMode.MANUAL);
		Transaction tx = session.beginTransaction();
		
		User user = (User) session.get(User.class, 1);//持久对象,被载入一级缓存
		user.setAddress("abc");
		
		user.setId(10);
		
		session.flush();
		tx.commit();
		session.close();//session关闭，缓存消失
	}
	
	/**
	 * update
	 */
	@Test
	public void test6(){
		Session session = HibernateUtils.getSession();
		Transaction tx = session.beginTransaction();
		
		User user = (User) session.get(User.class, 1);//持久对象,被载入一级缓存
		
		session.clear();//user对象变为托管对象
		
		session.update(user);//user对象变为持久对象
		user.setAddress("sss");//修改持久对象
		
		tx.commit();
		session.close();//session关闭，缓存消失
	}
	
	/**
	 * select-before-update
	 */
	@Test
	public void test7(){
		Session session = HibernateUtils.getSession();
		Transaction tx = session.beginTransaction();
		
		User user = (User) session.get(User.class, 1);//持久对象,被载入一级缓存
		
		session.clear();//user对象变为托管对象
		
		user.setAddress("ddd");//修改的是托管对象
		session.update(user);//user对象变为持久对象
		
		tx.commit();
		session.close();//session关闭，缓存消失
	}
	
	/**
	 * 当 update() 方法关联一个脱管对象时, 如果在 Session 的缓存中已经存在相同 OID 的持久对象, 会抛出异常
	 */
	@Test
	public void test8(){
		Session session = HibernateUtils.getSession();
		Transaction tx = session.beginTransaction();
		
		User user1 = (User) session.get(User.class, 1);//持久对象,被载入一级缓存
		
		User user2 = new User();//瞬时
		user2.setId(1);//托管
		
		session.update(user2);//user2变为持久对象
		
		tx.commit();
		session.close();//session关闭，缓存消失
	}
	
	/**
	 * 当update()方法关联一个脱管对象时,如果在数据库中不存在相应的记录,也会抛出异常
	 */
	@Test
	public void test9(){
		Session session = HibernateUtils.getSession();
		Transaction tx = session.beginTransaction();
		
		User user1 = new User();//瞬时
		user1.setId(10);//托管
		
		session.update(user1);//user2变为持久对象
		
		tx.commit();
		session.close();//session关闭，缓存消失
	}
	
	/**
	 * saveOrUpdate
	 */
	@Test
	public void test10(){
		Session session = HibernateUtils.getSession();
		Transaction tx = session.beginTransaction();
		
		/*User user1 = new User();//瞬时
		session.saveOrUpdate(user1);//save
		 */	
		
		/*User user2 = new User();
		user2.setId(3);//托管对象
		session.saveOrUpdate(user2);//update
		 */		
		
		User user3 = (User) session.get(User.class, 3);//持久对象
		user3.setAddress("abc");// 修改持久对象
		session.saveOrUpdate(user3);
				
		
		
		tx.commit();
		session.close();//session关闭，缓存消失
	}
	/**
	 * get/load
	 */
	@Test
	public void test11(){
		Session session = HibernateUtils.getSession();
		Transaction tx = session.beginTransaction();
		
		User user = (User) session.load(User.class,1000);
		System.out.println(user);
		
		tx.commit();
		session.close();//session关闭，缓存消失
	}
	
	/**
	 * delete
	 */
	@Test
	public void test12(){
		Session session = HibernateUtils.getSession();
		Transaction tx = session.beginTransaction();
		
		User user = (User) session.load(User.class,3);//持久对象
		System.out.println(user);
		session.delete(user);//删除持久对象
		
		User user2 = new User();
		user2.setId(2);//托管
		session.delete(user2);//删除托管对象
		
		tx.commit();
		session.close();//session关闭，缓存消失
	}
}
