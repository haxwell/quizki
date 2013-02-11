<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" xmlns:c="http://java.sun.com/jsp/jstl/core" version="2.0">
    <jsp:directive.page language="java"
        contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
    <jsp:text>
        <![CDATA[ <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> ]]>
    </jsp:text>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Logout - Questions JEE</title>
</head>
<body>

<jsp:scriptlet>
org.apache.shiro.SecurityUtils.getSubject().logout();

request.getSession().setAttribute("currentUserEntity", null);
//request.getSession().setAttribute("shiroSubject", null);

response.sendRedirect("/index.jsp");
</jsp:scriptlet>
	
</body>
</html>
</jsp:root>