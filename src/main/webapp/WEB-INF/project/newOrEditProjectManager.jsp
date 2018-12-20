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
            <c:set value="${requestScope.entity}" var="entity" />
            <input type="hidden" name="id" value="${entity.id}"/>
            <div class="form-group">
                <label for="projectName" class="col-sm-1 required">项目名称</label>
                <div class="col-md-6 ref_projectName">
                    <input type="text" class="form-control" value="${entity.projectName}" validate="required:项目名称必须填写"
                           id="projectName" name="projectName">
                </div>
            </div>
            <div class="form-group">
                <label for="contractNumber" class="col-sm-1 required">合同编号</label>
                <div class="col-md-6 ref_contractNumber">
                    <input type="text" class="form-control" value="${entity.contractNumber}" validate="required:合同编号必须填写"
                           id="contractNumber" name="contractNumber">
                </div>
            </div>
            <div class="form-group">
                <label for="contractAmount" class="col-sm-1 required">合同金额</label>
                <div class="col-md-6 ref_contractAmount">
                    <input type="number" class="form-control" value="<currency:convert value='${entity.contractAmount}'/>"
                           validate="required:合同金额必须填写" id="contractAmount" name="contractAmount">
                </div>
            </div>
            <div class="form-group">
                <label for="rate" class="col-sm-1 required">提成比例</label>
                <div class="col-md-6 ref_contractAmount">
                    <input type="number" class="form-control" value="${entity.rate}" placeholder="请填写0-100的提成百分比"
                           validate="required:提成比例必须填写" id="rate" name="rate">
                </div>
            </div>
            <div class="form-group">
                <label for="projectType" class="col-sm-1">项目类型</label>
                <div class="col-md-6">
                    <input type="text" class="form-control" value="${entity.projectType}" id="projectType" name="projectType">
                </div>
            </div>
            <div class="form-group">
                <label for="employeeChosen" class="col-sm-1">参与人员</label>
                <div class="col-md-8">
                    <select name='employees' data-placeholder="请选择" id="employeeChosen" class='form-control chosen-select' multiple="">
                        <option></option>
                        <c:forEach items="${requestScope.employeeChosen}" var="sl">
                            <c:choose>
                                <c:when test="${sl.selected}">
                                    <option value='${sl.value}' data-keys="${sl.dataKeys}" selected="selected">${sl.html}</option>
                                </c:when>
                                <c:otherwise>
                                    <option value='${sl.value}' data-keys="${sl.dataKeys}">${sl.html}</option>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </select>
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
        $(".chosen-select").chosen({
            allow_single_deselect:true,
            width:'100%'
        });
        $('select.chosen-select').on('change', function(e,v){
            if(v.deselected){
                postJSON("${contextPath}/master/projectManager/unbindEmployee${suffix}",{
                    employeeId:v.deselected,
                    projectId:"${entity.id}"
                },"正在删除参与人员",function(result){

                })
            }
        });
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
                    $this.removeClass("disabled");
                    var isEdit=checkValue("required",$form.id);
                    var operText=isEdit?"修改":"新增";
                    if(result.code==SUCCESS){
                        newOrUpdateFinish("消息",operText+"工程项目成功,点击确认返回",isEdit);
                    }else{
                        bootbox.alert(result.msg);
                    }
                });
            }else{
                $this.removeClass("disabled");
            }
        });
    }
</script>
</body>
</html>