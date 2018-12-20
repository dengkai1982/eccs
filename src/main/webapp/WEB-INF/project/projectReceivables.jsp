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
            <b>工程收款明细</b>
        </div>
        <div class="actions">
            <div class="btn-group">
                <a href="${contextPath}/project/projectReceivables/new${suffix}?${paginationCurrentPage}=1" class="btn btn-link"  style="margin-left: 10px;"><i class="icon icon-plus"></i> 工程收款</a>
                <%-- id="showSearch" 不能修改 --%>
                <a href="#" id="showSearch" class="btn btn-link"><i class="icon icon-search"></i> 搜索</a>
            </div>
        </div>
    </div>
    <div class="search_container base-border none-border-bottom clearfix hidden">
        <form action="#" id="searchQueryForm" class="form-horizontal">
            <input type="hidden" name="projectManagementId">
            <div class="form-group">
                <label for="projectName" class="col-sm-1">项目名称</label>
                <div class="col-sm-3">
                    <input type="text" class="form-control" readonly id="projectName" name="projectName">
                </div>
                <label class="col-sm-1">收款时间</label>
                <div class="col-sm-4">
                    <div class="input-group">
                        <span class="input-group-addon">起</span>
                        <input type="text" name="startTime" readonly class="form-control form-date" style="background:#fff" placeholder="请选择开始时间">
                        <span class="input-group-addon fix-border">止</span>
                        <input type="text" name="endTime" readonly class="form-control form-date" style="background:#fff" placeholder="请选择结束时间">
                    </div>
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-1">收款金额</label>
                <div class="col-sm-5">
                    <div class="input-group">
                        <span class="input-group-addon">大于等于</span>
                        <input type="number" name="startAmount" class="form-control">
                        <span class="input-group-addon fix-border">小于等于</span>
                        <input type="number" name="endAmount" class="form-control">
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
                    {name: 'projectManagement',sort:false,label:'项目名称',width:220,valueOperator:{
                            getter: function(dataValue) {
                                if(dataValue){
                                    return dataValue.projectName;
                                }
                            }
                        }},
                    {name: 'projectManagement',sort:false,label:'合同编号',width:140,valueOperator:{
                            getter: function(dataValue) {
                                if(dataValue){
                                    return dataValue.contractNumber;
                                }
                            }
                        }},
                    {name: 'projectManagement',sort:true,label:'项目类型',width:100,valueOperator:{
                            getter: function(dataValue) {
                                if(dataValue){
                                    return dataValue.projectType;
                                }
                            }
                        }},
                    {name: 'createTime',sort:true,label:'收款时间',width:180},
                    {name: 'amount',sort:true,label:'收款金额',width:120},
                    {name: 'operMan',sort:true,label:'操作员',width:120,valueOperator:{
                            getter: function(dataValue) {
                                if(dataValue){
                                    return dataValue.realName;
                                }else{
                                    return "未找到"
                                }
                            }
                        }},
                    {name: 'remark',sort:true,label:'备注',width:400},
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
                        url: '${contextPath}/query/projectAmountFlow${suffix}',
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
                            classStyle:"text-primary margin_right deleteFlow",
                            title:"删除记录",
                            entityId:d.id,
                            access:"",
                            showName:"删除记录"
                        },$);
                        editor+=getTemplateHtml("operAction",{
                            href:"${contextPath}/project/projectReceivables/extractGrant${suffix}?id="+d.id+
                            "&${paginationCurrentPage}="+getPaginationCurrentPage(),
                            classStyle:"text-primary margin_right",
                            title:"提成发放",
                            entityId:d.id,
                            access:"",
                            showName:"提成发放"
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
                fixedRightFrom: 8  // 从第12列开始固定到右侧
            },
            width:'100%',
            height:400
        });
        //查询项目
        $("#projectName").click(function(){
            queryProjectWindow("项目选择","projectManagementId","projectName");
        })
        $("#remoteDataGrid").on('click','.deleteFlow',function(){
            var $this=$(this);
            var id=$this.attr("entityId");
            confirmOper("确实要删除选中的收款项?",function(){
                postJSON("${contextPath}/project/projectReceivables/delete${suffix}",{
                    flowId:id
                },"正在执行,请稍后...",function(result){
                    if(result.code==SUCCESS){
                        bootbox.alert({
                            title:"消息",
                            message: "删除收款项成果,点击确认返回",
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