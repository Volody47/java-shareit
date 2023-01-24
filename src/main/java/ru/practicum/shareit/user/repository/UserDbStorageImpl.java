package ru.practicum.shareit.user.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.DuplicateEmailException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.utils.Validator;

import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
public class UserDbStorageImpl implements UserStorage {
    private final UserRepository userStorage;

    @Autowired
    public UserDbStorageImpl(UserRepository userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public User createUser(User user) {
        Validator.validateUser(user);
        try {
            User userToDb = userStorage.save(user);
            log.debug("New user created with id={}", userToDb.getId());
            return userToDb;
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateEmailException("Email: " + user.getEmail() + " is already taken.");
        }
    }

    @Override
    public User updateUser(User user) {
        User updatedUser = getUser(user.getId());
        if (updatedUser != null) {
            if (user.getName() == null) {
                user.setName(updatedUser.getName());
            } else if (user.getEmail() == null) {
                user.setEmail(updatedUser.getEmail());
            }
            if (findDuplicateEmail(user)) {
                log.error("Email: '{}' is already taken.", user.getEmail());
                throw new DuplicateEmailException("Email: " + user.getEmail() + " is already taken.");
            }
            Validator.validateUser(user);
            userStorage.save(user);
            log.debug("User with id={} updated", user.getId());
            return user;
        } else {
            return null;
        }
    }

    @Override
    public User getUser(int id) {
        Optional<User> user = userStorage.findById(id);
        return user.orElse(null);
    }

    @Override
    public void removeUser(Integer userId) {
        Optional<User> user = userStorage.findById(userId);
        userStorage.delete(user.get());
    }

    @Override
    public List<User> findAllUsers() {
        log.debug("Users quantity: {}", userStorage.findAll().size());
        return userStorage.findAll();
    }

    @Override
    public boolean findDuplicateEmail(User user) {
        List<User> allUsers = findAllUsers();
        for (User existentUser :  allUsers) {
            if (existentUser.getEmail().equals(user.getEmail()) && existentUser.getId() != user.getId()) {
                return true;
            }
        }
        return false;
    }
}
