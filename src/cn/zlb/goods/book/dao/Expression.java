package cn.zlb.goods.book.dao;
/**
 * 拼装通用sql的表达式类，包含sql语句中的各种符号，
 *和参数信息
 * @author Bingo
 *
 */
public class Expression {
	private String name;  //拼装sql时的 sql字段名
	private String operator; // 拼装 sql时 sql的操作符 < > = ? 这些操作符
	private String value; // 拼装sql ，查询的值
	// 加上有参数的构造函数
	public Expression(String name, String operator, String value) {
		super();
		this.name = name;
		this.operator = operator;
		this.value = value;
	}
	@Override
	public String toString() {
		return "Expression [name=" + name + ", operator=" + operator
				+ ", value=" + value + "]";
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public Expression() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
