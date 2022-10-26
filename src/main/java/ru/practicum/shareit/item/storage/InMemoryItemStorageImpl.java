package ru.practicum.shareit.item.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.InvalidItemAvailabilityException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.utils.Validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Repository
@Slf4j
public class InMemoryItemStorageImpl implements ItemStorage {
    private HashMap<Integer, Item> items = new HashMap<>();
    private Integer identificator = 0;

    public int generateId() {
        return ++identificator;
    }

    @Override
    public Item createItem(Item item, User user) {
        if (!item.isAvailable() && getItem(item.getId()) == null) {
            log.error("Item Availability can't false or empty");
            throw new InvalidItemAvailabilityException("Field 'available' should be 'true' and can't be empty.");
        }
        Validator.validateItem(item);
        item.setId(generateId());
        item.setOwner(user);
        items.put(item.getId(), item);
        log.debug("New item created with id={}", item.getId());
        return item;
    }

    @Override
    public Item updateItem(Item item, User user) {
        Item updatedItem = null;
        for (int itemId : items.keySet()) {
            if (itemId == item.getId()) {
                if (item.getName() == null && item.getDescription() == null) {
                    item.setName(items.get(itemId).getName());
                    item.setDescription(items.get(itemId).getDescription());
                } else if (item.getName() == null) {
                    item.setName(items.get(itemId).getName());
                    item.setAvailable(items.get(itemId).isAvailable());
                } else if (item.getDescription() == null) {
                    item.setDescription(items.get(itemId).getDescription());
                    item.setAvailable(items.get(itemId).isAvailable());
                }
                if (!item.isAvailable() && getItem(item.getId()) == null) {
                    log.error("Item Availability can't false or empty");
                    throw new InvalidItemAvailabilityException("Field 'available' should be 'true' and can't be empty.");
                }
                Validator.validateItem(item);
                item.setOwner(user);
                items.put(item.getId(), item);
                updatedItem = items.get(item.getId());
                log.debug("Item with id={} updated", item.getId());
            }
        }
        return updatedItem;
    }

    @Override
    public Item getItem(int id) {
        Item item = null;
        for (Integer itemId : items.keySet()) {
            if (itemId == id) {
                item = items.get(id);
            }
        }
        return item;
    }

    @Override
    public void removeItem(Integer itemId) {
        items.remove(itemId);
        log.debug("Item with id={} removed", itemId);
    }

    @Override
    public List<Item> findAllItems(User user) {
        ArrayList<Item> listOfItems = new ArrayList<>();
        for (Item item : items.values()) {
            if (item.getOwner().getId() == user.getId())
            listOfItems.add(item);
        }
        return listOfItems;
    }

    @Override
    public List<Item> findItemsBaseOnRequest(String text) {
        ArrayList<Item> listOfItems = new ArrayList<>();
        for (Item item : items.values()) {
            String nameInLowerCase = item.getName().toLowerCase();
            String descriptionInLowerCase = item.getDescription().toLowerCase();
            if (item.isAvailable() &&
                    (nameInLowerCase.contains(text) || descriptionInLowerCase.contains(text))) {
                listOfItems.add(item);
            }
        }
        return listOfItems;
    }
}
