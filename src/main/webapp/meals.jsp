<%@ page import="static ru.javawebinar.topjava.web.MealServlet.FORMAT" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Meals</title>
</head>
<body>
<h3 align="center"><a href="index.html">Home</a></h3>
<hr>
<h2 align="center">Meals</h2>
<section>
    <h3 align="center"><a href="meals?action=add">Create</a></h3>
    <table border="1" cellpadding="8" cellspacing="0" style="margin: auto;">
        <tr>
            <th>Date</th>
            <th>Description</th>
            <th>Calories</th>
            <th></th>
            <th></th>
        </tr>
        <c:forEach items="${meals}" var="meal">
            <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.MealTo"/>
            ${meal.excess ? '<tr style="color : red;">' : '<tr style="color : green;">'}
            <td><%=meal.getDateTime().format(FORMAT)%>
            </td>
            <td>${meal.description}</td>
            <td>${meal.calories}</td>
            <td><a href="meals?id=${meal.id}&action=edit">Update</a></td>
            <td><a href="meals?id=${meal.id}&action=delete">Delete</a></td>
            </tr>
        </c:forEach>
    </table>
</section>
</body>
</html>
