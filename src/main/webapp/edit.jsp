<%@ page import="static ru.javawebinar.topjava.web.MealServlet.FORMAT" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.Meal" scope="request"/>
    <title>Create/Edit</title>
</head>
<body>
<h3 align="center"><a href="index.html">Home</a></h3>
<hr>
<h2 align="center">Create/Edit meal</h2>
<section>
    <form method="post" action="meals" enctype="application/x-www-form-urlencoded">
        <input type="hidden" name="id" value="${meal.id}">
        <p align="center">
            DateTime:
            <input type="text" name="date" size="30" value="<%=meal.getDateTime().format(FORMAT)%>"
                   placeholder="yyyy-MM-dd HH:mm">
            Description:
            <input type="text" name="description" size="30" value="${meal.description}">
            Calories:
            <input type="text" name="calories" size="30" value="${meal.calories}">
        <hr>
        <p align="center">
            <button type="submit">save</button>
            <button onclick="window.history.back()">back</button>
    </form>
</section>
</body>
</html>
