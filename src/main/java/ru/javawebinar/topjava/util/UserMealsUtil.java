package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;

import static java.util.stream.Collectors.*;
import static ru.javawebinar.topjava.util.TimeUtil.*;

public class UserMealsUtil {
    public static void main(String[] args) {

        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 5000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<UserMeal, Integer> mapCaloriesSumByTime = new HashMap<>();
        for (UserMeal m : meals) {
            if (isBetweenHalfOpen(m.getDateTime().toLocalTime(), startTime, endTime)) {
                mapCaloriesSumByTime.merge(m, m.getCalories(), Integer::sum);
            }
        }
        List<UserMealWithExcess> userMealWithExcesses = new ArrayList<>();
        for (Map.Entry<UserMeal, Integer> m : mapCaloriesSumByTime.entrySet()) {
            UserMeal userMeal = m.getKey();
            boolean isExcess = m.getValue() > caloriesPerDay;
            userMealWithExcesses.add(new UserMealWithExcess(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(), isExcess));
        }
        return userMealWithExcesses;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<UserMeal, Integer> mapCaloriesSumByTime = meals.stream()
                .filter(m -> isBetweenHalfOpen(m.getDateTime().toLocalTime(), startTime, endTime))
                .collect(groupingBy(m -> m, summingInt(UserMeal::getCalories)));

        return mapCaloriesSumByTime.keySet().stream()
                .map(m -> new UserMealWithExcess(
                        m.getDateTime(), m.getDescription(), m.getCalories(), mapCaloriesSumByTime.get(m) > caloriesPerDay))
                .collect(toList());

    }
}
