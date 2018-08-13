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
  	<h2><%=pageName.replace("_002d", "-").replace("_", ".") %></h2>
    <h2>Welcome To Struts 2!</h2>
    <h4>Powered by maven-jetty-plugin!</h4>
    <p>项目根目录：path = <%=path%></p>
    <p>Url根目录：basePath = <%=basePath%></p>
    <h4>可以把鼠标放在超链接上观察浏览器(例如：chrome)左下角显示的超链接地址:</h4>
    <ol>
    	<li>不通过action，直接访问项目根目录(mobile-server/)中的HelloWorld.jsp : <a href="<%=basePath%>HelloWorld.jsp">[mobile-server/HelloWorld.jsp]</a></li>
    	<li>通过 hello.action 访问 : <a href="<s:url action='hello'/>">[mobile-server/HelloWorld.jsp]</a></li>
    	<li>通过 login.action 访问 (没有给userName和password参数) : <a href="<s:url action='login'/>">[mobile-server/WEB-INF/resources/jsp/login.jsp]</a></li>
    	<li>通过 jettyDebugConfig.action 访问 : <a href="<s:url action='jettyDebugConfig'/>">[mobile-server/WEB-INF/resources/jsp/maven-jetty-debug-config.jsp]</a></li>
    </ol>
    
    <p><p><p><s:include value="road-map.jsp"></s:include>
  </body>
</html>