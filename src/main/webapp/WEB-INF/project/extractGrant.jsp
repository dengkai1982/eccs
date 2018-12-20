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
                <a href="javascript:void(0)" id="addEmployeeItem" class="btn btn-link"><i class="icon icon-list"></i> 添加员工提成项</a>
                <a href="#" class="btn btn-link tobackAction"><i class="icon icon-reply-all"></i> 返回</a>
            </div>
        </div>
    </div>
    <article class="main_center base-border">
        <form id="new_or_edit_page_form">
            <c:set var="entity" value="${requestScope.entity.projectManagement}"></c:set>
            <c:set var="projectAmountFlow" value="${requestScope.entity}"></c:set>
            <input type="hidden" name="projectAmountFlowId" value="${projectAmountFlow.id}">
            <input type="hidden" name="excludeEmployee" value="${requestScope.excludeEmployee}">
            <%@include file="/WEB-INF/project/projectInfo.jsp"%>
            <table class="table table-bordered no-bottom-margin margin-top-10">
                <tbody>
                    <tr>
                        <td class="left_column" colspan="8">已收款项记录</td>
                    </tr>
                    <tr>
                        <td class="left_column" style="width:100px;">入账金额</td>
                        <td class="right_column"><currency:convert value="${projectAmountFlow.amount}"/></td>
                        <td class="left_column" style="width:100px;">到账时间</td>
                        <td class="right_column"><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${projectAmountFlow.createTime}"/></td>
                        <td class="left_column" style="width:100px;">入账记录员</td>
                        <td class="right_column">${projectAmountFlow.operMan.realName}</td>
                        <td class="left_column" style="width:140px;">已发放提成总额</td>
                        <td class="right_column">
                            <c:choose>
                                <c:when test="${empty requestScope.extractGrant}">
                                    0.00
                                </c:when>
                                <c:otherwise>
                                    <currency:convert value="${requestScope.extractGrant.totalGrantAmount}"/>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                    <tr>
                        <td class="left_column">入账备注</td>
                        <td class="right_column" colspan="7">${projectAmountFlow.remark}</td>
                    </tr>
                </tbody>
            </table>
            <fieldset class="margin-top-10">
                <legend>参与人员提成项</legend>
                <div id="extractGrantContainer">
                    <c:forEach items="${requestScope.grantItems}" var="item">
                        <div class="extractGrantItem input-group" style="margin-bottom:10px" data-id="${item.id}">
                            <input type="hidden" name="employeeId" value="${item.employee.id}">
                            <span class="input-group-addon" style="border-right:none">员工姓名</span>
                            <input type="text" class="form-control" style="background:#fff" readonly="" value="${item.employee.name}">
                            <span class="input-group-addon fix-border">提成比例 单位(%)</span>
                            <input type="number" name="rate" class="form-control rate_input" value="${item.rate}">
                            <span class="input-group-addon fix-border">提成金额</span>
                            <input type="number" name="amount" class="form-control amoutn_input" value="<currency:convert value='${item.amount}'/>">
                            <span class="input-group-btn">
                                <button class="btn btn-default delete_extract_grant_item" type="button">删除</button>
                            </span>
                        </div>
                    </c:forEach>
                </div>
            </fieldset>
            <div class="form-horizontal">
                <div class="form-group">
                    <div class="col-md-6 col-sm-11">
                        <button id="commitForm" type="button" class="btn btn-success">保存提交</button>&nbsp;&nbsp;
                        <a href="#" class="btn btn-danger tobackAction">关闭返回</a>
                    </div>
                </div>
            </div>
        </form>
    </article>
</div>
<div class="modal fade" id="choose_project_employee">
    <div class="modal-dialog" style="width: 420px;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span class="sr-only">关闭</span></button>
                <h4 class="modal-title">选择项目参与员工</h4>
            </div>
            <div class="modal-body">
                <table class="table table-bordered">
                    <thead>
                        <tr>
                            <th>员工姓名</th>
                            <th>选择</th>
                        </tr>
                    </thead>
                    <tbody id="project_employee_container">

                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
