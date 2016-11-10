package cn.zlb.goods.order.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.itcast.commons.CommonUtils;
import cn.zlb.goods.book.domin.Book;
import cn.zlb.goods.order.domin.OrderItem;
/**
 * 
 * 把OrderItem查询出
 * 映射其中OrderItem 和 Book相关字段
 * 添加Book到OrderItem
 * @author Bingo
 *
 */
public class OrderItemTools {

	public static OrderItem toOrderItem(Map<String , Object> map){
		OrderItem orderitem=CommonUtils.toBean(map, OrderItem.class);
		Book book=CommonUtils.toBean(map, Book.class);
		orderitem.setBook(book);
	 	
		return  orderitem;
	}
	
	public static List<OrderItem> toOrderItemList(List<Map<String, Object>> mapList){
		List<OrderItem> orderItemList=new ArrayList<OrderItem>();
		
		for (Map<String, Object> map : mapList) {
			orderItemList.add(toOrderItem(map));
		}
		
		return orderItemList;
	}
}
