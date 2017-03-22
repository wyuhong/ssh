<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript">
	function _go() {
		var pageNumber = $("#pageCode").val();//获取文本框中的当前页码
		if(!/^[1-9]\d*$/.test(pageNumber)) {//对当前页码进行整数校验
			alert('请输入正确的页码！');
			return;
		}
		if(pageNumber > "${pb.totalPages}") {//判断当前页码是否大于最大页
			alert('请输入正确的页码！');
			return;
		}
		location = "${pb.url}&pageNumber="+pageNumber;
	}
</script>


<div class="divBody">
  <div class="divContent">
    <%--上一页 --%>
		<%--上一页 --%>

	<c:choose>
			<c:when test="${pb.pageNumber>1 }">
			<a href="${pb.url }&pageNumber=${pageNumber-1}" class="aBtn bold">上一页</a>
				
			</c:when>
			<c:otherwise>
				<span class="spanBtnDisabled">上一页</span>
			</c:otherwise>
		</c:choose>

<%-- 计算begin和end --%>
   
    <c:choose>
      <%-- 如果总页数<=6，那么显示所有页码，即begin=1 end=${pb.tp} --%>
      <c:when test="${pb.totalPages <= 6 }">
        <c:set var="begin" value="1"/>
        <c:set var="end" value="${pb.totalPages}"/>
      </c:when>
      <c:otherwise>
        <%-- 设置begin=当前页码-2，end=当前页码+3 --%>
        <c:set var="begin" value="${pb.pageNumber-2 }"/>
        <c:set var="end" value="${pb.pageNumber+3 }"/>
        <c:choose>
          <%-- 如果begin<1，那么让begin=1 end=6 --%>
          <c:when test="${begin<1}">
            <c:set var="begin" value="1"/>
            <c:set var="end" value="6"/>
          </c:when>
          <%-- 如果end>最大页，那么begin=最大页-5 end=最大页 --%>
          <c:when test="${end>pb.totalPages}">
            <c:set var="begin" value="${pb.totalPages-5 }"/>
            <c:set var="end" value="${pb.totalPages}"/>
          </c:when>
        </c:choose>
      </c:otherwise>
    </c:choose>
    
    <%-- 当前页的页码有橙色的样式，非当前页的页码上有跳转的超链接 --%>
    <%-- 显示页码列表 --%>
    <c:forEach var="pc" begin="${begin }" end="${end }">
      <c:choose>
        <c:when test="${pb.pageNumber eq pc }">
          <span class="spanBtnSelect">${pc}</span>
        </c:when>
        <c:otherwise>
          <a href="${pb.url }&pageNumber=${pc}" class="aBtn">${pc}</a>
        </c:otherwise>
      </c:choose>
    </c:forEach>
    
    <%-- 显示点点点 --%>
    <c:if test="${end <pb.totalPages }">
      <span class="spanApostrophe">...</span> 
    </c:if>
		
		<%--下一页 --%>
		<c:choose>
			<c:when test="${pb.pageNumber eq pb.totalPages }">
				<span class="spanBtnDisabled">下一页</span>
			</c:when>
			<c:otherwise>
				<a href="${pb.url }&pageNumber=${pb.pageNumber+1}" class="aBtn bold">下一页</a>
			</c:otherwise>
		</c:choose>
		<span>共${pb.totalPages}页</span>
    <span>到</span>
    <input type="text" class="inputPageCode" id="pageCode" value="${pb.pageNumber}"/>
    <span>页</span>
    <a href="javascript:_go();" class="aSubmit">确定</a>

  </div>
</div>