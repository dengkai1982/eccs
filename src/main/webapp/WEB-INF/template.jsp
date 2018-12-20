<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/share.jsp" %>
<script id="operAction" type="text/template7">
    <a href="{{href}}" class="{{classStyle}}" target="{{#if isBlank}}_blank{{else}}_self{{/if}}" access="{{access}}" entityId="{{entityId}}" title="{{title}}">{{showName}}</a>
</script>
<script id="extractGrantItemNode" type="text/template7">
    <div class="extractGrantItem input-group" style="margin-bottom:10px" data-id="{{id}}">
        <input type="hidden" name="employeeId" value="{{employeeId}}">
        <span class="input-group-addon" style="border-right:none">员工姓名</span>
        <input type="text" class="form-control" style="background:#fff" readonly value="{{employeeName}}">
        <span class="input-group-addon fix-border">提成比例 单位(%)</span>
        <input type="number" name="rate" class="form-control rate_input" value="{{rate}}">
        <span class="input-group-addon fix-border">提成金额</span>
        <input type="number" name="amount" class="form-control amoutn_input" value="{{amount}}">
        <span class="input-group-btn">
            <button class="btn btn-default delete_extract_grant_item" type="button">删除</button>
        </span>
    </div>
</script>
<script id="chooseJoinProjectEmployee" type="text/template7">
    {{#each employees}}
    <tr>
        <td>{{employeeName}}</td>
        <td><a href="javascript:void(0)" class="employeeChoosed" employeeId="{{employeeId}}" employeeName="{{employeeName}}">立即选择</a></td>
    </tr>
    {{/each}}
</script>
