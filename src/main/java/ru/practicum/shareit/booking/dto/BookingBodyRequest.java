package ru.practicum.shareit.booking.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class BookingBodyRequest {
    private int id;
    private int itemId;
    private LocalDateTime start;
    private LocalDateTime end;
}
