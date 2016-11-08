$(function(){
	//计算总计
	showtotal();
	
	//全选按钮点击事件
	$("#selectAll").click(function(){
		//获取全选按钮的状态，
		var status=$("#selectAll").attr("checked");
		console.log(status);
		//全选按钮的状态和单选按钮结算按钮同步
		selectALlCheckbox(status);
		
		setJieSuan(status);
		
		showtotal();
		
	});
	
	/*给所有单选按钮添加click事件
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
 
 
 
 
 
 
 
 
 
 
 