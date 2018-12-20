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
                <label for="name" class="col-sm-3 required">员工姓名</label>
                <div class="col-md-6 ref_name">
                    <input type="text" class="form-control" value="${entity.name}" validate="required:员工姓名必须填写"
                           id="name" name="name">
                </div>
            </div>
            <div class="form-group">
                <label for="onTheJobChosen" class="col-sm-3 required">是否在职</label>
                <div class="col-md-6 ref_onTheJobChosen">
                    <select name='onTheJob' data-placeholder="请选择" id="onTheJobChosen" class='form-control chosen-select'>
                        <option></option>
                        <c:forEach items="${requestScope.onTheJobChosen}" var="sl">
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
                <label for="jobDate" class="col-sm-3">入职时间</label>
                <div class="col-md-6">
                    <input type="text" class="form-control form-date" placeholder="请选择" readonly value="<fmt:formatDate value='${entity.jobDate}' pattern='yyyy-MM-dd'/>"
                           id="jobDate" name="jobDate">
                </div>
            </div>
            <div class="form-group">
                <label for="exitDate" class="col-sm-3">离职时间</label>
                <div class="col-md-6">
                    <input type="text" class="form-control form-date" placeholder="请选择" readonly value="<fmt:formatDate value='${entity.exitDate}' pattern='yyyy-MM-dd'/>"
                           id="exitDate" name="exitDate">
                </div>
            </div>
            <div class="form-group">
                <label for="profressWages" class="col-sm-3">专业技术职务工资</label>
                <div class="col-md-6">
                    <input type="number" class="form-control" value="<currency:convert value='${entity.profressWages}'/>"
                           id="profressWages" name="profressWages">
                </div>
            </div>
            <div class="form-group">
                <label for="reviewCheckWages" class="col-sm-3">复核人员内部经营责任制考核</label>
                <div class="col-md-6">
                    <input type="number" class="form-control" value="<currency:convert value='${entity.reviewCheckWages}'/>"
                           id="reviewCheckWages" name="reviewCheckWages">
                </div>
            </div>
            <div class="form-group">
                <label for="operateCheckWages" class="col-sm-3">经营协调等人员内部经营责任制考核</label>
                <div class="col-md-6">
                    <input type="number" class="form-control" value="<currency:convert value='${entity.operateCheckWages}'/>"
                           id="operateCheckWages" name="operateCheckWages">
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
                        newOrUpdateFinish("消息",operText+"员工成功,点击确认返回",isEdit);
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