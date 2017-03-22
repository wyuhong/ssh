<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>left</title>
<base target="body" />
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<meta http-equiv="content-type" content="text/html;charset=utf-8">
<!-- 首先导入css文件 -->
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/ztree/zTreeStyle/zTreeStyle.css"
	type="text/css">
</head>

<body>
	<ul id="categoryTree" class="ztree"></ul>

	<!-- 导入js文件 -->
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/ztree/jquery-1.4.4.min.js"></script>
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/ztree/jquery.ztree.core.js"></script>
	<script type="text/javascript">
	

		var zTreeObj;
		// zTree 的参数配置，深入使用请参考 API 文档（setting 配置详解）
		var setting = {
				async: {
					enable: true,
					url: "/ssh_goods/listCategories",
					autoParam: ["cid"]
				},
		data: {
			simpleData: {
				enable: true,
				idKey: "cid",
				pIdKey: "pid",
				rootPId: null
			},
				key: {
					name: "cname"
				}
		}
		};
		// zTree 的数据属性，深入使用请参考 API 文档（zTreeNode 节点数据详解）
		var zNodes = [];
		
		$(function(){
			zTreeObj = $.fn.zTree.init($("#categoryTree"), setting, zNodes);
		});
	</script>
</body>
</html>
