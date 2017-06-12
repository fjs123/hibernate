package cn.itcast.many2many;

import java.util.HashSet;
import java.util.Set;

/**
 * 课程
 * @author Administrator
 *
 */
public class Course {
	private Integer id;
	private String cname;
	private Set<Student> students = new HashSet<Student>();//一门课程对应多个学生
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCname() {
		return cname;
	}
	public void setCname(String cname) {
		this.cname = cname;
	}
	public Set<Student> getStudents() {
		return students;
	}
	public void setStudents(Set<Student> students) {
		this.students = students;
	}
	
}
