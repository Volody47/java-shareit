package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ItemNotFoundException;
import ru.practicum.shareit.exceptions.OwnerNotFoundForItemException;
import ru.practicum.shareit.item.dto.CommentForItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.InMemoryItemStorageImpl;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Service
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final InMemoryItemStorageImpl itemStorage;

    @Autowired
    public ItemServiceImpl(InMemoryItemStorageImpl itemStorage) {
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
    public ItemDto getItemDto(int id, User user) {
        ItemDto item = itemStorage.getItemDto(id);
        if (item == null) {
            throw new ItemNotFoundException("Item with id=" + id + " not found.");
        }
        return item;
    }

    @Override
    public Item getItem(int id, User user) {
        Item item = itemStorage.getItem(id);
        if (item == null) {
            throw new ItemNotFoundException("Item with id=" + id + " not found.");
        }
        return item;
    }

    @Override
    public void removeItem(Integer itemId) {
        ItemDto item = itemStorage.getItemDto(itemId);
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

    @Override
    public CommentForItemDto createComment(Comment comment, User user, ItemDto itemDto) {
        return null;
    }


}
