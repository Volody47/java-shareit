package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Builder
public class BookingForItemDto {
    private int id;

    @JsonIgnore
    private User booker;

    private int bookerId;
    private LocalDateTime start;
    private LocalDateTime end;

    public String getStart() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        return dateTimeFormatter.format(start);
    }

    public String getEnd() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        return dateTimeFormatter.format(end);
    }

    public int getBookerId() {
        return booker.getId();
    }

}
