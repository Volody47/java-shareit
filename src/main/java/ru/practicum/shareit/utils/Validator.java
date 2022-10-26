package ru.practicum.shareit.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import ru.practicum.shareit.exceptions.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.InMemoryItemStorageImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.InMemoryUserStorageImpl;

import java.util.List;

@Component
@Slf4j
public class Validator {
    private static InMemoryUserStorageImpl userStorage;
    private static InMemoryItemStorageImpl itemStorage;


    @Autowired
    public Validator(InMemoryUserStorageImpl userStorage, InMemoryItemStorageImpl itemStorage) {
        Validator.userStorage = userStorage;
        Validator.itemStorage = itemStorage;
    }

    public static void validateUser(@RequestBody User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.error("Email: '{}' can't be empty and should contains @", user.getEmail());
            throw new InvalidEmailException("Email can't be empty and should contains @.");
        }  else if (findDuplicateEmail(user)) {
            log.error("Email: '{}' is already taken.", user.getEmail());
            throw new DuplicateEmailException("Email: " + user.getEmail() + " is already taken.");
        }  else if (user.getName() == null || user.getName().isBlank()) {
            log.error("Name: '{}' can't be empty", user.getName());
            throw new InvalidUserNameException("Name can't be empty.");
        }
    }

    private static boolean findDuplicateEmail(User user) {
        if (userStorage == null) {
            return false;
        } else {
            List<User> allUsers = userStorage.findAllUsers();
            for (User existentUser :  allUsers) {
                if (existentUser.getEmail().equals(user.getEmail()) && existentUser.getId() != user.getId()) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void validateItem(@RequestBody Item item) {
        if (item.getName() == null || item.getName().isBlank()) {
            log.error("ItemName: '{}' can't be empty", item.getName());
            throw new InvalidItemNameException("ItemName can't be empty.");
        } else if (item.getDescription() == null || item.getDescription().isBlank()) {
            log.error("ItemDescription: '{}' can't be empty", item.getDescription());
            throw new InvalidItemDescriptionException("ItemDescription can't be empty.");
        } else if (!item.isAvailable() && itemStorage.getItem(item.getId()) == null) {
            log.error("Item Availability can't false or empty");
            throw new InvalidItemAvailabilityException("Field 'available' should be 'true' and can't be empty.");
        }
    }
}
