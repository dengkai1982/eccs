<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/share.jsp" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
		<meta name="renderer" content="webkit">
		<title>用户登录 - ${system_name}</title>
		<link rel="icon" href="${contextPath}/img/favicon.ico" type="image/x-icon" />
		<link rel="shortcut icon" href="${contextPath}/img/favicon.ico" type="image/x-icon" />
		<link href="${contextPath}/zui/css/zui.min.css" rel="stylesheet">
		<link href="${contextPath}/css/commons.css" rel="stylesheet">
		<link href="${contextPath}/zui/css/zui-theme-indigo.css" rel="stylesheet" id="zui_theme">
		<link href="${contextPath}/zui/lib/datatable/zui.datatable.min.css" rel="stylesheet">
		<link href="${contextPath}/zui/lib/chosen/chosen.css" rel="stylesheet">
		<link href="${contextPath}/zui/lib/chosenicons/zui.chosenicons.css" rel="stylesheet">
		<link href="${contextPath}/zui/lib/bootbox/bootbox.min.css" rel="stylesheet">
		<link href="${contextPath}/css/login.css" rel="stylesheet">
		<link href="${contextPath}/css/theme-indigo.css" rel="stylesheet" id="frame_zheme">
		<!-- jQuery (ZUI中的Javascript组件依赖于jQuery) -->
		<script src="${contextPath}/js/jquery-3.3.1.min.js" type="text/javascript"></script>
		<script src="${contextPath}/js/js-ext.js" type="text/javascript"></script>
		<!-- ZUI Javascript组件 -->
		<script src="${contextPath}/zui/js/zui.min.js" type="text/javascript"></script>
		<script src="${contextPath}/js/zui-ext.js" type="text/javascript"></script>
		<script src="${contextPath}/zui/lib/datatable/zui.datatable.min.js"></script>
		<script src="${contextPath}/zui/lib/chosen/chosen.js" type="text/javascript"></script>
		<script src="${contextPath}/zui/lib/chosenicons/zui.chosenicons.js" type="text/javascript"></script>
		<script src="${contextPath}/zui/lib/datetimepicker/datetimepicker.min.js" type="text/javascript"></script>
		<script src="${contextPath}/zui/lib/bootbox/bootbox.min.js"></script>
		<script src="${contextPath}/js/jquery.blockUI.js" type="text/javascript"></script>
		<script src="${contextPath}/js/login_cloud.js" type="text/javascript"></script>
	</head>
	<body>
		<div id="mainBody">
			<div id="cloud1" class="cloud"></div>
			<div id="cloud2" class="cloud"></div>
		</div>
		<div class="loginbox" id="loginbox">
			<form id="login_form">
				<div class="form-group" id="ref_username">
					<div class="input-group">
					  <span class="input-group-addon"><i class="icon icon-user"></i></span>
					  <input type="text" name="username" validate="required:用户名必须填写" class="form-control input-lg" placeholder="请输入登录名">
					</div>
				</div>
				<div class="form-group" id="ref_password">
					<div class="input-group">
					  <span class="input-group-addon"><i class="icon icon-key"></i></span>
					  <input type="password" name="password" validate="required:密码必须填写" class="form-control input-lg" placeholder="请输入密码">
					</div>
				</div>
				<div class="help-block">&nbsp;<span class="text-danger"><strong id="error_msg"></strong></span> </div>
				<button type="button" id="loginButton" class="btn btn-block btn-primary btn-lg">立即登录</button>
			</form>
		</div>
		<div class="logintop">
			<span>欢迎登录${system_name}</span>
		</div>
		<script type="text/javascript">
			var contextPath="${contextPath}";
		</script>
		<script src="${contextPath}/js/commons.js" type="text/javascript"></script>
		<script type="text/javascript">
			function pageReady(doc){
			    $("#loginButton2").click(function(){
                    bootbox.alert("Hello world!");
				});
				$("#loginButton").click(function(){
					$("#error_msg").html("");
					var flag=$("#login_form").formValidate(function(el,hint){
						$("#error_msg").html(hint);
						$("#ref_"+el.attr("name")).addClass("has-error");
						el.focus().keydown(function(){
							$("#ref_"+el.attr("name")).removeClass("has-error");
						});
					});
					if(flag){
						var data=$("#login_form").formToJson();
						postJSON("${contextPath}/master/login${suffix}",data,"正在登录请稍后...",function(result){
							if(result.code==SUCCESS){
								showBlock("正在进入请稍后");
								window.location.href=result.body;
							}else{
								$("#error_msg").html(result.msg);
							}
						});
					}
				});
			}
		</script>
	</body>
</html>