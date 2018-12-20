<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/share.jsp"%>
<div class="modal fade" id="change_my_password_modal">
    <div class="modal-dialog" style="width: 420px;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span class="sr-only">关闭</span></button>
                <h4 class="modal-title">修改密码</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" id="change_password_form">
                    <div class="form-group">
                        <label for="oldPassword" style="float:left;width:56px;">旧密码</label>
                        <input type="password" style="float:left;width:270px;" class="form-control" name="oldPassword" id="oldPassword" placeholder="请输入旧密码">
                    </div>
                    <div class="form-group">
                        <label for="oldPassword" style="float:left;width:56px;">新密码</label>
                        <input type="password" style="float:left;width:270px;" class="form-control" name="newPassword" id="newPassword1" placeholder="请输入新密码">
                    </div>
                    <div class="form-group">
                        <label for="oldPassword" style="float:left;width:56px;">确认密码</label>
                        <input type="password" style="float:left;width:270px;" class="form-control" name="donePassword" id="newPassword2" placeholder="请再次确认新密码">
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-success" data-dismiss="modal">取消返回</button>
                <button type="button" class="btn btn-danger" id="save_change_password">立即修改</button>
            </div>
        </div>
    </div>
</div>
<nav id="primaryNavbar">
    <ul class="nav nav-stacked">
        <c:forEach items="${userMenu}" var="mainMenu">
            <c:if test="${mainMenu.showable}">
                <c:choose>
                    <c:when test="${mainMenu.active}">
                        <li class="active"><a data-toggle="tooltip" href="#" title="${mainMenu.detail}">${mainMenu.name}</a></li>
                    </c:when>
                    <c:otherwise>
                        <c:choose>
                            <c:when test="${empty mainMenu.actionFlag}">
                                <li><a data-toggle="tooltip" href="${contextPath}/commons/chooseFirstMenu${suffix}?id=${mainMenu.id}" title="${mainMenu.detail}">${mainMenu.name}</a></li>
                            </c:when>
                            <c:otherwise>
                                <li><a data-toggle="tooltip" href="${contextPath}${mainMenu.actionFlag}${suffix}" title="${mainMenu.detail}">${mainMenu.name}</a></li>
                            </c:otherwise>
                        </c:choose>
                    </c:otherwise>
                </c:choose>
            </c:if>
        </c:forEach>
    </ul>
    <ul class="nav nav-stacked fixed-bottom">
        <li data-toggle="tooltip" title="" data-id="profile" class="dropdown" data-original-title="demo">
            <a href="#" class="dropdown-toggle" data-toggle="dropdown"><i class="icon icon-user icon-2x"></i><span
                    class="text-username"> demo <b class="caret"></b></span></a>
            <ul class="dropdown-menu">
                <li class="heading"><i class="icon icon-user icon-large"></i><strong>${user.loginName}</strong></li>
                <li class="divider"></li>
                <li>
                    <a href="#" data-toggle="modal" data-target="#change_my_password_modal">修改密码</a>
                </li>
                <li>
                    <a href="#" id="exitUserLogin">退出登录</a>
                </li>
            </ul>
        </li>
    </ul>
</nav>
<nav class="navbar navbar-fixed-top" role="navigation" id="mainNavbar">
    <div class="collapse navbar-collapse" id="mainNavbarCollapse">
        <ul class="nav navbar-nav">
            <c:forEach items="${userMenu}" var="mainMenu" varStatus="vs">
                <c:if test="${mainMenu.active}">
                    <c:forEach items="${mainMenu.childrenList}" var="subMenu">
                        <c:if test="${subMenu.showable}">
                            <c:choose>
                                <c:when test="${subMenu.active}">
                                    <li class="active"><a href="${contextPath}${subMenu.actionFlag}${suffix}" title="${subMenu.detail}">${subMenu.name}</a></li>
                                </c:when>
                                <c:otherwise>
                                    <li><a href="${contextPath}${subMenu.actionFlag}${suffix}" title="${subMenu.detail}">${subMenu.name}</a></li>
                                </c:otherwise>
                            </c:choose>
                        </c:if>
                    </c:forEach>
                </c:if>
            </c:forEach>
        </ul>
    </div>
</nav>
<nav class="navbar navbar-default navbar-fixed-bottom hidden-sm hidden-xs">
    <ul class="breadcrumb pull-left" id="positionBar">
        <c:forEach items="${webPage.navigations}" var="nav">
            <c:choose>
                <c:when test="${nav.active}">
                    <li><a href="#" class="active">${nav.name}</a></li>
                </c:when>
                <c:otherwise>
                    <c:choose>
                        <c:when test="${empty nav.path}">
                            <li><a href="${contextPath}/commons/chooseFirstMenu${suffix}?id=${nav.accessId}">${nav.name}</a></li>
                        </c:when>
                        <c:otherwise>
                            <li><a href="${contextPath}${nav.path}${suffix}">${nav.name}</a></li>
                        </c:otherwise>
                    </c:choose>
                </c:otherwise>
            </c:choose>
        </c:forEach>
    </ul>
    <div id="powerby">
        <span>${system_name}</span>
    </div>
</nav>
<script>
    $("#save_change_password").click(function(){
        var $form=$("#change_password_form").formToJson();
        if($form.oldPassword==""){
            bootbox.alert("旧密码必须填写");
            return;
        }
        if($form.newPassword==""){
            bootbox.alert("新密码必须填写");
            return;
        }
        if($form.newPassword!=$form.donePassword){
            bootbox.alert("两次密码不一致");
            return;
        }
        postJSON("${contextPath}/mgr/master/changePassword${suffix}",{
            oldPassword:$form.oldPassword,
            newPassword:$form.newPassword
        },"正在修改密码请稍后",function(result){
            if(result.code==SUCCESS){
                bootbox.alert({
                    title:"消息",
                    message: "修改密码成功,点击确认关闭",
                    callback: function () {
                        $("#change_my_password_modal").modal('hide');
                    }
                })
            }else{
                bootbox.alert({
                    title:"错误",
                    message:result.msg,
                    callback:function(){

                    }
                });
            }
        });
    });
    $('#change_my_password_modal').on('show.zui.modal', function() {
        document.getElementById("change_password_form").reset();
    });
    $("#exitUserLogin").click(function(){
        bootbox.confirm({
            title:'提示',
            message: "确实要退出本次登录",
            buttons: {
                confirm: {
                    label: '确定',
                    className: 'btn-success'
                },
                cancel: {
                    label: '取消',
                    className: 'btn-danger'
                }
            },
            callback: function (result) {
                if(result){
                    postJSON("${contextPath}/mgr/master/exit${suffix}",{},"正在退出系统请稍后",function(data){
                        window.location.href="${contextPath}/login.jsp";
                    });
                }
            }
        });
    });
</script>