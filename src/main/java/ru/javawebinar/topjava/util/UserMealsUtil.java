package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;

import static java.util.stream.Collectors.*;
import static ru.javawebinar.topjava.util.TimeUtil.*;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);
        System.out.println();
        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
        System.out.println();
        List<UserMealWithExcess> mealsTo2 = filteredByCycles2(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo2.forEach(System.out::println);
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> mapCaloriesSumByDate = new HashMap<>();
        for (UserMeal m : meals) {
            mapCaloriesSumByDate.merge(m.getDateTime().toLocalDate(), m.getCalories(), Integer::sum);
        }
        List<UserMealWithExcess> userMealWithExcesses = new ArrayList<>();
        for (UserMeal m : meals) {
            LocalDateTime localDateTime = m.getDateTime();
            if (isBetweenHalfOpen(localDateTime.toLocalTime(), startTime, endTime)) {
                boolean isExcess = mapCaloriesSumByDate.get(localDateTime.toLocalDate()) > caloriesPerDay;
                userMealWithExcesses.add(new UserMealWithExcess(m.getDateTime(), m.getDescription(), m.getCalories(), isExcess));
            }
        }
        return userMealWithExcesses;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> mapCaloriesSumByDate = meals.stream()
                .collect(groupingBy(m -> m.getDateTime().toLocalDate(), summingInt(UserMeal::getCalories)));

        return meals.stream()
                .filter(m -> isBetweenHalfOpen(m.getDateTime().toLocalTime(), startTime, endTime))
                .map(m -> new UserMealWithExcess(
                        m.getDateTime(), m.getDescription(), m.getCalories(), mapCaloriesSumByDate.get(m.getDateTime().toLocalDate()) > caloriesPerDay))
                .collect(toList());
    }

    public static List<UserMealWithExcess> filteredByCycles2(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        List<UserMealWithExcess> userMealWithExcesses = new ArrayList<>();
        Map<LocalDate, Integer> mapCaloriesSumByDate = new HashMap<>();
        Map<UserMeal, Boolean> setUser = new HashMap<>();
        int saveI = 1;
        for (int i = 1; i <= meals.size(); ) {
            UserMeal userMeal = meals.get(i - 1);
            boolean hasTime = isBetweenHalfOpen(userMeal.getDateTime().toLocalTime(), startTime, endTime);
            if (!setUser.containsKey(userMeal)) {
                mapCaloriesSumByDate.merge(userMeal.getDateTime().toLocalDate(), userMeal.getCalories(), Integer::sum);
                setUser.put(userMeal, false);
            } else {
                setUser.put(userMeal, true);
            }
            if (mapCaloriesSumByDate.get(userMeal.getDateTime().toLocalDate()) > caloriesPerDay && setUser.get(userMeal)) {
                if (hasTime) {
                    userMealWithExcesses.add(new UserMealWithExcess(
                            userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(), true));
                }
            } else {
                if (setUser.get(userMeal)) {
                    if (hasTime) {
                        userMealWithExcesses.add(new UserMealWithExcess(
                                userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(), false));
                    }
                } else {
                    if (i == meals.size()) {
                        if (i == saveI)
                            break;
                    } else {
                        i++;
                        continue;
                    }
                }
            }
            if (i == meals.size()) {
                if (i == saveI)
                    break;
            }
            i = saveI;
            saveI++;
        }
        return userMealWithExcesses;
    }

    public static List<UserMealWithExcess> filteredByStreams2(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        return null;
    }
}
