package ru.javawebinar.topjava.to;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class MealTo {
    @JsonProperty
    private Integer id;
    @JsonProperty
    private LocalDateTime dateTime;
    @JsonProperty
    private String description;
    @JsonProperty
    private int calories;
    @JsonProperty
    private boolean excess;

    public MealTo() {
    }

    public MealTo(Integer id, LocalDateTime dateTime, String description, int calories, boolean excess) {
        this.id = id;
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
        this.excess = excess;
    }

    public Integer getId() {
        return id;
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

    public boolean isExcess() {
        return excess;
    }

    @Override
    public String toString() {
        return "MealTo{" +
                "id=" + id +
                ", dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                ", excess=" + excess +
                '}';
    }
}
