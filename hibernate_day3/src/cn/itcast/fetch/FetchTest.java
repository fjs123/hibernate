package cn.itcast.fetch;

import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;

import cn.itcast.utils.HibernateUtils;

/**
 * 抓取策略
 * @author Administrator
 *
 */
public class FetchTest {
	
	//初始化数据
	 @Test
	 public void test1(){
		 Session session = HibernateUtils.getSession();
		 Transaction tx = session.beginTransaction();
		 
		 Customer c = new Customer();
		 c.setCname("刘备");
		 
		 session.save(c);
		 
		 for(int i=0;i<10;i++){
			 Order o = new Order();
			 o.setOrderNumber("000" + i);
			 o.setPrice(10d + i);
			 o.setCustomer(c);//订单和客户建立关联
			 session.save(o);
		 }
		 
		 tx.commit();
		 session.close();
	 }
	 
	 //区分立即检索和延迟检索
	 @Test
	 public void test2(){
		 Session session = HibernateUtils.getSession();
		 Transaction tx = session.beginTransaction();
		 
		 //Customer c = (Customer) session.get(Customer.class, 1);//立即查询数据库 
		 Customer c2 = (Customer) session.load(Customer.class, 1);//延迟检索 
		 //System.out.println(c2);
		 //System.out.println(c2.getId());//不会发出sql
		 //System.out.println(c2.getCname());//发出sql查询数据库
		 
		 Hibernate.initialize(c2);//发出sql查询数据库
		 
		 tx.commit();
		 session.close();
	 }
	 
	 	//关联级别检索 <set fetch="join" lazy="*">
		 @Test
		 public void test3(){
			 Session session = HibernateUtils.getSession();
			 Transaction tx = session.beginTransaction();
			 
			 Customer c = (Customer) session.get(Customer.class, 1);//立即查询数据库 
			 c.getOrders().size();
			 for(Order o:c.getOrders()){
				 o.getOrderNumber();
			 }
			 
			 tx.commit();
			 session.close();
		 }
		 
		//关联级别检索 <set fetch="join" lazy="*">
		 @Test
		 public void test4(){
			 Session session = HibernateUtils.getSession();
			 Transaction tx = session.beginTransaction();
			 
			 String hql = "FROM Customer WHERE 1=1";
			 Query query = session.createQuery(hql);
			 List<Customer> list = query.list();
			 /*for(Customer c : list){
				 c.getOrders().size();
				 for(Order o : c.getOrders()){
					 o.getOrderNumber();
				 }
			 }*/
			 
			 tx.commit();
			 session.close();
		 }
		//关联级别检索 <many-to-one fetch="join" lazy="*">
		 @Test
		 public void test5(){
			 Session session = HibernateUtils.getSession();
			 Transaction tx = session.beginTransaction();
			 
			 Order order = (Order) session.get(Order.class, 15);
			 
			 tx.commit();
			 session.close();
			 
			 //order.getCustomer().getCname();
		 }
		 
		//批量检索<set batch-size="">
		 @Test
		 public void test6(){
			 Session session = HibernateUtils.getSession();
			 Transaction tx = session.beginTransaction();
			 
			 Query query = session.createQuery("FROM Customer");
			 List<Customer> list = query.list();
			 
			 for(Customer c : list){
				 c.getOrders().size();
			 }
			 
			 tx.commit();
			 session.close();
		 }
		 
		//批量检索<class batch-size="">
		 @Test
		 public void test7(){
			 Session session = HibernateUtils.getSession();
			 Transaction tx = session.beginTransaction();
			 
			 String hql = "FROM Order";
			 Query query = session.createQuery(hql);
			 List<Order> list = query.list();
			 for(Order o:list){
				 o.getCustomer().getCname();
			 }
			 
			 tx.commit();
			 session.close();
		 }
}
