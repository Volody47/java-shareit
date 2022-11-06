package ru.practicum.shareit.exceptions;

public class InvalidItemNameException extends RuntimeException {
    public InvalidItemNameException(String s) {
        super(s);
    }
}
