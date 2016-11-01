package cn.zlb.goods.category.domin;

import java.util.List;

/**
 * 分类模块的实体类
 * @author Bingo
 *
 */
public class Category {
	private String cid;
	private String cname;
	private String desc;
	private Category parent;  //父分类
	private List<Category> child;//子分类
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	public String getCname() {
		return cname;
	}
	public void setCname(String cname) {
		this.cname = cname;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public Category getParent() {
		return parent;
	}
	public void setParent(Category parent) {
		this.parent = parent;
	}
	public List<Category> getChild() {
		return child;
	}
	public void setChild(List<Category> child) {
		this.child = child;
	}
	@Override
	public String toString() {
		return "Category [cid=" + cid + ", cname=" + cname + ", desc=" + desc
				+ ", parent=" + parent + ", child=" + child + "]";
	}
	
		
}
