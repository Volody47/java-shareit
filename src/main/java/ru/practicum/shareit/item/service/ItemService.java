package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface ItemService {

    ItemDto createItem(Item item, User user);

    ItemDto updateItem(Item item, User user);

    ItemDto getItemDto(int id, User user);

    Item getItem(int id, User user);

    void removeItem(Integer itemId);

    List<ItemDto> findAllItems(User user);

    List<ItemDto> findItemsBaseOnRequest(String text);
}
