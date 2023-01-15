package ru.practicum.shareit.booking.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.StateType;
import ru.practicum.shareit.exceptions.BookingStatusException;
import ru.practicum.shareit.exceptions.DuplicateEmailException;
import ru.practicum.shareit.exceptions.UnsupportedStateException;
import ru.practicum.shareit.exceptions.UserAccessException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.text.DateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.practicum.shareit.utils.Validator.validateBooking;

@Repository
@Slf4j
public class BookingDbRepositoryImpl implements BookingStorage {
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;

    @Autowired
    public BookingDbRepositoryImpl(BookingRepository bookingRepository, BookingMapper bookingMapper) {
        this.bookingRepository = bookingRepository;
        this.bookingMapper = bookingMapper;
    }

    @Override
    public BookingDto getBookingById(Integer bookingId) {
        Optional<Booking> booking = bookingRepository.findById(bookingId);
        return bookingMapper.mapToBookingDto(booking.orElse(null));
    }

    @Override
    public BookingDto createBooking(Booking booking, User user, Item item) {
        validateBooking(item, booking);
        booking.setBooker(user);
        booking.setStatus("WAITING");
        bookingRepository.save(booking);
        log.debug("New booking created with id={}", booking.getId());
        return bookingMapper.mapToBookingDto(booking);
    }

    @Override
    public BookingDto verifyBookingRequest(BookingDto bookingDto, String bookingStatus) {
        if (bookingStatus.equals("true") && !bookingDto.getStatus().equals("APPROVED")) {
            bookingDto.setStatus("APPROVED");
        } else if (bookingStatus.equals("false")) {
            bookingDto.setStatus("REJECTED");
        } else {
            throw new BookingStatusException("Status parameter should be 'true' or 'false'");
        }
        bookingRepository.save(bookingMapper.mapToBooking(bookingDto));
        return bookingDto;
    }

    @Override
    public User findBookingOwner(Integer bookingId) {
        Optional<Booking> userById = bookingRepository.findById(bookingId);
        if (userById.isPresent()) {
            return userById.get().getBooker();
        } else {
            return null;
        }
    }

    @Override
    public User findItemOwnerByBookingId(Integer bookingId) {
        Optional<Booking> userById = bookingRepository.findById(bookingId);
        if (userById.isPresent()) {
            return userById.get().getItem().getOwner();
        } else {
            return null;
        }
    }

    @Override
    public List<BookingDto> getAllBookings(User user, String stateType) {
        List<BookingDto> allBookings = null;
        try {
            if (StateType.valueOf(stateType).equals(StateType.ALL)) {
                allBookings = bookingRepository.findAll().stream()
                        .filter(booking -> (booking.getBooker().equals(user)))
                        .sorted(Comparator.comparing(Booking::getStart, Comparator.reverseOrder()))
                        .map(bookingMapper::mapToBookingDto)
                        .collect(Collectors.toList());
            } else if (StateType.valueOf(stateType).equals(StateType.FUTURE)) {
                allBookings = bookingRepository.findAll().stream()
                        .filter(booking -> (booking.getBooker().equals(user)))
                        .filter(booking -> (booking.getStart().isAfter(LocalDateTime.now())
                                && booking.getEnd().isAfter(LocalDateTime.now())))
                        .sorted(Comparator.comparing(Booking::getStart, Comparator.reverseOrder()))
                        .map(bookingMapper::mapToBookingDto)
                        .collect(Collectors.toList());
            } else if (StateType.valueOf(stateType).equals(StateType.PAST)) {
                allBookings = bookingRepository.findAll().stream()
                        .filter(booking -> (booking.getBooker().equals(user)))
                        .filter(booking -> (booking.getStart().isBefore(LocalDateTime.now())
                                && booking.getEnd().isBefore(LocalDateTime.now())))
                        .map(bookingMapper::mapToBookingDto)
                        .collect(Collectors.toList());
            } else if (StateType.valueOf(stateType).equals(StateType.WAITING)) {
                allBookings = bookingRepository.findAll().stream()
                        .filter(booking -> (booking.getBooker().equals(user)))
                        .filter(booking -> (booking.getStatus().equals("WAITING")))
                        .map(bookingMapper::mapToBookingDto)
                        .collect(Collectors.toList());
            } else if (StateType.valueOf(stateType).equals(StateType.REJECTED)) {
                allBookings = bookingRepository.findAll().stream()
                        .filter(booking -> (booking.getBooker().equals(user)))
                        .filter(booking -> (booking.getStatus().equals("REJECTED")))
                        .map(bookingMapper::mapToBookingDto)
                        .collect(Collectors.toList());
            } else if (StateType.valueOf(stateType).equals(StateType.CURRENT)) {
                allBookings = bookingRepository.findAll().stream()
                        .filter(booking -> (booking.getBooker().equals(user)))
                        .filter(booking -> (booking.getStart().isEqual(LocalDateTime.now())
                                && booking.getEnd().isEqual(LocalDateTime.now())))
                        .map(bookingMapper::mapToBookingDto)
                        .collect(Collectors.toList());
            }
        } catch (IllegalArgumentException e) {
            throw new UnsupportedStateException("Unknown state: " + stateType);
        }
        return allBookings;
    }

