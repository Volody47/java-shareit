package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface BookingService {

    BookingDto getBookingById(User user, Integer bookingId);

    BookingDto createBooking(Booking booking, User user, Item item);

    BookingDto verifyBookingRequest(User user, User itemOwner, BookingDto bookingDto, String bookingStatus);

    User findBookingOwner(Integer bookingId);

    User findItemOwnerByBookingId(Integer bookingId);


    List<BookingDto> getAllBookings(User user, String stateType);

    List<BookingDto> getAllBookingByOwner(User user, String stateType);
}
