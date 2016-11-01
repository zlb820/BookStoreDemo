<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript">
	function _go() {
		var pc = $("#pageCode").val();//获取文本框中的当前页码
		if(!/^[1-9]\d*$/.test(pc)) {//对当前页码进行整数校验
			alert('请输入正确的页码！');
			return;
		}
		if(pc > 10) {//判断当前页码是否大于最大页
			alert('请输入正确的页码！');
			return;
		}
		location = "";
	}
</script>


<div class="divBody">
  <div class="divContent">
  <%-- <c:set var="pb" value="${pagerbean }"></c:set> --%>
    <%--上一页 --%>
	<c:choose>
	<%-- 如果当前页是第一页 那么设置不可点击   --%>
		<c:when test="${pagerbean.pc eq 1}"><span class="spanBtnDisabled">上一页</span></c:when>
		<%-- 反之，设置请求url--%>  
		<c:otherwise><a href="${pagerbean.url}&pc=${pagerbean.pc-1}" class="aBtn bold">上一页</a></c:otherwise>
	</c:choose>
      
        

    
    <%-- 计算begin和end --%>
      <%-- 如果总页数<=6，那么显示所有页码，即begin=1 end=${pb.tp} --%>
        <%-- 设置begin=当前页码-2，end=当前页码+3 --%>
          <%-- 如果begin<1，那么让begin=1 end=6 --%>
          <%-- 如果end>最大页，那么begin=最大页-5 end=最大页 --%>
	 	
		<c:choose>
			<c:when test="${pagerbean.tp le 6}">
				<c:set var="begin" value="1"></c:set>
				<c:set var="end" value="${pagerbean.tp}"></c:set>
			</c:when>
			<c:otherwise>
				<c:set var="begin" value="${pagerbean.pc-2 }"></c:set>
				<c:set var="end" value="${pagerbean.pc+3 }"></c:set>
				
				<c:if test="${begin lt 1 }">
					<c:set var="begin" value="1"></c:set>
					<c:set var="end" value="6"></c:set>
				</c:if>
				
				<c:if test="${end gt pagerbean.tp}">
					<c:set var="begin" value="${pagerbean.tp-5 }"></c:set>
					<c:set var="end" value="${pagerbean.tp }"></c:set>
				</c:if>
			</c:otherwise>
		</c:choose>
	 
	 <%-- 循环迭代页码 --%>
	 <c:forEach var="i" begin="${begin }" end="${end }">
	 	<%--判断是否为当前页,是当前页 则不能点击 --%>
	 	<c:choose>
	 		<c:when test="${pagerbean.pc eq i }">
	 		 	 <span class="spanBtnSelect">${i}</span>
	 		</c:when>
	 		<c:otherwise>
	 		     <%--需要添加uri --%>
	 			 <a href="${pagerbean.url }&pc=${i}" class="aBtn">${i}</a>
	 		</c:otherwise>
	 	</c:choose>
	 	
	 </c:forEach>
    
    <%-- 显示页码列表
          <span class="spanBtnSelect">1</span>
          <a href="" class="aBtn">2</a>
          <a href="" class="aBtn">3</a>
          <a href="" class="aBtn">4</a>
          <a href="" class="aBtn">5</a>
          <a href="" class="aBtn">6</a> --%>

    
    <%-- 显示点点点 --%>
     	<c:if test="${end lt pagerbean.tp }">
    		<span class="spanApostrophe">...</span> 
     	
     	</c:if>
    		 
     

     <%--下一页的设置  判断页码 --%>
     <%--下一页 --%>
     <c:choose>
     	<c:when test="${pagerbean.pc eq pagerbean.tp}"><span class="spanBtnDisabled">下一页</span></c:when>
     	<c:otherwise><a href="${pagerbean.url}&pc=${pagerbean.pc+1}" class="aBtn bold">下一页</a> </c:otherwise>
     </c:choose>
        
        
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    
    <%-- 共N页 到M页 --%>
    <span>共${pagerbean.tp }页</span>
    <span>到</span>
    <input type="text" class="inputPageCode" id="pageCode" value="${pagerbean.pc}"/>
    <span>页</span>
    <a href="javascript:_go();" class="aSubmit">确定</a>
  </div>
</div>