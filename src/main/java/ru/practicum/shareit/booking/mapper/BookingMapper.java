package ru.practicum.shareit.booking.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingForItemDto;
import ru.practicum.shareit.booking.model.Booking;

@Component
@Mapper(componentModel = "spring") //Creates a Spring Bean automatically
public interface BookingMapper {

    @Mapping(source = "id", target = "id")
    Booking mapToBooking(BookingDto bookingDto);

    @Mapping(source = "id", target = "id")
    BookingDto mapToBookingDto(Booking booking);

    @Mapping(source = "id", target = "id")
    BookingForItemDto mapToBookingForItemDto(Booking booking);
}
