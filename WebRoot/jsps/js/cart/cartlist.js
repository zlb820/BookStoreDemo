$(function(){
	//1.0计算总计
	showtotal();
	
	//2.0全选按钮点击事件
	$("#selectAll").click(function(){
		//获取全选按钮的状态，
		var status=$("#selectAll").attr("checked");
		console.log(status);
		//全选按钮的状态和单选按钮结算按钮同步
		selectALlCheckbox(status);
		
		setJieSuan(status);
		
		showtotal();
		
	});
	
	/*3.0给所有单选按钮添加click事件
	 * 1.当已选单选按钮数==0  取消选中 全选取消， 结算按钮失效，总价重新计算
	 * 2.当已选按钮数==等于按钮总数  全选选中 ，结算有效，总价计算
	 * 3.其他情况  即：已选按钮<等于按钮总数  全选取消，按钮有效，总价值计算
*/	
	$(":checkbox[name=checkboxBtn]").click(function(){
		//所有单选框数量
		var all=$(":checkbox[name=checkboxBtn]").length;
		console.log("all："+all);
	    var checkedbox=$(":checkbox[name=checkboxBtn][checked=true]").length;
	    console.log("checkedbox:"+checkedbox);
	    if (all==checkedbox) {
			$("#selectAll").attr("checked",true);
			setJieSuan(true);
		}else if(checkedbox==0){
			$("#selectAll").attr("checked",false);
			setJieSuan(false);
		}else {
			$("#selectAll").attr("checked",true);
			setJieSuan(true);
		}
	    showtotal();
	});
	
/*	*//**
	 *4.0 购物车条目  增加 减少数量的 click时间，发送异步请求
	 */
	 $(".jian").click(function(){
		 console.log("加减");
		//获取当前条目的id，购物车每个标签的id由 该商品的条目 组合拼接而成，
		 var cartItemId=$(this).attr("id").substring(0,32);
		 //减少的判断  如果数量小于1 ，那么调用删除方法
		 var quantity=$("#"+cartItemId+"Quantity").val();
		 console.log("quantity:" +quantity);
		 if (quantity==1) {
			if (confirm("删除该商品？")) {
				location="/goods/CartItemServlet?method=deleteCartItem&cartitems="+cartItemId;
			}
			 
		}else {
				//发送异步请求到服务器
			updateQuantity(cartItemId,quantity-1);
			}
	  
	 });
	 //5.0增加购物条目 请求方法
	 $(".jia").click(function(){
		 var cartItemId=$(this).attr("id").substring(0,32);
		 
		 var quantity=$("#"+cartItemId+"Quantity").val();
		 
		 updateQuantity(cartItemId,Number(quantity)+1);
		 
		 
	 }); 
	 

	 
});
/*计算总价格*/
function showtotal(){
	console.log("showtotal");
	var total=0;
	//获取已选中的单选框
	$(":checkbox[name=checkboxBtn][checked=true]").each(function(){
		//checkbox 的值为cartItemId
		var cartItemId=$(this).val();
		//通过cartItemId，组合小计的id ，获得该物品小计
		var sub =$("#"+cartItemId+"Subtotal").text();
		//计算总价,
		total += Number(sub);
	 
	}); 
	//把总价设置到页面
	$("#total").text(round(total, 2));
}
//------------------------------------------------------------------------------
/*全选按钮的设置
 *	1.选中全选按后所有单选框被选中，并且结算按钮可以点击并使用
 *  2.取消选中全选按钮，所有单选框取消选中，并且结算按钮不能点击并使用 
 **/
//选中所有单选框
function selectALlCheckbox(bool){
	$(":checkbox[name=checkboxBtn]").attr("checked",bool);
	}
//结算按钮样式设置
function setJieSuan(bool){
	if (bool) {
		$("#jiesuan").removeClass("kill").addClass("jiesuan");
		$("#jiesuan").unbind("click");
	}else{
		$("#jiesuan").removeClass("jiesuan").addClass("kill");
		$("#jiesuan").click(function(){return false;});
	}
	
}

//------------------------------------------------------------------------------ 
 /*批量删除 拼装所有已选checkbox的 id 发送到服务器*/
function cartitems(){
	var array=new Array();
	$(":checkbox[name=checkboxBtn][checked=true]").each(function(){
		array.push($(this).val());
		 
	});
	
	location="/goods/CartItemServlet?method=deleteCartItem&cartitems="+array;
	console.log(location);
}
 
 
//------------------------------------------------------------------------------
/**
 * 购物车条目  增加 减少数量的 click时间，发送异步请求
 */

 function updateQuantity(cartItemId,quantity){
	 
	 $.ajax({
		 async:false,
		 cache:false,
		 dataType:"json",
		 type:"POST",
		 url:"/goods/CartItemServlet",
		 data:{method:"modifyQuantity",cartItemId:cartItemId,quantity:quantity},
		 success:function(result){
			 //调用成功 重新设置标签值,设置数量
			 $("#"+cartItemId+"Quantity").val(result.quantity);
			 //设置小计
			 $("#"+cartItemId+"Subtotal").text(result.totalPrice);
			 //重新计算总计
			 showtotal();
		 }
	 		
		 
	 });
	  
 }
 
//------------------------------------------------------------------------------
 /**
  * 提交已选中的 items信息  ，结算功能
  */
 function jiesuan(){
	 //拼接id字符串
	 var cartItemIds=new Array();
	 $(":checkbox[name=checkboxBtn][checked=true]").each(function(){
		 cartItemIds.push($(this).val());
	 });
	 
	 //hidden表单设置id字符串
	 $("#cartItemIds").attr("value",cartItemIds.toString());
	 
	 
	 //hidden input 内容设置
	 var total=$("#total").text();
	 $("#totalPrice").attr("value",total);
	 
	 
	 //提交表单
	 $("#form1").submit();
	 
 }
 
 
 
 
 
 
 
 