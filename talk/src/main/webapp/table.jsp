<%@page language="java" contentType="text/html; charset=utf-8" %> 
<%@page import="java.util.*"%>
<%@taglib uri="http://displaytag.sf.net" prefix="display" %>
<head>
<style type="text/css" media="all">         
	@import url("css/maven-base.css");         
	@import url("css/maven-theme.css");         
	@import url("css/site.css");         
	@import url("css/screen.css");     
</style>
<head>
<body>
<%
	List test = new ArrayList(6);
    test.add("test1 array1");
	test.add("test1 array1");
	test.add("test1 array1");
	test.add("test1 array1");
	test.add("test1 array1");
	request.setAttribute("test",test);
%>
<display:table name="test"/>
</body>