package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ItemNotFoundException;
import ru.practicum.shareit.exceptions.OwnerNotFoundForItemException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemDbStorageImpl;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Service
@Slf4j
public class ItemServiceForDbImpl implements ItemService {
    private final ItemDbStorageImpl itemStorage;

    @Autowired
    public ItemServiceForDbImpl(ItemDbStorageImpl itemStorage) {
        this.itemStorage = itemStorage;
    }

    @Override
    public ItemDto createItem(Item item, User user) {
        return itemStorage.createItem(item, user);
    }

    @Override
    public ItemDto updateItem(Item item, User user) {
        if (itemStorage.findItemOwner(item).getId() != user.getId()) {
            throw new OwnerNotFoundForItemException("This item not belongs to User with id=" + user.getId());
        }
        ItemDto updatedItem = itemStorage.updateItem(item, user);
        if (updatedItem == null) {
            throw new ItemNotFoundException("Item with id=" + item.getId() + " not found.");
        }
        return updatedItem;
    }

    @Override
    public ItemDto getItem(int id, User user) {
        ItemDto item = itemStorage.getItem(id);
        if (item == null) {
            throw new ItemNotFoundException("Item with id=" + id + " not found.");
        }
        return item;
    }

    @Override
    public void removeItem(Integer itemId) {
        ItemDto item = itemStorage.getItem(itemId);
        if (item == null) {
            throw new ItemNotFoundException("Item with id=" + itemId + " not found.");
        }
        itemStorage.removeItem(itemId);

    }

    @Override
    public List<ItemDto> findAllItems(User user) {
        log.debug("Items quantity: {}", itemStorage.findAllItems(user).size());
        return itemStorage.findAllItems(user);
    }

    @Override
    public List<ItemDto> findItemsBaseOnRequest(String text) {
        if (text == null || text.isBlank()) {
            log.error("Text can't be empty");
            return List.of();
        }
        String textInLowerCase = text.toLowerCase();

        return itemStorage.findItemsBaseOnRequest(textInLowerCase);
    }
}
