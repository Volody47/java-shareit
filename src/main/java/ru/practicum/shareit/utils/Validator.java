package ru.practicum.shareit.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import ru.practicum.shareit.exceptions.DuplicateEmailException;
import ru.practicum.shareit.exceptions.InvalidEmailException;
import ru.practicum.shareit.exceptions.InvalidUserNameException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.storage.InMemoryUserStorageImpl;

import java.time.LocalDate;
import java.util.List;

@Component
@Slf4j
public class Validator {
    //private static UserService userStorage;
    private static InMemoryUserStorageImpl userStorage;

    @Autowired
    public Validator(InMemoryUserStorageImpl userStorage) {
        this.userStorage = userStorage;
    }

    public static void validateUser(@RequestBody User user) {
        if(user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
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
}
