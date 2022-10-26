package ru.practicum.shareit.exceptions;

public class OwnerNotFoundForItemException extends RuntimeException {
    public OwnerNotFoundForItemException(String s) {
        super(s);
    }
}
