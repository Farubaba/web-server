<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String pageName = pageContext.getPage().getClass().getSimpleName();
%>

<html>
  <head>
  	<base href="<%=basePath%>">
    <meta charset="UTF-8">
    <title>Mobile Server Struts2 Application - Welcome</title>
  </head>
  <body>
  	<p>getServletName = <%= getServletName() %></p>
  	<p>getServletInfo = <%= getServletInfo() %></p>
  	<p>getServletContext.getAttribute() = <%= getServletContext().getAttribute("") %></p>
  	
    <h2><%=pageName.replace("_002d", "-").replace("_", ".") %></h2>
    <h4>mobile-server提供的所有api接口地址：</h4>
    <ol>
     	<li>系统配置：<a href="api/sys/config/v1" target="_blank"><%=basePath%>api/sys/config/v1</a></li>
     	
    </ol>
    <p><p><p><s:include value="road-map.jsp"></s:include>
  </body>
</html>