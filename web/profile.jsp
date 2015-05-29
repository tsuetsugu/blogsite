<%-- 
    Document   : profile
    Created on : 2015/05/08, 15:04:36
    Author     : lepra25-pc
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>プロフィール編集画面</h1>
        <s:form action="update">
            
            <table width="100%"  border="1" cellpadding="3" cellspacing="1" bordercolor="#CCCCCC">
                <tr>
                    <td valign="top" bgcolor="#F2F2F2"><s:label value="ユーザ名："/></td>
                    <td valign="top">
                        <s:textfield name="username" value="%{#session.currentUser.username}" size="24" id="user_name"/>
                    </td>
                </tr>
                <tr>
                    <td valign="top" bgcolor="#F2F2F2"><s:label value="出身地："/></td>
                    <td valign="top">
                        <s:select list="%{#session.tdfcode}" name="home" listKey = "code" listValue="code_name" value="%{#session.currentUser.todo_code}"></s:select>
                        </td>
                    </tr>
                    <tr>
                        <td valign="top" bgcolor="#F2F2F2">プロフィール画像</td>
                        <td valign="top">
                            <img src="<s:property value = "%{#session.uploadprofileimage.filepath}"/>" width="193" height="130"/><br><br>
                            <img src="data:image/<s:property value = "contentType"/>;base64,<s:property value = "encodeResult"/>">
                    </td>
                </tr>


                <tr>
                    <td valign="top" bgcolor="#F2F2F2"><s:label value="自己紹介"/></td>
                    <td valign="top">
                        <s:textarea rows="20" cols="70" name="myself" value="%{#session.currentUser.intro_myself}"/>
                    </td>
                </tr>
                <tr align="center" bgcolor="#FFFFFF"> 
                    <td height="40" colspan="2">
                        <s:submit value="更新"/>
                    </s:form>
                    <s:form action="cancel_profile">
                        <s:submit value="キャンセル"/>
                    </s:form>
                </td>
            </tr>     

            <tr align="left" bgcolor="#FFFFFF"> 
                <td height="40" colspan="2">
                    <s:form action="doUpload" method="post" enctype="multipart/form-data">

                        <s:file name="upload" label="File"/>
                        <s:submit value="画像アップロード"/>
                    </s:form>    
                </td>
            </tr>  
        </table>    
    </body>
</html>
