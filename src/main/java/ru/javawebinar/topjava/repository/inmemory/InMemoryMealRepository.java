package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepository.class);
    private final Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    @Override
    public Meal save(int userId, Meal meal) {
        log.info("save {}", meal);
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meal.setUserId(userId);
            repository.put(meal.getId(), meal);
            return meal;
        }
        // handle case: update, but not present in storage
        return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int userId, int id) {
        log.info("delete {}", id);
        return isUserMeal(userId, id) && repository.remove(id) != null;
    }

    @Override
    public Meal get(int userId, int id) {
        log.info("get {}", id);
        if (isUserMeal(userId, id)) {
            return repository.get(id);
        }
        return null;
    }

    @Override
    public Collection<Meal> getAll(int userId) {
        log.info("getAll");
        return repository.values().stream()
                .filter(meal -> isUserMeal(userId, meal.getId()))
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }

    private boolean isUserMeal(int userId, int id) {
        log.info("getUserMeal {}", userId);
        return repository.get(id).getUserId() == userId;
    }
}

