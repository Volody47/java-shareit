package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface ItemStorage {

    ItemDto createItem(Item item, User user);

    ItemDto updateItem(Item item, User user);

    ItemDto getItem(int id);

    void removeItem(Integer itemId);

    List<ItemDto> findAllItems(User user);

    List<ItemDto> findItemsBaseOnRequest(String text);

    User findItemOwner(Item item);
}