    @Override
    public List<BookingDto> getAllBookingByOwner(User user, String stateType) {
        List<BookingDto> allBookings = null;
        try {
            if (StateType.valueOf(stateType).equals(StateType.ALL)) {
                allBookings = bookingRepository.findAll().stream()
                        .filter(booking -> (booking.getItem().getOwner().equals(user)))
                        .sorted(Comparator.comparing(Booking::getStart, Comparator.reverseOrder()))
                        .map(bookingMapper::mapToBookingDto)
                        .collect(Collectors.toList());
            } else if (StateType.valueOf(stateType).equals(StateType.FUTURE)) {
                allBookings = bookingRepository.findAll().stream()
                        .filter(booking -> (booking.getItem().getOwner().equals(user)))
                        .filter(booking -> (booking.getStart().isAfter(LocalDateTime.now())
                                && booking.getEnd().isAfter(LocalDateTime.now())))
                        .sorted(Comparator.comparing(Booking::getStart, Comparator.reverseOrder()))
                        .map(bookingMapper::mapToBookingDto)
                        .collect(Collectors.toList());
            } else if (StateType.valueOf(stateType).equals(StateType.PAST)) {
                allBookings = bookingRepository.findAll().stream()
                        .filter(booking -> (booking.getItem().getOwner().equals(user)))
                        .filter(booking -> (booking.getStart().isBefore(LocalDateTime.now())
                                && booking.getEnd().isBefore(LocalDateTime.now())))
                        .map(bookingMapper::mapToBookingDto)
                        .collect(Collectors.toList());
            } else if (StateType.valueOf(stateType).equals(StateType.WAITING)) {
                allBookings = bookingRepository.findAll().stream()
                        .filter(booking -> (booking.getItem().getOwner().equals(user)))
                        .filter(booking -> (booking.getStatus().equals("WAITING")))
                        .map(bookingMapper::mapToBookingDto)
                        .collect(Collectors.toList());
            } else if (StateType.valueOf(stateType).equals(StateType.REJECTED)) {
                allBookings = bookingRepository.findAll().stream()
                        .filter(booking -> (booking.getItem().getOwner().equals(user)))
                        .filter(booking -> (booking.getStatus().equals("REJECTED")))
                        .map(bookingMapper::mapToBookingDto)
                        .collect(Collectors.toList());
            } else if (StateType.valueOf(stateType).equals(StateType.CURRENT)) {
                allBookings = bookingRepository.findAll().stream()
                        .filter(booking -> (booking.getItem().getOwner().equals(user)))
                        .filter(booking -> (booking.getStart().isEqual(LocalDateTime.now())
                                && booking.getEnd().isEqual(LocalDateTime.now())))
                        .map(bookingMapper::mapToBookingDto)
                        .collect(Collectors.toList());
            }
        } catch (IllegalArgumentException e) {
            throw new UnsupportedStateException("Unknown state: " + stateType);
        }
        return allBookings;
    }


}
