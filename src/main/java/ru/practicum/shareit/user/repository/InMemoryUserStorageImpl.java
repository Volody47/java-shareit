package ru.practicum.shareit.user.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.DuplicateEmailException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.utils.Validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Repository
@Slf4j
public class InMemoryUserStorageImpl implements UserStorage {
    private HashMap<Integer, User> users = new HashMap<>();
    private Integer identificator = 0;


    public int generateId() {
        return ++identificator;
    }

    @Override
    public User createUser(User user) {
        Validator.validateUser(user);
        if (findDuplicateEmail(user)) {
            log.error("Email: '{}' is already taken.", user.getEmail());
            throw new DuplicateEmailException("Email: " + user.getEmail() + " is already taken.");
        }
        user.setId(generateId());
        users.put(user.getId(), user);
        log.debug("New user created with id={}", user.getId());
        return user;
    }

    @Override
    public User updateUser(User user) {
        User updatedUser = users.get(user.getId());
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
            users.put(user.getId(), user);
            log.debug("User with id={} updated", user.getId());
            return user;
        } else {
            return null;
        }
    }

    @Override
    public User getUser(int id) {
        User user = users.get(id);
        if (user != null) {
            return user;
        } else {
            return null;
        }
    }

    @Override
    public void removeUser(Integer userId) {
        users.remove(userId);
        log.debug("User with id={} removed", userId);
    }

    @Override
    public List<User> findAllUsers() {
        ArrayList<User> listOfUsers = new ArrayList<>();
        for (Integer user : users.keySet()) {
            listOfUsers.add(users.get(user));
        }
        log.debug("Users quantity: {}", listOfUsers.size());
        return listOfUsers;
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
