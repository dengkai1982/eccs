<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/share.jsp"%>
<script>
    var contextPath="${contextPath}";
    var backUrl="${contextPath}${requestScope.backUrl}${suffix}?${paginationCurrentPage}=${requestScope.currentPage}";
    $(".tobackAction").click(function(){
        window.location.href=backUrl;
    })
    //查询员工
    function queryEmployeWindow(title,fieldId,fieldName){
        var url="${contextPath}/employee/queryPage${suffix}?fieldId="+fieldId+"&fieldName="+fieldName;
        employeeModalTrigger= new $.zui.ModalTrigger({
            url:url,
            size:'fullscreen',
            title:title,
            backdrop:'static',
            type:'iframe',
            width:400,
            height:280
        });
        employeeModalTrigger.show({hidden: function() {
            $("#triggerModal").remove();
        }});
    }
    //查询工程
    function queryProjectWindow(title,fieldId,fieldName){
        var url="${contextPath}/project/queryPage${suffix}?fieldId="+fieldId+"&fieldName="+fieldName;
        projectModalTrigger= new $.zui.ModalTrigger({
            url:url,
            size:'fullscreen',
            title:title,
            backdrop:'static',
            type:'iframe',
            width:400,
            height:280
        });
        projectModalTrigger.show({hidden: function() {
            $("#triggerModal").remove();
        }});
    }
</script>
