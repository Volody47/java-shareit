package ru.practicum.shareit.exceptions;

public class BookingNotFoundException extends RuntimeException {
    public BookingNotFoundException(String s) {
        super(s);
    }
}
