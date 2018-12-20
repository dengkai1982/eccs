<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/share.jsp" %>
<div class="detail_info">
    <table class="table table-bordered no-bottom-margin">
        <table class="table table-bordered no-bottom-margin">
            <tr>
                <td class="left_column" colspan="6">工程项目信息</td>
            </tr>
            <tr>
                <td class="left_column">项目名称</td>
                <td class="right_column">${entity.projectName}</td>
                <td class="left_column">合同编号</td>
                <td class="right_column">${entity.contractNumber}</td>
                <td class="left_column">项目类型</td>
                <td class="right_column">${entity.projectType}</td>
            </tr>
            <td class="left_column">合同金额</td>
            <td class="right_column"><currency:convert value="${entity.contractAmount}"/></td>
            <td class="left_column">到账金额</td>
            <td class="right_column"><currency:convert value="${entity.transferredAmount}"/></td>
            <td class="left_column">全部到账</td>
            <td class="right_column">
                <c:choose>
                    <c:when test="${entity.contractAmount==entity.transferredAmount}">
                        <span class="text-blue">全部到账</span>
                    </c:when>
                    <c:otherwise>
                        <span class="text-red">剩余 <currency:convert value="${entity.contractAmount-entity.transferredAmount}"/> 未到账</span>
                    </c:otherwise>
                </c:choose>
            </td>
            <tr>
                <td class="left_column">提成比例</td>
                <td class="right_column">${entity.rate}%</td>
                <td class="left_column">项目参与人员</td>
                <td class="right_column" colspan="3">${requestScope.joinEmployees}</td>
            </tr>
        </table>
    </table>
</div>
