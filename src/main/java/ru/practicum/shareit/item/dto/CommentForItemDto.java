package ru.practicum.shareit.item.dto;

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
public class CommentForItemDto {
    private int id;
    private String text;

    @JsonIgnore
    private User author;

    private String authorName;
    private LocalDateTime created;

    public String getAuthorName() {
        return author.getName();
    }

    public String getCreated() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        return dateTimeFormatter.format(created);
    }
}
