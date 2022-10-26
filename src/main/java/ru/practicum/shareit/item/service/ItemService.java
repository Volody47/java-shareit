package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface ItemService {

    Item createItem(Item item, User user);

    Item updateItem(Item item, User user);

    Item getItem(int id, User user);

    void removeItem(Integer itemId);

    List<Item> findAllItems(User user);

    List<Item> findItemsBaseOnRequest(String text);
}
