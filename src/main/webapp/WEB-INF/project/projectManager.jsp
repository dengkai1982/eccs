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
            <b>工程项目</b>
        </div>
        <div class="actions">
            <div class="btn-group">
                <a href="${contextPath}/project/projectManager/new${suffix}?${paginationCurrentPage}=1" class="btn btn-link"  style="margin-left: 10px;"><i class="icon icon-plus"></i> 新建工程项目</a>
                <%-- id="showSearch" 不能修改 --%>
                <a href="#" id="showSearch" class="btn btn-link"><i class="icon icon-search"></i> 搜索</a>
            </div>
        </div>
    </div>
    <div class="search_container base-border none-border-bottom clearfix hidden">
        <form action="#" id="searchQueryForm" class="form-horizontal">
            <div class="form-group">
                <label for="projectName" class="col-sm-1">项目名称</label>
                <div class="col-sm-3">
                    <input type="text" class="form-control" id="projectName" name="projectName">
                </div>
                <label for="contractNumber" class="col-sm-1">合同编号</label>
                <div class="col-sm-3">
                    <input type="text" class="form-control" id="contractNumber" name="contractNumber">
                </div>
                <label for="projectType" class="col-sm-1">项目类型</label>
                <div class="col-sm-2">
                    <input type="text" class="form-control" id="projectType" name="projectType">
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-1">创建时间</label>
                <div class="col-sm-4">
                    <div class="input-group">
                        <span class="input-group-addon">起</span>
                        <input type="text" name="startTime" readonly class="form-control form-date" style="background:#fff" placeholder="请选择开始时间">
                        <span class="input-group-addon fix-border">止</span>
                        <input type="text" name="endTime" readonly class="form-control form-date" style="background:#fff" placeholder="请选择结束时间">
                    </div>
                </div>
                <label class="col-sm-1">合同金额</label>
                <div class="col-sm-5">
                    <div class="input-group">
                        <span class="input-group-addon">大于等于</span>
                        <input type="number" name="gtContractAmount" class="form-control">
                        <span class="input-group-addon fix-border">小于等于</span>
                        <input type="number" name="ltContractAmount" class="form-control">
                    </div>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-1">实收金额</label>
                <div class="col-sm-5">
                    <div class="input-group">
                        <span class="input-group-addon">大于等于</span>
                        <input type="number" name="gtTransferredAmount" class="form-control">
                        <span class="input-group-addon fix-border">小于等于</span>
                        <input type="number" name="ltTransferredAmount" class="form-control">
                    </div>
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
                    {name: 'projectName',sort:true,label:'项目名称'},
                    {name: 'contractNumber',sort:true,label:'合同编号'},
                    {name: 'projectType',sort:true,label:'项目类型'},
                    {name: 'contractAmount',sort:true,label:'合同金额'},
                    {name: 'transferredAmount',sort:true,label:'到账金额'},
                    {name: 'rate',sort:true,label:'提成比例',valueOperator:{
                            getter: function(dataValue) {
                                return dataValue+"%";
                            }
                        }},
                    {name: 'proportion',sort:true,label:'计提比列',valueOperator:{
                            getter: function(dataValue) {
                                return dataValue+"%";
                            }
                        }},
                    {name: 'commissionAmount',sort:true,label:'计提金额'},
                    {name: 'finishCommission',sort:true,label:'累计提成'},
                    {name: 'createTime',sort:true,label:'创建时间'},
                    {name: 'oper', label: '操作',sort:false,html:true,width:140}
                ],
                remote: function(params) {
                    var requestParams=$("#searchQueryForm").formToJson();
                    requestParams['${paginationCurrentPage}']=getPaginationCurrentPage();
                    requestParams['${paginationMaxResult}']=params.recPerPage;
                    requestParams['${paginationSortBy}']=params.sortBy;
                    requestParams['${paginationOrderType}']=params.order;
                    return {
                        // 原创请求地址
                        url: '${contextPath}/query/projectManagement${suffix}',
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
                            href:"${contextPath}/project/projectManager/modify${suffix}?id="+d.id+
                            "&${paginationCurrentPage}="+getPaginationCurrentPage(),
                            classStyle:"text-primary margin_right",
                            title:"编辑修改",
                            entityId:d.id,
                            access:"",
                            showName:"编辑修改"
                        },$);
                        editor+=getTemplateHtml("operAction",{
                            href:"${contextPath}/project/projectManager/detail${suffix}?id="+d.id+
                            "&${paginationCurrentPage}="+getPaginationCurrentPage(),
                            classStyle:"text-primary margin_right",
                            title:"详细信息",
                            entityId:d.id,
                            access:"",
                            showName:"详细信息"
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
                fixedRightFrom: 11  // 从第12列开始固定到右侧
            },
            width:'100%',
            height:getDataGridHeight()
        });
    }
</script>
</body>
</html>

