package cn.itcast.one2many;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;

import cn.itcast.utils.HibernateUtils;

/**
 * 测试一对多操作
 * @author Administrator
 *
 */
public class One2ManyTest {
	/**
	 * 保存操作(cascade:级联操作save-update)
	 */
	@Test
	public void test1(){
		Session session = HibernateUtils.getSession();
		Transaction tx = session.beginTransaction();
		
		Customer customer = new Customer();//瞬时
		customer.setCname("zhangsan");
		
		Order order = new Order();//瞬时
		order.setOrderNumber("0001");
		order.setPrice(200d);
		
		customer.getOrders().add(order);//客户和订单建立关联
		//order.setCustomer(customer);//订单和客户建立关联
		
		session.save(customer);//持久对象
		//session.save(order);
		
		tx.commit();
		session.close();
	}
	
	/**
	 * 级联删除:cascade="delete"
	 */
	@Test
	public void test2(){
		Session session = HibernateUtils.getSession();
		Transaction tx = session.beginTransaction();
		
		Customer customer = (Customer) session.get(Customer.class, 5);
		session.delete(customer);
		
		tx.commit();
		session.close();
	}
	
	/**
	 * 从查询的客户对象中移除订单对象，订单对象是否删除
	 */
	@Test
	public void test3(){
		Session session = HibernateUtils.getSession();
		Transaction tx = session.beginTransaction();
		
		Customer customer = (Customer) session.get(Customer.class, 2);//持久对象
		
		Order order = (Order) session.get(Order.class, 2);
		customer.getOrders().remove(order);//从订单集合中移除订单，修改持久对象
		
		tx.commit();
		session.close();
	}
	/**
	 * 双向维护 --- 多余的SQL
	 */
	@Test
	public void test4(){
		Session session = HibernateUtils.getSession();
		Transaction tx = session.beginTransaction();
		
		Customer customer = (Customer) session.get(Customer.class, 2);//持久对象
		Order order = (Order) session.get(Order.class, 1);//持久对象
		
		customer.getOrders().add(order);//客户和订单建立关系
		order.setCustomer(customer);//订单和客户建立关系
		
		tx.commit();
		session.close();
	}
}
