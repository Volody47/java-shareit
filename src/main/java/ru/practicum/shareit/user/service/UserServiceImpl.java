package ru.practicum.shareit.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.InMemoryUserStorageImpl;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final InMemoryUserStorageImpl userStorage;

    @Autowired
    public UserServiceImpl(InMemoryUserStorageImpl userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    @Override
    public User updateUser(User user) {
        User updatedUser = userStorage.updateUser(user);
        if (updatedUser == null) {
            throw new UserNotFoundException("User with id=" + user.getId() + " not found.");
        }
        return updatedUser;
    }

    @Override
    public User getUser(int id) {
        User user = userStorage.getUser(id);
        if (user == null) {
            throw new UserNotFoundException("User with id=" + id + " not found.");
        }
        return user;
    }

    @Override
    public void removeUser(Integer userId) {
        User user = userStorage.getUser(userId);
        if (user == null) {
            throw new UserNotFoundException("User with id=" + userId + " not found.");
        }
        userStorage.removeUser(userId);
    }

    @Override
    public List<User> findAllUsers() {
        log.debug("Users quantity: {}", userStorage.findAllUsers().size());
        return userStorage.findAllUsers();
    }
}
