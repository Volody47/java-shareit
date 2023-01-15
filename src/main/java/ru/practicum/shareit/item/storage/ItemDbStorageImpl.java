package ru.practicum.shareit.item.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exceptions.InvalidItemAvailabilityException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.utils.Validator;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class ItemDbStorageImpl implements ItemStorage {
    private final ItemRepository itemStorage;
    private final ItemMapper itemMapper;

    @Autowired
    public ItemDbStorageImpl(ItemRepository itemStorage, ItemMapper itemMapper) {
        this.itemStorage = itemStorage;
        this.itemMapper = itemMapper;
    }

    @Override
    public ItemDto createItem(Item item, User user) {
        if (!item.isAvailable() && getItemDto(item.getId()) == null) {
            log.error("Item Availability can't false or empty");
            throw new InvalidItemAvailabilityException("Field 'available' should be 'true' and can't be empty.");
        }
        Validator.validateItem(item);
        item.setOwner(user);
        itemStorage.save(item);
        log.debug("New item created with id={}", item.getId());
        return itemMapper.mapToItemDto(item);
    }

    @Override
    public ItemDto updateItem(Item item, User user) {
        ItemDto updatedItem = getItemDto(item.getId());
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
            itemStorage.save(item);
            return itemMapper.mapToItemDto(item);
        } else {
            return null;
        }
    }

    @Override
    public ItemDto getItemDto(int id) {
        Optional<Item> item = itemStorage.findById(id);
        return itemMapper.mapToItemDto(item.orElse(null));
    }

    @Override
    public Item getItem(int id) {
        Optional<Item> item = itemStorage.findById(id);
        return item.orElse(null);
    }

    @Override
    public void removeItem(Integer itemId) {
        Optional<Item> item = itemStorage.findById(itemId);
        itemStorage.delete(item.get());
    }

    @Override
    public List<ItemDto> findAllItems(User user) {
        return itemStorage.findAll().stream()
                .filter(item -> (item.getOwner().getId() == user.getId()))
                .sorted(Comparator.comparing(Item::getId, Comparator.naturalOrder()))
                .map(itemMapper::mapToItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> findItemsBaseOnRequest(String text) {
        return itemStorage.findAll().stream()
                .filter(item -> (item.getName().toLowerCase().contains(text)
                        || item.getDescription().toLowerCase().contains(text)) && item.isAvailable())
                .map(itemMapper::mapToItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public User findItemOwner(Item item) {
        User user = itemStorage.findById(item.getId()).get().getOwner();
        if (user != null) {
            return user;
        } else {
            return null;
        }
    }
}
