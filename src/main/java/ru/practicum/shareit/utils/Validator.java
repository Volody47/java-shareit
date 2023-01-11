package ru.practicum.shareit.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exceptions.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.sql.Timestamp;
import java.time.LocalDateTime;


@Slf4j
public class Validator {

    public static void validateUser(@RequestBody User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.error("Email: '{}' can't be empty and should contains @", user.getEmail());
            throw new InvalidEmailException("Email can't be empty and should contains @.");
        } else if (user.getName() == null || user.getName().isBlank()) {
            log.error("Name: '{}' can't be empty", user.getName());
            throw new InvalidUserNameException("Name can't be empty.");
        }
    }

    public static void validateItem(@RequestBody Item item) {
        if (item.getName() == null || item.getName().isBlank()) {
            log.error("ItemName: '{}' can't be empty", item.getName());
            throw new InvalidItemNameException("ItemName can't be empty.");
        } else if (item.getDescription() == null || item.getDescription().isBlank()) {
            log.error("ItemDescription: '{}' can't be empty", item.getDescription());
            throw new InvalidItemDescriptionException("ItemDescription can't be empty.");
        }
    }

    public static void validateBooking(@RequestBody Item item, @RequestBody Booking booking) {
        if (!item.isAvailable()) {
            log.error("Item: '{}' not available to book it", item.getName());
            throw new ItemNotAvailableException("Item not available to book it.");
        } else if (booking.getStart().isAfter(booking.getEnd())
                || booking.getStart().isBefore(LocalDateTime.now())) {
            log.error("booking startDate should be before endDate nad can't be in past");
            throw new BookingStartDateException("booking startDate should be before endDate and can't be in past.");
        }
    }
}
