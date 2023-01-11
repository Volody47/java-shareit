package ru.practicum.shareit.item.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.InvalidItemAvailabilityException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.utils.Validator;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


@Repository
@Slf4j
public class InMemoryItemStorageImpl implements ItemStorage {
    private HashMap<Integer, Item> items = new HashMap<>();
    private Integer identificator = 0;
    private final ItemMapper itemMapper;

    @Autowired
    public InMemoryItemStorageImpl(ItemMapper itemMapper) {
        this.itemMapper = itemMapper;
    }

    public int generateId() {
        return ++identificator;
    }

    @Override
    public ItemDto createItem(Item item, User user) {
        if (!item.isAvailable() && getItemDto(item.getId()) == null) {
            log.error("Item Availability can't false or empty");
            throw new InvalidItemAvailabilityException("Field 'available' should be 'true' and can't be empty.");
        }
        Validator.validateItem(item);
        item.setId(generateId());
        item.setOwner(user);
        items.put(item.getId(), item);
        log.debug("New item created with id={}", item.getId());
        return itemMapper.mapToItemDto(item);
    }

    @Override
    public ItemDto updateItem(Item item, User user) {
        Item updatedItem = items.get(item.getId());
        if (updatedItem != null) {
            if (item.getName() == null && item.getDescription() == null) {
                item.setName(updatedItem.getName());
                item.setDescription(updatedItem.getDescription());
            } else if (item.getName() == null) {
                item.setName(updatedItem.getName());
                item.setAvailable(updatedItem.isAvailable());
            } else if (item.getDescription() == null) {
                item.setDescription(updatedItem.getDescription());
                item.setAvailable(updatedItem.isAvailable());
            }
            Validator.validateItem(item);
            item.setOwner(user);
            if (!item.isAvailable() && getItemDto(item.getId()) == null) {
                log.error("Item Availability can't false or empty");
                throw new InvalidItemAvailabilityException("Field 'available' should be 'true' and can't be empty.");
            }
            items.put(item.getId(), item);
            return itemMapper.mapToItemDto(item);
        } else {
            return null;
        }
    }

    @Override
    public ItemDto getItemDto(int id) {
        Item item = items.get(id);
        if (item != null) {
            return itemMapper.mapToItemDto(item);
        } else {
            return null;
        }
    }

    @Override
    public Item getItem(int id) {
        Item item = items.get(id);
        if (item != null) {
            return item;
        } else {
            return null;
        }
    }

    @Override
    public void removeItem(Integer itemId) {
        items.remove(itemId);
        log.debug("Item with id={} removed", itemId);
    }

    @Override
    public List<ItemDto> findAllItems(User user) {
        return items.values().stream()
                .filter(item -> (item.getOwner().getId() == user.getId()))
                .map(itemMapper::mapToItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> findItemsBaseOnRequest(String text) {
        return items.values().stream()
                .filter(item -> (item.getName().toLowerCase().contains(text)
                        || item.getDescription().toLowerCase().contains(text)) && item.isAvailable())
                .map(itemMapper::mapToItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public User findItemOwner(Item item) {
        User user = items.get(item.getId()).getOwner();
        if (user != null) {
            return user;
        } else {
            return null;
        }
    }
}
