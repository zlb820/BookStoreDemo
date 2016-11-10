package cn.zlb.goods.pager;
/**
 * ==========================所有分页查询通用的类====================================
 * 
 * 分页查询，结果bean 
 * 包含分页查询结果的信息，
 * 用PagerBean向客户端返回结果
 */
import java.util.List;

public class PagerBean<T> {
	private int pc;  //当前页码
	private int tr;  //总记录数
	private int ps;		//每页记录数
	private String url; //请求路径和参数
	private List<T>beanlist; //保存分页查询的结果bean
	//计算总页数
	public int getTp(){
		int tp=tr/ps;
		return tr%ps==0?tp:tp+1;//如果取余为0，就返回结果，不为0就加一
		
	}
	public int getPc() {
		return pc;
	}
	public void setPc(int pc) {
		this.pc = pc;
	}
	public int getTr() {
		return tr;
	}
	public void setTr(int tr) {
		this.tr = tr;
	}
	public int getPs() {
		return ps;
	}
	public void setPs(int ps) {
		this.ps = ps;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public List<T> getBeanlist() {
		return beanlist;
	}
	public void setBeanlist(List<T> beanlist) {
		this.beanlist = beanlist;
	}
	@Override
	public String toString() {
		return "Pager [pc=" + pc + ", tr=" + tr + ", ps=" + ps + ", url=" + url
				+ ", beanlist=" + beanlist + "]";
	}
	
	
}
