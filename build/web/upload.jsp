<%-- 
    Document   : upload
    Created on : 2015/05/13, 14:41:38
    Author     : lepra25-pc
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
    <s:form action="doUpload" method="post" enctype="multipart/form-data">
        <s:file name="upload" label="File"/>
        <s:submit/>
    </s:form>
</body>
</html>
