package ru.practicum.shareit.item.dto;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class ItemDto {
    private int id;
    private String name;
    private String description;
    private boolean available;
}
