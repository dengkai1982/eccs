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
        <form class="form-horizontal" id="new_or_edit_page_form">
            <input type="hidden" name="projectManagementId" value="${requestScope.entity.projectManagement.id}">
            <input type="hidden" name="editor" value="${requestScope.editor}">
            <input type="hidden" name="flowId" value="${requestScope.entity.id}">
            <div class="form-group">
                <label for="projectName" class="col-sm-1 required">收款项目</label>
                <div class="col-md-6 ref_projectName">
                    <input type="text" class="form-control" readonly validate="required:必须制定收款项目" placeholder="请点击选择"
                           id="projectName" name="projectName" value="${requestScope.entity.projectManagement.projectName}">
                </div>
            </div>
            <div class="form-group">
                <label for="amount" class="col-sm-1 required">收款金额</label>
                <div class="col-md-6 ref_amount">
                    <input type="number" class="form-control"  validate="required:收款金额必须填写"
                           id="amount" name="amount" value="<currency:convert value='${requestScope.entity.amount}'/>">
                </div>
            </div>
            <div class="form-group">
                <label for="amount" class="col-sm-1 required">收款时间</label>
                <div class="col-md-6 ref_amount">
                    <input type="text" class="form-control form-date" placeholder="请选择" readonly
                           value="<fmt:formatDate value='${requestScope.entity.receivablesDate}' pattern='yyyy-MM-dd'/>" id="receivablesDate" name="receivablesDate">
                </div>
            </div>
            <div class="form-group">
                <label for="remark" class="col-sm-1">备注</label>
                <div class="col-md-6 ref_amount">
                    <textarea class="form-control" name="remark" rows="3" id="remark">${requestScope.entity.remark}</textarea>
                </div>
            </div>
            <div class="form-group">
                <label  class="col-sm-1">&nbsp;</label>
                <div class="col-md-6 col-sm-11">
                    <button id="commitForm" type="button" class="btn btn-success">保存提交</button>&nbsp;&nbsp;
                    <a href="#" class="btn btn-danger tobackAction">关闭返回</a>
                </div>
            </div>
        </form>
    </article>
</div>
<%@ include file="/WEB-INF/pageCommons.jsp"%>
<script src="${contextPath}/js/commons.js" type="text/javascript"></script>
<script type="text/javascript">
    function pageReady(doc){
        $("#commitForm").click(function(){
            var $this=$(this);
            $this.addClass("disabled");
            var flag=$("form").formValidate(function(el,hint){
                $(".ref_"+el.attr("name")).addClass("has-error");
                toast(hint);
                el.focus().keydown(function(){
                    $(".ref_"+el.attr("name")).removeClass("has-error");
                })
            });
            if(flag){
                var $form=$("#new_or_edit_page_form").formToJson();
                postJSON("${contextPath}${requestScope.commitUrl}${suffix}",$form,"正在处理请稍后",function(result){
                    $this.removeClass("disabled");;
                    if(result.code==SUCCESS){
                        newOrUpdateFinish("消息","添加收款项成功,点击确认返回",false);
                    }else{
                        bootbox.alert(result.msg);
                    }
                });
            }else{
                $this.removeClass("disabled");
            }
        });
        //查询项目
        $("#projectName").click(function(){
            projectModalTrigger= new $.zui.ModalTrigger({
                url:"${contextPath}/project/queryPage${suffix}?fieldId=projectManagementId&fieldName=projectName",
                size:'fullscreen',
                title:'项目选择',
                backdrop:'static',
                type:'iframe',
                width:400,
                height:280
            });
            projectModalTrigger.show({hidden: function() {
                    $("#triggerModal").remove();
                }});
        })
    }
</script>
</body>
</html>