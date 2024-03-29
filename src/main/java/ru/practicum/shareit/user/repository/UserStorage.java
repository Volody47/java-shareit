package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserStorage {

    User createUser(User user);

    User updateUser(User user);

    User getUser(int id);

    void removeUser(Integer userId);

    List<User> findAllUsers();

    boolean findDuplicateEmail(User user);
}