<%@include file="/WEB-INF/template.jsp"%>
<%@ include file="/WEB-INF/pageCommons.jsp"%>
<script src="${contextPath}/js/commons.js" type="text/javascript"></script>
<script type="text/javascript">
    function pageReady(doc){
        $("#addEmployeeItem").click(function(){
            var excludeEmployee=$("input[name='excludeEmployee']").val();
            var projectId="${entity.id}";
            getJson("${contextPath}/project/queryProjectParticipateEmployee${suffix}",{
                excludeEmployee:excludeEmployee,
                projectId:projectId
            },"正在获取参与员工信息",6000,function(data){
                if(data.participateEmployee==undefined){
                    bootbox.alert({
                        title:"提示",
                        message: "已经没有可选择的员工",
                        callback: function () {

                        }
                    })
                }else{
                    var context={
                        employees:new Array()
                    }
                    $.each(data.participateEmployee,function(i,d){
                        context.employees.push({
                            employeeName:d.employee.name,
                            employeeId:d.employee.id
                        })
                    })
                    var project_employee_container=$("#choose_project_employee").find("#project_employee_container");
                    var employeeListHtml=getTemplateHtml("chooseJoinProjectEmployee",context,$);
                    project_employee_container.empty().append(employeeListHtml);
                    $("#choose_project_employee").modal('show');
                }
            })
        });
        $("#choose_project_employee #project_employee_container").on("click",".employeeChoosed",function(){
            var employeeName=$(this).attr("employeeName");
            var employeeId=$(this).attr("employeeId");
            var excludeEmployee=$("input[name='excludeEmployee']").val().split(",");
            excludeEmployee.push(employeeId);
            var joinStr=excludeEmployee.join(",");
            $("input[name='excludeEmployee']").val(clearFirstChar(joinStr,","));
            var rate=parseFloat("${entity.rate}")/100;
            var flowAmount=parseFloat("<currency:convert value="${projectAmountFlow.amount}"/>");
            var amount=(flowAmount*rate).toFixed(2);
            var extractGrantItem=getTemplateHtml("extractGrantItemNode",{
                id:"e_"+employeeId,
                employeeId:employeeId,
                employeeName:employeeName,
                rate:parseInt("${entity.rate}"),
                amount:amount
            },$);
            $("#extractGrantContainer").append(extractGrantItem);
            $("#choose_project_employee").modal('hide');
        });
        $("#extractGrantContainer").on("keyup",".rate_input",function(){
            var rate=parseFloat($(this).val())/100;
            var flowAmount=parseFloat("<currency:convert value="${projectAmountFlow.amount}"/>");
            flowAmount=flowAmount*rate;
            $(this).parent(".extractGrantItem").find(".amoutn_input").val(flowAmount.toFixed(2));
        })
        //删除提成
        $("#extractGrantContainer").on("click",".delete_extract_grant_item",function(){
            var extractGrantItem=$(this).parents(".extractGrantItem");
            var dataId=extractGrantItem.attr("data-id");
            if(!dataId.startWith("e_")){
                //通知后台进行删除
                postJSON("${contextPath}/project/deleteExtractGrantItem${suffix}",{
                    itemId:dataId
                },"正在删除请稍后",function(result){

                });
            }
            var employeeId=extractGrantItem.find("input[name='employeeId']").val();
            var excludeEmployee=$("input[name='excludeEmployee']").val().split(",");
            var newArray=new Array();
            for(i=0;i<excludeEmployee.length;i++){
                if(excludeEmployee[i]!=employeeId){
                    newArray.push(excludeEmployee[i]);
                }
            }
            var joinStr=newArray.join(",");
            $("input[name='excludeEmployee']").val(clearFirstChar(joinStr,","));
            extractGrantItem.remove();
        })
        $("#commitForm").click(function(){
            var $this=$(this);
            $this.addClass("disabled");
            var $form=$("#new_or_edit_page_form").formToJson();
            var projectAmountFlowId=$form.projectAmountFlowId;
            var grantItems={
                grantItem:new Array()
            };
            $(".extractGrantItem").each(function(i,d){
                var $this=$(d);
                var employeeId=$this.find("input[name='employeeId']").val();
                var rate=$this.find("input[name='rate']").val();
                var amount=$this.find("input[name='amount']").val();
                grantItems.grantItem.push({
                    employeeId: employeeId,
                    rate: rate,
                    amount: amount
                })
            })
            if(grantItems.grantItem.length==0){
                toast("参与人员未指定");
                $this.removeClass("disabled");
                return;
            }
            postJSON("${contextPath}${requestScope.commitUrl}${suffix}",{
                projectAmountFlowId:projectAmountFlowId,
                grantItems:JSON.stringify(grantItems)
            },"正在处理请稍后",function(result){
                $this.removeClass("disabled");
                if(result.code==SUCCESS){
                    bootbox.confirm({
                        title:"消息",
                        message: "操作完成,点击确认返回",
                        size: 'small',
                        buttons: {
                            confirm: {
                                label: '确认',
                                className: 'btn-success'
                            },
                            cancel: {
                                label: '取消',
                                className: 'btn-danger'
                            }
                        },
                        callback: function (result) {
                            if(result){
                                window.location.href=backUrl;
                            }else{
                                window.location.reload();
                            }
                        }
                    });
                }else{
                    bootbox.alert(result.msg);
                }
            },function(){
                $this.removeClass("disabled");
            });
        })
    }
</script>
</body>
</html>