package ru.practicum.shareit.item.mapper;

import org.springframework.stereotype.Component;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

@Component
@Mapper(componentModel = "spring") //Creates a Spring Bean automatically
public interface ItemMapper {

    @Mapping(source = "id", target = "id")
    Item mapToItem(ItemDto itemDto);

    @Mapping(source = "id", target = "id")
    ItemDto mapToItemDto(Item item);
}
