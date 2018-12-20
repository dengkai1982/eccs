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
            <b>项目参与人员</b>
        </div>
        <div class="actions">
            <div class="btn-group">
                <%--
                <a href="${contextPath}/project/participateEmployee/new${suffix}?${paginationCurrentPage}=1" class="btn btn-link"  style="margin-left: 10px;"><i class="icon icon-link"></i> 关联项目参与人员</a>
                 id="showSearch" 不能修改 --%>
                <a href="#" id="showSearch" class="btn btn-link"><i class="icon icon-search"></i> 搜索</a>
            </div>
        </div>
    </div>
    <div class="search_container base-border none-border-bottom clearfix hidden">
        <form action="#" id="searchQueryForm" class="form-horizontal">
            <input type="hidden" class="notClear" name="bindStatus" value="true">
            <input type="hidden" name="employeeId">
            <input type="hidden" name="projectManagementId">
            <div class="form-group">
                <label for="projectName" class="col-sm-1">项目名称</label>
                <div class="col-sm-3">
                    <input type="text" class="form-control" readonly id="projectName" name="projectName">
                </div>
                <label for="employeeName" class="col-sm-1">参与人员</label>
                <div class="col-sm-3">
                    <input type="text" class="form-control" readonly id="employeeName" name="employeeName">
                </div>
                <div class="col-sm-1">
                    <button type="button" id="doSearchAction" class="btn btn-primary">立即查询</button>
                </div>
                <div class="col-sm-1">
                    <button type="button" id="doResetFormAction" class="btn btn-success">重置表单</button>
                </div>
            </div>
        </form>
    </div>
    <article class="main_center base-border">
        <div id="remoteDataGrid" class="datagrid datagrid-striped"></div>
        <%@ include file="/WEB-INF/pagination.jsp"%>
    </article>
</div>
<%@include file="/WEB-INF/template.jsp"%>
<%@ include file="/WEB-INF/pageCommons.jsp"%>
<script src="${contextPath}/js/commons.js" type="text/javascript"></script>
<script type="text/javascript">
    function pageReady(doc){
        $(".chosen-select").chosen({
            allow_single_deselect:true,
            width:'100%'
        });
        $('#remoteDataGrid').datagrid({
            dataSource: {
                cols:[
                    {name: 'projectManagement',sort:false,label:'项目名称',valueOperator:{
                            getter: function(dataValue) {
                                if(dataValue){
                                    return dataValue.projectName;
                                }
                            }
                        }},
                    {name: 'projectManagement',sort:false,label:'合同编号',valueOperator:{
                            getter: function(dataValue) {
                                if(dataValue){
                                    return dataValue.contractNumber;
                                }
                            }
                        }},
                    {name: 'projectManagement',sort:true,label:'项目类型',valueOperator:{
                            getter: function(dataValue) {
                                if(dataValue){
                                    return dataValue.projectType;
                                }
                            }
                        }},
                    {name: 'employee',sort:true,label:'参与人员',valueOperator:{
                            getter: function(dataValue) {
                                if(dataValue){
                                    return dataValue.name;
                                }
                            }
                        }},
                    {name: 'updateTime',sort:true,label:'参与时间'},
                    {name: 'oper', label: '操作',sort:false,html:true}
                ],
                remote: function(params) {
                    var requestParams=$("#searchQueryForm").formToJson();
                    requestParams['${paginationCurrentPage}']=getPaginationCurrentPage();
                    requestParams['${paginationMaxResult}']=params.recPerPage;
                    requestParams['${paginationSortBy}']=params.sortBy;
                    requestParams['${paginationOrderType}']=params.order;
                    return {
                        // 原创请求地址
                        url: '${contextPath}/query/participateEmployee${suffix}',
                        // 请求类型
                        type: 'GET',
                        data: requestParams,
                        // 数据类型
                        dataType: 'json'
                    };
                },
                remoteConverter:function(result){
                    var pagination=createPagination(result);
                    console.log(pagination)
                    $.each(pagination.data,function(i,d){
                        var editor=getTemplateHtml("operAction",{
                            href:"#",
                            classStyle:"text-primary margin_right unProjectBind",
                            title:"移除绑定",
                            entityId:d.id,
                            access:"",
                            showName:"移除绑定"
                        },$);
                        d["oper"]=editor;
                    });
                    setPagination(pagination);
                    return pagination;
                }
            },
            sortable:true,
            showRowIndex:false,
            states: {
                pager:{
                    page: getPaginationCurrentPage(),
                    recPerPage: parseInt('${pagination.maxResult}')
                },
                fixedLeftUntil:1,    // 固定左侧第一列
                fixedRightFrom: 6  // 从第12列开始固定到右侧
            },
            width:'100%',
            height:400
        });
        //查询员工
        $("#employeeName").click(function(){
            queryEmployeWindow("员工选择","employeeId","employeeName");
        });
        $("#projectName").click(function(){
            queryProjectWindow("项目选择","projectManagementId","projectName");
        })
        $("#doResetFormAction").click(function(){
            $("input[name='projectManagementId']").val("");
            $("input[name='employeeId']").val("");
        });
        $("#remoteDataGrid").on('click','.unProjectBind',function(){
            var $this=$(this);
            var id=$this.attr("entityId");
            confirmOper("确实要移除对选定员工的项目绑定?",function(){
                postJSON("${contextPath}/project/unbingUser${suffix}",{
                    id:id
                },"正在执行,请稍后...",function(result){
                    if(result.code==SUCCESS){
                        bootbox.alert({
                            title:"消息",
                            message: "移除项目绑定成功",
                            callback: function () {
                                window.location.reload();
                            }
                        })
                    }else{
                        showMessage(result.msg,1500);
                    }
                });
            })
        });
    }
</script>
</body>
</html>