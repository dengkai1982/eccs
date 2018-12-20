<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/share.jsp" %>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
    <%@ include file="/WEB-INF/pageResource.jsp"%>
</head>
<body style="background: #fff;">
<%@include file="/WEB-INF/menus.jsp"%>
<div class="edu_content">
    <div class="oper_panel base-border main_panel_bg">
        <div class="heading">
            ${webPage.pageTitle}
        </div>
        <div class="actions">
            <div class="btn-group">
                <a href="#"  class="btn btn-link tobackAction"><i class="icon icon-reply-all"></i> 返回</a>
            </div>
        </div>
    </div>
    <article class="main_center base-border">
        <c:set var="entity" value="${requestScope.entity}"></c:set>
        <%@include file="/WEB-INF/project/projectInfo.jsp"%>
        <table class="table table-bordered no-bottom-margin margin-top-10">
            <thead>
            <tr>
                <th class="left_column" colspan="4">收款记录</th>
            </tr>
            <tr>
                <th style="width:150px;">收款时间</th>
                <th style="width:150px;">收款金额</th>
                <th style="width:100px;">操作员</th>
                <th>备注</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${requestScope.flows}" var="flow">
                <tr>
                    <td><fmt:formatDate value="${flow.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                    <td><currency:convert value="${flow.amount}"/></td>
                    <td>${flow.operMan.realName}</td>
                    <td>${flow.remark}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </article>
</div>
<%@ include file="/WEB-INF/pageCommons.jsp"%>
<script src="${contextPath}/js/commons.js" type="text/javascript"></script>
<script type="text/javascript">
    function pageReady(doc){

    }
</script>
</body>
</html>