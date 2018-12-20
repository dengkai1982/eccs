<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/share.jsp" %>
<%-- copy 过来的代码,写完后删除 --%>
<!DOCTYPE html>
<html lang="zh-cn">
	<head>
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
		<meta name="renderer" content="webkit">
		<title>访问受限 - ${systemName}</title>
		<!-- zui -->
		<link href="${contextPath}/zui/css/zui.min.css" rel="stylesheet">
		<link href="${contextPath}/css/commons.css" rel="stylesheet">
		<link href="${contextPath}/zui/css/zui-theme-indigo.css" rel="stylesheet">
		<script src="${contextPath}/js/jquery-3.3.1.min.js" type="text/javascript"></script>
		<script src="${contextPath}/zui/js/zui.min.js" type="text/javascript"></script>
		<!--[if lt IE 8]>
    			<div class="alert alert-danger">您正在使用 <strong>过时的</strong> 浏览器. 是时候 <a href="https://www.microsoft.com/zh-cn/download/internet-explorer.aspx">更换一个更好的浏览器</a> 来提升用户体验.</div>
  		<![endif]-->
		<!--针对IE8浏览器，我们引入html5shiv来使得HTML5标签在IE8中也能使用-->
		<!--[if lt IE 9]>
	  		<script src="${contextPath}/zui/lib/ieonly/html5shiv.js"></script>
	  		<script src="${contextPath}/zui/lib/ieonly/respond.js"></script>
	  		<script src="${contextPath}/zui/lib/ieonly/excanvas.js"></script>
		<![endif]-->
		<style>
			#featurebar ul.nav li .chosen-container a.chosen-single {
				background: #F8FAFE;
				border: none;
				-webkit-box-shadow: none;
				box-shadow: none;
				padding-top: 5px;
			}
			#featurebar ul.nav li .chosen-container .chosen-drop {
				min-width: 200px;
				!important
			}
			#dept_chosen.chosen-container .chosen-drop {
				min-width: 400px;
				!important
			}
			body {
				background: #f1f1f1;
			}
			.container {
				padding: 0
			}
			.modal-dialog {
				width: 500px!important;
				margin-top: 10%;
			}
			.modal-footer {
				text-align: center;
				margin-top: 0;
			}
			.table,
			.alert {
				margin: 0;
			}
			.table+.alert {
				margin-top: 20px;
			}
			.table.table-form>thead>tr>th,
			.table.table-form>tbody>tr>th,
			.table.table-form>tfoot>tr>th {
				color: #666
			}
			.table>thead>tr>th {
				background-color: transparent;
			}
			.table.table-form>thead>tr>th,
			.table.table-form>tbody>tr>th,
			.table.table-form>tfoot>tr>th,
			.table.table-form>thead>tr>td,
			.table.table-form>tbody>tr>td,
			.table.table-form>tfoot>tr>td {
				vertical-align: middle;
			}
			@media (max-width: 700px) {
				.modal-dialog {
					padding: 0;
				}
				.modal-content {
					box-shadow: none;
					border-width: 1px 0;
					border-radius: 0
				}
			}
			.alert {
				display: table;
				background: #fff;
			}
			.btn {
				transition: none;
			}
			.body-modal .modal-dialog {
				margin: 50px auto;
				border: none;
				box-shadow: none;
			}
		</style>
	</head>

	<body>
		<div class='container'>
			<div class='modal-dialog'>
				<div class='modal-header'><strong>访问受限</strong></div>
				<div class='modal-body'>
					<div class='alert with-icon alert-pure'>
						<i class='icon-info-sign'></i>
						<div class='content'>
							抱歉，您无权访问该资源或回话已失效,请重新登录 </div>
					</div>
				</div>
				<div class='modal-footer'>
					<a href='${contextPath}' class='btn btn-primary'>重新登录</a>
				</div>
			</div>
		</div>
	</body>
</html>