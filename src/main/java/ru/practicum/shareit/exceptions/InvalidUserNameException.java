package ru.practicum.shareit.exceptions;

public class InvalidUserNameException extends RuntimeException {
    public InvalidUserNameException(String s) {
        super(s);
    }
}
