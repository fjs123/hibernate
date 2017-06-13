package cn.itcast.test;

import java.util.List;

import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;

import cn.itcast.domain.User;
import cn.itcast.fetch.Customer;
import cn.itcast.fetch.Order;
import cn.itcast.utils.HibernateUtils;

public class HibernateTest {
	// 使用悲观锁解决丢失更新
	@Test
	public void test1() {
		Session session = HibernateUtils.getSession();
		Transaction tx = session.beginTransaction();

		// 持久对象
		Customer c = (Customer) session
				.get(Customer.class, 1, LockMode.UPGRADE);
		c.setCname("abc");

		tx.commit();
		session.close();
	}

	// 使用悲观锁解决丢失更新
	@Test
	public void test2() {
		Session session = HibernateUtils.getSession();
		Transaction tx = session.beginTransaction();

		// 持久对象
		@SuppressWarnings("deprecation")
		Customer c = (Customer) session
				.get(Customer.class, 1, LockMode.UPGRADE);
		c.setDescription("ccc");

		tx.commit();
		session.close();
	}

	// 使用乐观锁解决丢失更新
	@Test
	public void test3() {
		Session session = HibernateUtils.getSession();
		Transaction tx = session.beginTransaction();

		User user = (User) session.get(User.class, 1);
		user.setName("tedst");

		tx.commit();
		session.close();
	}

	// 使用乐观锁解决丢失更新
	@Test
	public void test4() {
		Session session = HibernateUtils.getSession();
		Transaction tx = session.beginTransaction();

		User user = (User) session.get(User.class, 1);
		user.setAge(50);

		tx.commit();
		session.close();
	}

	// 二级缓存
	@Test
	public void test5() {
		Session session = HibernateUtils.getSession();
		Transaction tx = session.beginTransaction();

		Query query = session.createQuery("FROM Customer");
		List<Customer> list = query.list();// 将查询出的数据放入一级缓存和二级缓存

		tx.commit();
		session.close();// 一级缓存消失

		session = HibernateUtils.getSession();
		// 直接从二级缓存中获取数据,二级缓存缓存的是散装数据，又放入了一级缓存
		Customer c = (Customer) session.get(Customer.class, 2);
		Customer c1 = (Customer) session.get(Customer.class, 2);// 从一级缓存中获取
		System.out.println(c == c1);
	}

	// 测试类级别的二级缓存只适用于get和load获取数据，
	// query接口可以将数据放置到类级别的二级缓存中，但是不能使用query接口的 list方法从缓存中获取数据
	@Test
	public void test6() {
		Session session = HibernateUtils.getSession();
		Transaction tx = session.beginTransaction();

		Query query = session.createQuery("FROM Customer");
		List<Customer> list = query.list();// 将查询出的数据放入一级缓存和二级缓存

		tx.commit();
		session.close();// 一级缓存消失

		session = HibernateUtils.getSession();
		query = session.createQuery("FROM Customer");
		// 会发出sql，虽然二级缓存中存在数据，list方法不使用二级缓存中的数据
		List list2 = query.list();
	}

	// 集合级别的二级缓存
	@Test
	public void test7() {
		Session session = HibernateUtils.getSession();
		Transaction tx = session.beginTransaction();

		Query query = session.createQuery("FROM Customer");
		List<Customer> list = query.list();// 将查询出的数据放入一级缓存和二级缓存

		tx.commit();
		session.close();// 一级缓存消失

		session = HibernateUtils.getSession();

		// 不会发出sql，直接从集合级别的二级缓存中获取
		Order order = (Order) session.get(Order.class, 15);
		System.out.println(order);
	}

	// 测试一级缓存更新数据会同步到二级缓存
	@Test
	public void test8() {
		Session session = HibernateUtils.getSession();
		Transaction tx = session.beginTransaction();

		// 将查询出的对象放入一级缓存和二级缓存
		Customer c = (Customer) session.get(Customer.class, 1);
		c.setCname("zhangfei");

		tx.commit();
		session.close();// 一级缓存消失

		session = HibernateUtils.getSession();

		// 不会发出sql，直接从集合级别的二级缓存中获取
		Customer c2 = (Customer) session.get(Customer.class, 1);
		System.out.println(c2.getCname());
	}

	// 测试更新时间戳
	@Test
	public void test9() {
		Session session = HibernateUtils.getSession();
		Transaction tx = session.beginTransaction();

		// 将查询出的对象放入一级缓存和二级缓存
		Customer c = (Customer) session.get(Customer.class, 1);
		String hql = "update Customer set cname='itcast' where id = 1";
		Query query = session.createQuery(hql);
		query.executeUpdate();

		tx.commit();
		session.close();// 一级缓存消失

		session = HibernateUtils.getSession();

		// 会发出sql，虽然二级缓存中存在该对象，但是数据已经过期
		Customer c2 = (Customer) session.get(Customer.class, 1);
		System.out.println(c2.getCname());
	}

	// 查询缓存
	@Test
	public void test10() {
		Session session = HibernateUtils.getSession();
		Transaction tx = session.beginTransaction();

		Query query = session.createQuery("FROM Customer");
		
		query.setCacheable(true);//指定将查询出的数据放入查询缓存
		query.list();//发出sql
		tx.commit();
		session.close();// 一级缓存消失

		session = HibernateUtils.getSession();

		// 会发出sql，虽然二级缓存中存在该对象，但是数据已经过期
		query = session.createQuery("FROM Customer");
		query.setCacheable(true);//指定可以从查询缓存中获取数据
		query.list();//不会发出sql，直接从查询缓存中获取
	}
}
