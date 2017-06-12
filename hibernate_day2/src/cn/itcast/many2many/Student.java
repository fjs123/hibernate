package cn.itcast.many2many;

import java.util.HashSet;
import java.util.Set;

/**
 * 学生
 * @author Administrator
 *
 */
public class Student {
	private Integer id;
	private String sname;
	private Set<Course> courses = new HashSet<Course>();//一个学生可以对应多门课程
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getSname() {
		return sname;
	}
	public void setSname(String sname) {
		this.sname = sname;
	}
	public Set<Course> getCourses() {
		return courses;
	}
	public void setCourses(Set<Course> courses) {
		this.courses = courses;
	}
	
}
