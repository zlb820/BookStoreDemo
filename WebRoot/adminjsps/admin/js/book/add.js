/**
 * 添加图书页面，一级目录的二级目录的 联动显示js
 */

$(function(){
	
	$("#pid").change(function(){
	
		loadChild();
	});
	});

function loadChild(){
	console.log("loadChild");
	//获取pid
	var pid=$("#pid").val();
	//异步请求
	$.ajax({
		asyns:false,
		cache:false,
		dataType:"json",
		type:"POST",
		url:"/goods/admin/AdminAddBookServlet",
		data:{method:"checkChild",pid:pid},
		success:function(result){
			//每加载一个一级目录的二级目录，需要先清空先前所有的内容 
			$("#cid").empty();
			//添加头信息
			var text=$("<option></option>").text("=====请选择2级分类=====");
			$("#cid").append(text);
			//循环遍历添加所有二级分类
			for(var i=0;i<result.length;i++){
				var opt=$("<option></option>").val(result[i].cid).text(result[i].cname);
				$("#cid").append(opt);
				
			}
		}
		
		
	});
	
}
 