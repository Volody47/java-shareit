package ru.practicum.shareit.booking.repository;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface BookingStorage {

    BookingDto createBooking(Booking booking, User user, Item item);

    BookingDto verifyBookingRequest(BookingDto bookingDto, String bookingStatus);

    BookingDto getBookingById(Integer bookingId);

    User findBookingOwner(Integer bookingId);

    User findItemOwnerByBookingId(Integer bookingId);

    List<BookingDto> getAllBookings(User user, String stateType);

    List<BookingDto> getAllBookingByOwner(User user, String stateType);
}
