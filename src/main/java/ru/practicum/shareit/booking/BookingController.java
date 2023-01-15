package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingBodyRequest;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingServiceForDbImpl;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemServiceForDbImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserServiceForDbImpl;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private final ItemServiceForDbImpl itemService;
    private final UserServiceForDbImpl userService;
    private final BookingServiceForDbImpl bookingService;
    private final ItemMapper itemMapper;

    @Autowired
    public BookingController(ItemServiceForDbImpl itemService, UserServiceForDbImpl userService,
                             BookingServiceForDbImpl bookingService, ItemMapper itemMapper) {
        this.itemService = itemService;
        this.userService = userService;
        this.bookingService = bookingService;
        this.itemMapper = itemMapper;
    }

    @RequestMapping(method = RequestMethod.POST)
    public BookingDto createBooking(@RequestBody BookingBodyRequest bookingBodyRequest,
                                    @RequestHeader(value = "X-Sharer-User-Id") int ownerId) {
        User user = userService.getUser(ownerId);
        //ItemDto itemDto = itemService.getItemDto(bookingBodyRequest.getItemId(), user);
        Item item = itemService.getItem(bookingBodyRequest.getItemId(), user);
        Booking booking = new Booking();
        //booking.setItem(itemMapper.mapToItem(itemDto));
        booking.setItem(item);
        booking.setStart(bookingBodyRequest.getStart());
        booking.setEnd(bookingBodyRequest.getEnd());
        //return bookingService.createBooking(booking, user, itemDto);
        return bookingService.createBooking(booking, user, item);
    }

    @RequestMapping(method = RequestMethod.PATCH, value = "/{bookingId}")
    public BookingDto verifyBookingRequest(@RequestHeader(value = "X-Sharer-User-Id") int ownerId,
                                    @PathVariable Integer bookingId,
                                    @RequestParam(value = "approved", required = true) String bookingStatus) {
        User user = userService.getUser(ownerId);
        User itemOwner = bookingService.findItemOwnerByBookingId(bookingId);
        BookingDto bookingDto = bookingService.getBookingById(user, bookingId);
        return bookingService.verifyBookingRequest(user, itemOwner, bookingDto, bookingStatus);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{bookingId}")
    public BookingDto getBookingById(@RequestHeader(value = "X-Sharer-User-Id") int ownerId,
                                     @PathVariable Integer bookingId) {
        User user = userService.getUser(ownerId);
        return bookingService.getBookingById(user, bookingId);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<BookingDto> getAllBookings(@RequestHeader(value = "X-Sharer-User-Id") int ownerId,
                                          @RequestParam(value = "state", required = false) String stateType) {
        User user = userService.getUser(ownerId);
        return bookingService.getAllBookings(user, stateType);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/owner")
    public List<BookingDto> getAllBookingByOwner(@RequestHeader(value = "X-Sharer-User-Id") int ownerId,
                                           @RequestParam(value = "state", required = false) String stateType) {
        User user = userService.getUser(ownerId);
        return bookingService.getAllBookingByOwner(user, stateType);
    }




}
