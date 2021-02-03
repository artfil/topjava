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
    private final Storage storage = meals;
    public static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String stringId = request.getParameter("id");
        String date = request.getParameter("date");
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));
        if (stringId.isEmpty()) {
            storage.add(new Meal(LocalDateTime.parse(date, FORMAT), description, calories));
        } else {
            int id = Integer.parseInt(stringId);
            storage.update(id, new Meal(id, LocalDateTime.parse(date, FORMAT), description, calories));
        }
        response.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("redirect to meals");
        String action = request.getParameter("action");
        if (action == null) {
            request.setAttribute("meals", MealsUtil.filteredByStreams(storage.getAll(), LocalTime.MIN, LocalTime.MAX, CALORIES_PER_DAY));
            request.getRequestDispatcher("/meals.jsp").forward(request, response);
            return;
        }
        Meal meal;
        int id;
        switch (action) {
            case "delete": {
                id = Integer.parseInt(request.getParameter("id"));
                storage.delete(id);
                response.sendRedirect("meals");
                return;
            }
            case "add": {
                meal = new Meal(LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0)), "food name", 0);
                break;
            }
            case "edit": {
                id = Integer.parseInt(request.getParameter("id"));
                meal = storage.get(id);
                break;
            }
            default:
                throw new IllegalArgumentException("Action" + action + " is illegal");
        }
        request.setAttribute("meal", meal);
        request.getRequestDispatcher("/edit.jsp").forward(request, response);
    }
}
