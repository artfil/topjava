package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

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
        System.out.println(filteredByStreams2(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
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
        return null;
    }

    public static List<UserMealWithExcess> filteredByStreams2(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        return meals.stream()
                .collect(new Collector<UserMeal, ArrayList<UserMeal>, List<UserMealWithExcess>>() {
                    @Override
                    public Supplier<ArrayList<UserMeal>> supplier() {
                        return ArrayList::new;
                    }

                    @Override
                    public BiConsumer<ArrayList<UserMeal>, UserMeal> accumulator() {
                        return ArrayList::add;
                    }

                    @Override
                    public BinaryOperator<ArrayList<UserMeal>> combiner() {
                        return (listNew, listOld) -> {
                            listNew.addAll(listOld);
                            return listNew;
                        };
                    }

                    @Override
                    public Function<ArrayList<UserMeal>, List<UserMealWithExcess>> finisher() {
                        return meal -> {
                            Map<LocalDate, Integer> mapCaloriesSumByDate = meal.stream()
                                    .collect(groupingBy(m -> m.getDateTime().toLocalDate(), summingInt(UserMeal::getCalories)));
                            return meal.stream()
                                    .filter(m -> isBetweenHalfOpen(m.getDateTime().toLocalTime(), startTime, endTime))
                                    .map(m -> new UserMealWithExcess(
                                            m.getDateTime(), m.getDescription(), m.getCalories(), mapCaloriesSumByDate.get(m.getDateTime().toLocalDate()) > caloriesPerDay))
                                    .collect(toList());
                        };
                    }

                    @Override
                    public Set<Characteristics> characteristics() {
                        return EnumSet.of(Characteristics.CONCURRENT);
                    }
                });
    }
}