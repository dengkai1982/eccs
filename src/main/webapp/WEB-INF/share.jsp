
<%@ page import="kaiyi.app.eccs.controller.ManagerController" %>
<%@ page import="kaiyi.puer.web.servlet.WebInteractive" %>
<%@ page import="kaiyi.puer.web.servlet.WebPage" %>
<%@page pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ page isELIgnored="false" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://kaiyi.puer.web/jsp/jstl/enums" prefix="enums"%>
<%@ taglib uri="http://kaiyi.puer.web/jsp/jstl/currency" prefix="currency"%>
<%@ taglib uri="http://kaiyi.puer.web/jsp/jstl/webPage" prefix="webpage"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:set var="suffix" value=".xhtml"/>
<c:set var="pagination" value="<%=request.getAttribute(ManagerController.PAGINATION)%>"/>
<c:set var="paginationCurrentPage" value="<%=WebInteractive.PAGINATION_PARAMETER_CURRENT_PAGE%>"/>
<c:set var="paginationFirst" value="<%=WebInteractive.PAGINATION_PARAMETER_FIRST%>"/>
<c:set var="paginationMaxResult" value="<%=WebInteractive.PAGINATION_PARAMETER_MAX_RESULT%>"/>
<c:set var="paginationSortBy" value="<%=WebInteractive.ORDER_SORT_BY%>"/>
<c:set var="paginationOrderType" value="<%=WebInteractive.ORDER_TYPE%>"/>
<c:set var="webPage" value="<%=request.getAttribute(WebPage.WEB_PAGE)%>"/>
<c:set var="userMenu" value="<%=ManagerController.getMenuForApplication(request)%>"/>
<c:set var="system_name" value="欣升公司工程费用提成管理"/>
<c:set var="user" value="<%=ManagerController.getLoginedUser(request)%>"/>