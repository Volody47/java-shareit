package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserDbStorageImpl;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.utils.Validator;

import java.util.List;

@Service
@Slf4j
public class UserServiceForDbImpl implements UserService {
    private final UserDbStorageImpl userStorage;


    @Autowired
    public UserServiceForDbImpl(UserDbStorageImpl userStorage) {
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
