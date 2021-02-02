package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.storage.Storage;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.TestData.*;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private final Storage listStorage = meals;
    private static final int CALORIESPERDAY = 2000;
    public static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String uuid = request.getParameter("uuid");
        String date = request.getParameter("date");
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));
        Meal meal = new Meal(uuid, LocalDateTime.parse(date, FORMAT), description, calories);
        if (listStorage.get(uuid) == null) {
            listStorage.add(meal);
        } else {
            listStorage.update(meal);
        }
        response.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("redirect to meals");
        String uuid = request.getParameter("uuid");
        String action = request.getParameter("action");
        if (action == null) {
            request.setAttribute("meals", MealsUtil.filteredByStreams(listStorage.getAll(), LocalTime.MIN, LocalTime.MAX, CALORIESPERDAY));
            request.getRequestDispatcher("/meals.jsp").forward(request, response);
            return;
        }
        Meal meal;
        switch (action) {
            case "delete": {
                listStorage.delete(uuid);
                response.sendRedirect("meals");
                return;
            }
            case "add": {
                meal = new Meal(LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0)), "food name", 0);
                break;
            }
            case "edit": {
                meal = listStorage.get(uuid);
                break;
            }
            default:
                throw new IllegalArgumentException("Action" + action + " is illegal");
        }
        request.setAttribute("meal", meal);
        request.getRequestDispatcher("/edit.jsp").forward(request, response);
    }
}
