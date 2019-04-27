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
            <b>提成发放列表</b>
        </div>
        <div class="actions">
            <div class="btn-group">
                <%--
                <a href="${contextPath}/employee/extractGrantManager/new${suffix}?${paginationCurrentPage}=1" class="btn btn-link"  style="margin-left: 10px;"><i class="icon icon-plus"></i> 新增提成发放</a>
                 id="showSearch" 不能修改 --%>
                <a href="#" id="showSearch" class="btn btn-link"><i class="icon icon-search"></i> 搜索</a>
            </div>
        </div>
    </div>
    <div class="search_container base-border none-border-bottom clearfix hidden">
        <form action="#" id="searchQueryForm" class="form-horizontal">
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
        $("#employeeName").click(function(){
            queryEmployeWindow("员工选择","employeeId","employeeName");
        });
        $("#projectName").click(function(){
            queryProjectWindow("项目选择","projectManagementId","projectName");
        })
        $('#remoteDataGrid').datagrid({
            dataSource: {
                cols:[
                    {name: 'extractGrant',sort:false,label:'项目名称',valueOperator:{
                            getter: function(dataValue) {
                                return dataValue.projectAmountFlow.projectManagement.projectName;
                            }
                        }},
                    {name: 'extractGrant',sort:false,label:'收款时间',valueOperator:{
                            getter: function(dataValue) {
                                return dataValue.projectAmountFlow.createTime;
                            }
                        }},
                    {name: 'extractGrant',sort:false,label:'收款金额',valueOperator:{
                            getter: function(dataValue) {
                                return dataValue.projectAmountFlow.amount;
                            }
                        }},
                    {name: 'employee',sort:false,label:'提成人员',valueOperator:{
                            getter: function(dataValue) {
                                return dataValue.name;
                            }
                        }},
                    {name: 'rate',sort:false,label:'提成比例',valueOperator:{
                            getter: function(dataValue) {
                                return dataValue+"%";
                            }
                        }},
                    {name: 'amount',sort:false,label:'提成金额'}
                ],
                remote: function(params) {
                    var requestParams=$("#searchQueryForm").formToJson();
                    requestParams['${paginationCurrentPage}']=getPaginationCurrentPage();
                    requestParams['${paginationMaxResult}']=params.recPerPage;
                    requestParams['${paginationSortBy}']=params.sortBy;
                    requestParams['${paginationOrderType}']=params.order;
                    return {
                        // 原创请求地址
                        url: '${contextPath}/query/extractGrantItem${suffix}',
                        // 请求类型
                        type: 'GET',
                        data: requestParams,
                        // 数据类型
                        dataType: 'json'
                    };
                },
                remoteConverter:function(result){
                    var pagination=createPagination(result);
                    console.log(pagination);
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
                fixedRightFrom: 8  // 从第12列开始固定到右侧
            },
            width:'100%',
            height:getDataGridHeight()
        });
        otherLoad();
    }
    function otherLoad(){
        var formToJson=$("#searchQueryForm").formToJson();
        postJSON("${contextPath}/employee/extractGrantCount${suffix}",formToJson,"正在处理请稍后",function(result){
            if(result.code==SUCCESS){
                $("#tfoot_left").html("发放金额合计:"+result.body);
            }
        });
    }
</script>
</body>
</html>