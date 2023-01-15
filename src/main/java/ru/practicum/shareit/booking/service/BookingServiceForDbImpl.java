package ru.practicum.shareit.booking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.StateType;
import ru.practicum.shareit.booking.repository.BookingDbRepositoryImpl;
import ru.practicum.shareit.exceptions.BookingNotFoundException;
import ru.practicum.shareit.exceptions.ItemNotFoundException;
import ru.practicum.shareit.exceptions.UserAccessException;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Service
public class BookingServiceForDbImpl implements BookingService {
    private final BookingDbRepositoryImpl bookingRepository;

    @Autowired
    public BookingServiceForDbImpl(BookingDbRepositoryImpl bookingRepository) {
        this.bookingRepository = bookingRepository;
    }


    @Override
    public BookingDto createBooking(Booking booking, User user, Item item) {
        if (user.getId() == booking.getItem().getOwner().getId()) {
            throw new UserAccessException("User with id=" + user.getId() + " is owner of " + item.getName() +".");
        }
        return bookingRepository.createBooking(booking, user, item);
    }

    @Override
    public BookingDto verifyBookingRequest(User user, User itemOwner, BookingDto bookingDto, String bookingStatus) {
        if (user.getId() == itemOwner.getId()) {
            return bookingRepository.verifyBookingRequest(bookingDto, bookingStatus);
        } else {
            throw new UserAccessException("User with id=" + user.getId() + " can't change a status.");
        }
    }

    @Override
    public BookingDto getBookingById(User user, Integer bookingId) {
        User bookingOwner = bookingRepository.findBookingOwner(bookingId);
        User itemOwnerByBookingId = bookingRepository.findItemOwnerByBookingId(bookingId);
        if (bookingOwner == null || itemOwnerByBookingId == null) {
            throw new BookingNotFoundException("Booking with id=" + bookingId + " not found.");
        }
        if (user.getId() == bookingOwner.getId() || user.getId() == itemOwnerByBookingId.getId()) {
            return bookingRepository.getBookingById(bookingId);
        } else {
            throw new UserAccessException("User with id=" + user.getId() + " can't view the booking.");
        }
    }

    @Override
    public User findBookingOwner(Integer bookingId) {
        return bookingRepository.findBookingOwner(bookingId);
    }

    @Override
    public User findItemOwnerByBookingId(Integer bookingId) {
        return bookingRepository.findItemOwnerByBookingId(bookingId);
    }

    @Override
    public List<BookingDto> getAllBookings(User user, String stateType) {
        if (stateType == null) {
            stateType = "ALL";
        }
        List<BookingDto> allBookings = bookingRepository.getAllBookings(user, stateType);
        return allBookings;
    }

    @Override
    public List<BookingDto> getAllBookingByOwner(User user, String stateType) {
        if (stateType == null) {
            stateType = "ALL";
        }
        return bookingRepository.getAllBookingByOwner(user, stateType);
    }


}
