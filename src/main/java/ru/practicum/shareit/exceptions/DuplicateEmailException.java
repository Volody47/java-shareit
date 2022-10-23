package ru.practicum.shareit.exceptions;

public class DuplicateEmailException extends RuntimeException {
    public DuplicateEmailException(String s) {
        super(s);
    }
}
