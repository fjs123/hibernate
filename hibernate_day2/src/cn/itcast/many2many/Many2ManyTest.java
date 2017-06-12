package cn.itcast.many2many;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;

import cn.itcast.utils.HibernateUtils;

/**
 * 测试多对多
 * @author Administrator
 *
 */
public class Many2ManyTest {
	/**
	 * 保存操作
	 */
	@Test
	public void test1(){
		Session session = HibernateUtils.getSession();
		Transaction tx = session.beginTransaction();
		
		Student s = new Student();//瞬时
		s.setSname("张飞");
		
		Course c = new Course();//瞬时
		c.setCname("语文");
		
		s.getCourses().add(c);//学生和课程建立关系1 1
		//c.getStudents().add(s);//课程和学生建立关系1 1
		
		session.save(s);//持久对象1 1
		session.save(c);
		
		tx.commit();
		session.close();
	}
	
	/**
	 * 解除1号学生和1号课程的关联关系
	 */
	@Test
	public void test2(){
		Session session = HibernateUtils.getSession();
		Transaction tx = session.beginTransaction();
		
		Student s = (Student) session.get(Student.class, 1);
		Course c = (Course) session.get(Course.class, 1);
		
		s.getCourses().remove(c);//解除学生和课程的关系,操作关系表
		
		tx.commit();
		session.close();
	}
	
	/**
	 * 改变1号学生和1号课程的关联关系,改为1号学生和3号课程
	 */
	@Test
	public void test3(){
		Session session = HibernateUtils.getSession();
		Transaction tx = session.beginTransaction();
		
		Student s = (Student) session.get(Student.class, 1);
		Course c1 = (Course) session.get(Course.class, 1);
		Course c2 = (Course) session.get(Course.class, 3);
		s.getCourses().remove(c1);//解除学生和课程的关系,操作关系表
		s.getCourses().add(c2);//建立学生和课程的关系
		
		tx.commit();
		session.close();
	}
	
	/**
	 * 删除学生,首先从关系表中将此学生的选课记录删除，再删除学生记录
	 */
	@Test
	public void test4(){
		Session session = HibernateUtils.getSession();
		Transaction tx = session.beginTransaction();
		
		Student s = (Student) session.get(Student.class, 1);
		session.delete(s);
		
		tx.commit();
		session.close();
	}
}
