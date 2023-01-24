package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import ru.practicum.shareit.booking.dto.BookingForItemDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Builder
public class ItemDto {
    private int id;
    private String name;
    private String description;
    private boolean available;
    private BookingForItemDto lastBooking;
    private BookingForItemDto nextBooking;
    private List<CommentForItemDto> comments;
    @JsonIgnore
    private User owner;

}

