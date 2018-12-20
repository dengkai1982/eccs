<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/share.jsp"%>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no"/>
<meta name="renderer" content="webkit">
<link rel="icon" href="${contextPath}/img/favicon.ico" type="image/x-icon"/>
<link rel="shortcut icon" href="${contextPath}/img/favicon.ico" type="image/x-icon"/>
<title>${system_name}</title>
<!-- zui -->
<link href="${contextPath}/zui/css/zui.min.css" rel="stylesheet">
<link href="${contextPath}/zui/lib/datatable/zui.datatable.css" rel="stylesheet">
<link href="${contextPath}/zui/lib/chosen/chosen.css" rel="stylesheet">
<link href="${contextPath}/zui/lib/chosenicons/zui.chosenicons.css" rel="stylesheet">
<link href="${contextPath}/zui/lib/datetimepicker/datetimepicker.css" rel="stylesheet">
<link href="${contextPath}/zui/lib/uploader/zui.uploader.min.css" rel="stylesheet">
<link href="${contextPath}/zui/lib/bootbox/bootbox.css" rel="stylesheet">
<link href="${contextPath}/zui/lib/dashboard/zui.dashboard.min.css" rel="stylesheet">
<link href="${contextPath}/css/commons.css" rel="stylesheet">
<link href="${contextPath}/zui/css/zui-theme-indigo.css" rel="stylesheet" id="zui_theme">
<link href="${contextPath}/css/frame.css" rel="stylesheet">
<link href="${contextPath}/css/theme-indigo.css" rel="stylesheet" id="frame_zheme">
<link href="${contextPath}/zui/lib/datagrid/zui.datagrid.min.css" rel="stylesheet">
<!-- jQuery (ZUI中的Javascript组件依赖于jQuery) -->
<script src="${contextPath}/js/jquery-3.3.1.min.js" charset="utf-8" type="text/javascript"></script>
<!-- ZUI Javascript组件 -->
<script src="${contextPath}/zui/js/zui.min.js" charset="utf-8" type="text/javascript"></script>
<script src="${contextPath}/js/jquery.blockUI.js" charset="utf-8" type="text/javascript"></script>
<script src="${contextPath}/zui/lib/datatable/zui.datatable.js" charset="utf-8" type="text/javascript"></script>
<script src="${contextPath}/zui/lib/chosen/chosen.js" type="text/javascript" charset="utf-8"></script>
<script src="${contextPath}/zui/lib/chosenicons/zui.chosenicons.js" charset="utf-8" type="text/javascript"></script>
<script src="${contextPath}/zui/lib/datetimepicker/datetimepicker.js" charset="utf-8" type="text/javascript"></script>
<script src="${contextPath}/zui/lib/uploader/zui.uploader.min.js" charset="utf-8" type="text/javascript"></script>
<script src="${contextPath}/zui/lib/bootbox/bootbox.js" charset="utf-8" type="text/javascript"></script>
<script src="${contextPath}/zui/lib/dashboard/zui.dashboard.min.js" charset="utf-8" type="text/javascript"></script>
<script src="${contextPath}/zui/lib/sortable/zui.sortable.min.js" charset="utf-8" type="text/javascript"></script>
<script src="${contextPath}/zui/lib/datagrid/zui.datagrid.min.js" charset="utf-8" type="text/javascript"></script>
<!--[if lt IE 9]>
<div class="alert alert-danger">您正在使用 <strong>过时的</strong> 浏览器. 是时候 <a
href="https://www.microsoft.com/zh-cn/download/internet-explorer.aspx">更换一个更好的浏览器</a> 来提升用户体验.
</div>
<![endif]-->
<!--针对IE8浏览器，我们引入html5shiv来使得HTML5标签在IE8中也能使用-->
<!--[if lt IE 9]>
<script src="${contextPath}/zui/lib/ieonly/html5shiv.js"></script>
<script src="${contextPath}/zui/lib/ieonly/respond.js"></script>
<script src="${contextPath}/zui/lib/ieonly/excanvas.js"></script>
<![endif]-->
<script src="${contextPath}/template7/template7.js" type="text/javascript"></script>
<script src="${contextPath}/js/js-ext.js" type="text/javascript"></script>
<script src="${contextPath}/js/zui-ext.js" type="text/javascript"></script>