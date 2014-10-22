<%@page language="java" contentType="text/html; charset=utf-8" %> 
<%@page import="java.util.*"%>
<%@page import="java.io.*"%>
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
	Properties pro = new Properties();
	System.out.println(Object.class);
	InputStream is = Object.class.getClassLoader().getResourceAsStream("server.properties");
	try {  
        pro.load(is);  
        is.close();  
    } catch (IOException e1) {  
        e1.printStackTrace();  
    } 
    System.out.println(pro.getProperty("test"));
%>

</body>