<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/share.jsp" %>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
    <%@ include file="/WEB-INF/pageResource.jsp"%>
</head>
<body style="background: #fff;">
<div class="edu_content" style="padding: 25px 10px 0 10px;">
    <div class="oper_panel base-border main_panel_bg">
        <div class="heading">
            <b>员工查询</b>
        </div>
    </div>
    <!--搜索区域 form id 不要修改-->
    <div class="search_container base-border none-border-bottom clearfix">
        <form action="#" id="searchQueryForm" class="form-horizontal">
            <div class="form-group">
                <label for="name" class="col-sm-1">员工姓名</label>
                <div class="col-sm-2">
                    <input type="text" class="form-control" id="name" name="name">
                </div>
                <label for="onTheJob" class="col-sm-1">是否在职</label>
                <div class="col-sm-2">
                    <select name='onTheJob' data-placeholder="请选择" id="onTheJob" class='form-control chosen-select'>
                        <option></option>
                        <option value="true">在职</option>
                        <option value="false">离职</option>
                    </select>
                </div>
                <label class="col-sm-1">入职时间</label>
                <div class="col-sm-4">
                    <div class="input-group">
                        <span class="input-group-addon">起</span>
                        <input type="text" name="startJobDate" readonly class="form-control form-date" style="background:#fff" placeholder="请选择开始时间">
                        <span class="input-group-addon fix-border">止</span>
                        <input type="text" name="endJobDate" readonly class="form-control form-date" style="background:#fff" placeholder="请选择结束时间">
                    </div>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-1">离职时间</label>
                <div class="col-sm-4">
                    <div class="input-group">
                        <span class="input-group-addon">起</span>
                        <input type="text" name="startExitDate" readonly class="form-control form-date" style="background:#fff" placeholder="请选择开始时间">
                        <span class="input-group-addon fix-border">止</span>
                        <input type="text" name="endExitDate" readonly class="form-control form-date" style="background:#fff" placeholder="请选择结束时间">
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
                    {name: 'projectName',sort:false,label:'项目名称'},
                    {name: 'contractNumber',sort:false,label:'合同编号'},
                    {name: 'projectType',sort:true,label:'项目类型'},
                    {name: 'contractAmount',sort:true,label:'合同金额'},
                    {name: 'transferredAmount',sort:true,label:'实际到账金额'},
                    {name: 'createTime',sort:true,label:'创建时间'},
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
                    $.each(pagination.data,function(i,d){
                        var editor=getTemplateHtml("operAction",{
                            href:"#"+d.projectName,
                            classStyle:"text-primary margin_right chooseIt",
                            title:"立即选择",
                            entityId:d.id,
                            access:"",
                            showName:"立即选择"
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
                fixedRightFrom: 7  // 从第12列开始固定到右侧
            },
            width:'100%',
            height:400
        });
        //迟邦定事件
        $("#remoteDataGrid").on('click','.chooseIt',function(){
            var id=$(this).attr("entityId");
            var name=$(this).attr("href").replace("#","");
            if(window.parent.projectModalTrigger){
                $("input[name='${requestScope.fieldName}']", window.parent.document).val(name);
                $("input[name='${requestScope.fieldId}']", window.parent.document).val(id);
                window.parent.projectModalTrigger.close();
            }
        });
    }
</script>
</body>
</html>