package ru.practicum.shareit.exceptions;

public class InvalidItemDescriptionException extends RuntimeException {
    public InvalidItemDescriptionException(String s) {
        super(s);
    }
}
