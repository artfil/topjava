package ru.javawebinar.topjava.util.exception;

public class DuplicateEmailCreate extends RuntimeException {
    public DuplicateEmailCreate(String message) {
        super(message);
    }
}
