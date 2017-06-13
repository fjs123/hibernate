package cn.itcast.fetch;

import java.util.HashSet;
import java.util.Set;

/**
 * 瀹㈡埛
 * @author Administrator
 *
 */
public class Customer {
	private Integer id;
	private String cname;
	private String description;
	private Set<Order> orders = new HashSet<Order>();//涓�釜瀹㈡埛瀵瑰簲澶氫釜璁㈠崟
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Set<Order> getOrders() {
		return orders;
	}
	public void setOrders(Set<Order> orders) {
		this.orders = orders;
	}
	@Override
	public String toString() {
		return "Customer [id=" + id + ", cname=" + cname + ", description="
				+ description;
	}
	
	
}
