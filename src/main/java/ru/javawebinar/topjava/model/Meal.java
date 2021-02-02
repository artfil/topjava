package ru.javawebinar.topjava.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;
import java.util.UUID;

public class Meal {
    private final String uuid;

    private final LocalDateTime dateTime;

    private final String description;

    private final int calories;

    public Meal(LocalDateTime dateTime, String description, int calories) {
        this(UUID.randomUUID().toString(), dateTime, description, calories);
    }

    public Meal(String uuid, LocalDateTime dateTime, String description, int calories) {
        this.uuid = uuid;
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
    }

    public String getUuid() {
        return uuid;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getDescription() {
        return description;
    }

    public int getCalories() {
        return calories;
    }

    public LocalDate getDate() {
        return dateTime.toLocalDate();
    }

    public LocalTime getTime() {
        return dateTime.toLocalTime();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Meal meal = (Meal) o;
        return calories == meal.calories && uuid.equals(meal.uuid) && dateTime.equals(meal.dateTime) && description.equals(meal.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, dateTime, description, calories);
    }
}
