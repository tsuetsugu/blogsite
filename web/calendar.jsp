<%-- 
    Document   : calendar_web
    Created on : 2015/05/18, 10:08:36
    Author     : lepra25-pc
--%>

<%@page import="java.util.Calendar"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <%
           
            Map<String, String> calMap = (Map<String, String>) request.getAttribute("calMap");
            Calendar cal = Calendar.getInstance();

            String strYear = request.getParameter("year");
            String strMonth = request.getParameter("month");

            int year;
            int month;

            if (strYear != null && strMonth != null) {
                int intMonthTemp = Integer.parseInt(strMonth);

                month = intMonthTemp % 12;
                year = Integer.parseInt(strYear) + intMonthTemp / 12;

                if (month == 0) {
                    month = 12;
                    year--;
                }
                cal.set(Calendar.YEAR, year);
                cal.set(Calendar.MONTH, month - 1);
            }

            year = cal.get(Calendar.YEAR);
            month = cal.get(Calendar.MONTH) + 1;
            cal.set(year, month - 1, 1);
            int firstDayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
            int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

            int day = 2 - firstDayOfWeek;
        %>

        <br>
        <div class="head">
            <!--<a href="myPage.jsp?year=<%=year%>&month=<%=month - 1%>"><<</a> -->
            <s:url id="back_month" action="chmonth">
                <s:param name="year"><%=year%></s:param>
                <s:param name="month"><%=month - 1%></s:param>
            </s:url>                        
            <s:a href="%{back_month}"><<</s:a>           
                <!--現在の年月を表示-->
                <span class="title"><%=year%>年<%=month%>月</span>
            
           <!-- <a href="myPage.jsp?year=<%=year%>&month=<%=month + 1%>">>></a> -->
            <s:url id="next_month" action="chmonth">
                <s:param name="year"><%=year%></s:param>
                <s:param name="month"><%=month + 1%></s:param>
            </s:url>                        
            <s:a href="%{next_month}">>></s:a>               
        </div>        


        <table>
            <tr>
                <!--TH : Table Header-->
                <th class="holiday">日</th>
                <th class="weekday">月</th>
                <th class="weekday">火</th>
                <th class="weekday">水</th>
                <th class="weekday">木</th>
                <th class="weekday">金</th>
                <th class="saturday">土</th>
            <tr>

                <%
                    for (int row = 0; row < 6; row++) {
                %><tr><%
                    for (int col = 0; col < 7; col++) {
                %><%if (1 <= day && day <= lastDay) {%>
                <%if(calMap == null || "0".equals(calMap.get(String.valueOf(day)))) {%>
                <td><%=day%></td>
                <%} else {%>
                <td>
                    <s:url id="select_day" action="selectDay">
                        <s:param name="year"><%=year%></s:param>
                        <s:param name="month"><%=month%></s:param>
                        <s:param name="day"><%=day%></s:param>
                    </s:url>
                    <s:a href="%{select_day}"><%=day%></s:a>
                </td><%}%>
                <%} else {
                %><td>&nbsp</td>
                <%}
                        day++;
                    }
                %></tr><%
                    }
                %></table>


    </body>
</html>
