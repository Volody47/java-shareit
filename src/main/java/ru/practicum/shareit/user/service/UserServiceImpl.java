package ru.practicum.shareit.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.InMemoryUserStorageImpl;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final InMemoryUserStorageImpl userStorage;

    @Autowired
    public UserServiceImpl(InMemoryUserStorageImpl userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public User createUser(User user) {
        return null;
    }

    @Override
    public User updateUser(User user) {
        return null;
    }

    @Override
    public User getUser(int id) {
        return null;
    }

    @Override
    public void removeUser(User user) {

    }

    @Override
    public List<User> findAllUsers() {
        return userStorage.findAllUsers();
    }
}
