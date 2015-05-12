<%-- 
    Document   : login
    Created on : 2015/05/08, 12:14:47
    Author     : lepra25-pc
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>  
<!DOCTYPE html>
<html>
    <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Login</title>
<script>
function doLogin() {
	var form = document.getElementById('loginId');
	form.action = "login";
	form.submit();
}

function addUser() {
	var form = document.getElementById('loginId');
	form.action = "addUser";
	form.submit();
}

</script>       
    </head>
    <body>
        <h1>ログイン画面</h1>
        <s:actionerror />
        <s:form id="loginId">
            <s:label value="ログインID："/>
            <s:textfield name="loginid" value="" size="24" id="loginid"/><br/><br/>
            <s:label value="パスワード："/>
            <s:password name="password" value="" size="24" id="password"/><br/><br/>
        </s:form>
        <input type="submit" value="ログイン" onClick="doLogin()"/>
        <input type="submit" value="新規登録" onClick="addUser()"/><br/><br/>        

        パスワード忘れた方は<s:a action="changed_pass">こちら</s:a>
    </body>
</html>

