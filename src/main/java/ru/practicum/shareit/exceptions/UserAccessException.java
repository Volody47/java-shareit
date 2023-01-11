package ru.practicum.shareit.exceptions;

public class UserAccessException extends RuntimeException {
    public UserAccessException(String s) {
        super(s);
    }
}
